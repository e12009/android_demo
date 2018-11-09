package com.xinde.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;
import com.xinde.reponse.TaskCreationResponse;
import com.xinde.reponse.TaskStatusResponse;
import com.xinde.storage.Storage;
import com.xinde.storage.item.AuthInfo;
import com.xinde.storage.item.CarrierInfo;
import com.xinde.util.XindeSignatureBuilder;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;


public class TaskActivity extends AppCompatActivity {
    private static final String TAG = "TaskActivity";

    private static final int MSG_CREATE_TASK = 0x01;
    private static final int MSG_CREATE_TASK_FEEDBACK = 0x11;
    private static final int MSG_CHECK_TASK_STATUS = 0x02;
    private static final int MSG_CHECK_TASK_STATUS_FEEDBACK = 0x22;
    private static final int MSG_TASK_SUSPENDED = 0x03;
    private static final int MSG_TASK_SUSPENDED_FEEDBACK = 0x33;
    private static final int MSG_TASK_ABORT = 0x44;

    private Context mContext = null;
    private Transport mTransport = null;

    private View mProcessView = null;
    private View mSuspendedView = null;
    private View mFailureView = null;
    private View mSuccessView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mContext = this;
        mProcessView = findViewById(R.id.progress_view);
        mSuspendedView = findViewById(R.id.suspended_view);
        mFailureView = findViewById(R.id.suspended_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTransport = new Transport();

        mHandler.sendEmptyMessage(MSG_CREATE_TASK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset_auth) {
            Intent intent = new Intent(TaskActivity.this, AuthInfoActivity.class);
            startActivityForResult(intent, 200);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // just ignore result from AuthInfoActivity
    }

    private void hideViews(View... views) {
        for (View view : views) {
            if (null == view) continue;

            if (View.VISIBLE == view.getVisibility()) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showView(View view) {
        if (null != view) {
            if (View.VISIBLE != view.getVisibility()) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showOngoingMessage(String msg) {
        hideViews(mFailureView, mSuspendedView, mSuccessView);

        TextView textView = (TextView) mProcessView.findViewById(R.id.progress_message);
        textView.setText(msg);
        showView(mProcessView);

        ProgressBar progressBar = (ProgressBar) mProcessView.findViewById(R.id.progressbar);
        showView(progressBar);
    }

    private void showAbortMessage(String msg) {
        ProgressBar progressBar = (ProgressBar) mProcessView.findViewById(R.id.progressbar);
        hideViews(mFailureView, mSuspendedView, mSuccessView, progressBar);

        TextView textView = (TextView) mProcessView.findViewById(R.id.progress_message);
        textView.setText(msg);
        showView(mProcessView);

    }

    private Handler mHandler = new Handler() {
        /**
         * Subclasses must implement this to receive messages.
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CREATE_TASK:
                    showOngoingMessage(getString(R.string.progress_status_login));
                    mTransport.createTask();
                    break;

                case MSG_CHECK_TASK_STATUS:
                    mTransport.checkTaskStatus();;
                    break;

                case MSG_CHECK_TASK_STATUS_FEEDBACK:
                    TaskStatusResponse response = (TaskStatusResponse) msg.obj;

                    if (null == response) break;

                    if (response.isProcessing()) {
                        if (response.isLogin()) {
                            showOngoingMessage(getString(R.string.progress_status_login));
                        } else if (response.isFetching()) {
                            showOngoingMessage(getString(R.string.progress_status_fetching));
                        }

                        mHandler.sendEmptyMessageDelayed(MSG_CHECK_TASK_STATUS, 5000L);
                    }
                    else if (response.isFailed()) {
                        String errorInfo = "task " + response.getStatus() + ", "
                                + "reason:" + response.getReason() + ", "
                                + "failedCode:" + response.getFailCode();

                        mHandler.sendMessage(mHandler.obtainMessage(MSG_TASK_ABORT, errorInfo));
                    }
                    else if (response.isSuspended()) {
                        String hintMessage = "task suspended";
                        mHandler.sendMessage(mHandler.obtainMessage(MSG_TASK_SUSPENDED, hintMessage));
                    }
                    else if (response.isDone()) {
                        String hintMessage = "task done";
                        mHandler.sendMessage(mHandler.obtainMessage(MSG_TASK_SUSPENDED, hintMessage));
                    }

                    break;

                case MSG_TASK_ABORT:
                    String abortMsg = (String) msg.obj;
                    showAbortMessage(abortMsg);
                    break;



            }
            super.handleMessage(msg);

        }
    };

    private class Transport {
        private static final String TAG = "Transport";

        private static final String XINDE_API_URL = "https://api.xindedata.com/v1/task";

        private OkHttpClient client;
        private String tid = null;

        public Transport() {
            X509TrustManager trustManager;
            SSLSocketFactory sslSocketFactory;
            final InputStream inputStream;
            try {
                inputStream = mContext.getAssets().open("xinde_ca.cer"); // 得到证书的输入流
                try {

                    trustManager = trustManagerForCertificates(inputStream);//以流的方式读入证书
                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(null, new TrustManager[]{trustManager}, null);
                    sslSocketFactory = sslContext.getSocketFactory();

                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                }

                client = new OkHttpClient.Builder()
                        .connectTimeout(30L, TimeUnit.SECONDS)
                        .readTimeout(30L, TimeUnit.SECONDS)
                        .hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                Log.i(TAG, "verify hostname - " + hostname);
                                return "api.xindedata.com".equals(hostname);
                            }
                        })
                        .sslSocketFactory(sslSocketFactory, trustManager)
                        .build();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        /**
         * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
         * certificates have not been signed by these certificates will fail with a {@code
         * SSLHandshakeException}.
         * <p>
         * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
         * set. This is useful in development where certificate authority-trusted certificates aren't
         * available. Or in production, to avoid reliance on third-party certificate authorities.
         * <p>
         * <p>
         * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
         * <p>
         * <p>Relying on your own trusted certificates limits your server team's ability to update their
         * TLS certificates. By installing a specific set of trusted certificates, you take on additional
         * operational complexity and limit your ability to migrate between certificate authorities. Do
         * not use custom trusted certificates in production without the blessing of your server's TLS
         * administrator.
         */
        private X509TrustManager trustManagerForCertificates(InputStream in) throws GeneralSecurityException {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
            if (certificates.isEmpty()) {
                throw new IllegalArgumentException("expected non-empty set of trusted certificates");
            }

            // Put the certificates a key store.
            char[] password = "password".toCharArray(); // Any password will work.
            KeyStore keyStore = newEmptyKeyStore(password);
            int index = 0;
            for (Certificate certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificate);
            }

            // Use it to build an X509 trust manager.
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                    KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        }


        /**
         * 添加password
         * @param password
         * @return
         * @throws GeneralSecurityException
         */
        private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
            try {
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); // 这里添加自定义的密码，默认
                InputStream in = null; // By convention, 'null' creates an empty key store.
                keyStore.load(in, password);
                return keyStore;
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }


        public void createTask() {
            AuthInfo authInfo = Storage.getInstance().getAuthInfo(mContext);
            CarrierInfo carrierInfo = Storage.getInstance().getCarrierInfo(mContext);

            Log.i(TAG, "[x] create Task");
            Log.i(TAG, authInfo.toString());
            Log.i(TAG, carrierInfo.toString());

            String timestamp = String.valueOf(System.currentTimeMillis()/1000);
            String signature = new XindeSignatureBuilder(authInfo.getAppSecret())
                    .addParamStringPair("appid", authInfo.getAppId())
                    .addParamStringPair("time", timestamp)
                    .build();
            String jsonBody = new Gson().toJson(carrierInfo);

            HttpUrl.Builder httpBuilder = HttpUrl.parse(XINDE_API_URL).newBuilder()
                    .addQueryParameter("appid", authInfo.getAppId())
                    .addQueryParameter("time", timestamp)
                    .addQueryParameter("signature", signature);

            Request request = new Request.Builder()
                    .url(httpBuilder.build())
                    .post(RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonBody))
                    .build();


            {
                //FIXME:
                Log.i(TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                Log.i(TAG, "url:" + httpBuilder.build());
                Log.i(TAG, "jsonBody:" + jsonBody);
                Log.i(TAG, "body:" + RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonBody).toString());
            }

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "[-] failure - " + e.getMessage());
                    Message abortMsg = mHandler.obtainMessage(MSG_TASK_ABORT, e.getMessage());
                    mHandler.sendMessage(abortMsg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String jsonBody = response.body().string();
                        Log.i(TAG, "[-] succeeded - " + jsonBody);

                        TaskCreationResponse result = new Gson().fromJson(jsonBody, TaskCreationResponse.class);
                        tid = result.getTid();

                        Log.i(TAG, "[-] " + result.toString());

                        mHandler.sendEmptyMessage(MSG_CHECK_TASK_STATUS);
                    } else {
                        String errorMsg = response.body().string();
                        Log.e(TAG, errorMsg);
                        Message abortMsg = mHandler.obtainMessage(MSG_TASK_ABORT, errorMsg);
                        mHandler.sendMessage(abortMsg);
                    }
                }
            });



        }

        public void checkTaskStatus() {
            AuthInfo authInfo = Storage.getInstance().getAuthInfo(mContext);

            Log.i(TAG, "[x] Check Task Status");
            Log.i(TAG, authInfo.toString());

            String timestamp = String.valueOf(System.currentTimeMillis()/1000);
            String signature = new XindeSignatureBuilder(authInfo.getAppSecret())
                    .addParamStringPair("tid", tid)
                    .addParamStringPair("appid", authInfo.getAppId())
                    .addParamStringPair("time", timestamp)
                    .build();

            HttpUrl.Builder httpBuilder = HttpUrl.parse(XINDE_API_URL).newBuilder()
                    .addQueryParameter("tid", tid)
                    .addQueryParameter("appid", authInfo.getAppId())
                    .addQueryParameter("time", timestamp)
                    .addQueryParameter("signature", signature);

            Request request = new Request.Builder()
                    .url(httpBuilder.build())
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "[-] failure - " + e.getMessage());
                    Message abortMsg = mHandler.obtainMessage(MSG_TASK_ABORT, e.getMessage());
                    mHandler.sendMessage(abortMsg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String jsonBody = response.body().string();
                        Log.i(TAG, "[-] succeeded - " + jsonBody);

                        TaskStatusResponse result = new Gson().fromJson(jsonBody, TaskStatusResponse.class);
                        Log.i(TAG, "[-] " + result.toString());

                        Message msg = mHandler.obtainMessage(MSG_CHECK_TASK_STATUS_FEEDBACK);
                        msg.obj = result;

                        mHandler.sendMessage(msg);
                    } else {
                        String errorMsg = response.body().string();
                        Log.e(TAG, errorMsg);
                        Message abortMsg = mHandler.obtainMessage(MSG_TASK_ABORT, errorMsg);
                        mHandler.sendMessage(abortMsg);
                    }
                }
            });
        }
    }
}
