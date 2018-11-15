package com.xinde.network;

import android.content.Context;
import android.util.Log;
import com.xinde.storage.Storage;
import com.xinde.storage.item.AuthInfo;
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

/**
 * 网络请求类
 *
 * 用户处理安全认证，任务创建，任务状态查询，继续挂起的任务
 */
public class Transporter {
    private static final String TAG = "Transporter";

    private static final String XINDE_API_URL = "https://api.xindedata.com/v1/task";
    // 信德数聚的证书文件
    private static final String XINDE_CER = "xinde_ca.cer";

    private OkHttpClient client;
    private Context mContext;

    // 创建任务时使用的callback
    private Callback mCreateTaskCallback;
    // 查询任务时使用的callback
    private Callback mCheckTaskCallback;
    // 继续任务时使用的callback
    private Callback mResumeTaskCallback;

    public Transporter(Context context) {
        setup(context, null, null, null);
    }

    public Transporter(Context context, Callback createTaskCallback,
                       Callback resumeTaskCallback, Callback checkTaskCallback) {
        setup(context, createTaskCallback, resumeTaskCallback, checkTaskCallback);
    }

    /**
     * 初始化
     *
     * @param context               Context实例
     * @param createTaskCallback    为创建任务准备的callback
     * @param resumeTaskCallback    为继续挂起的任务准备的callback
     * @param checkTaskCallback     为检查任务状态准备的callback
     */
    private void setup(Context context, Callback createTaskCallback,
                       Callback resumeTaskCallback, Callback checkTaskCallback) {

        this.mContext = context;
        mCreateTaskCallback = createTaskCallback;
        mResumeTaskCallback = resumeTaskCallback;
        mCheckTaskCallback = checkTaskCallback;

        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;
        final InputStream inputStream;

        try {
            // 得到证书的输入流
            inputStream = mContext.getAssets().open(XINDE_CER);
            try {
                //以流的方式读入证书
                trustManager = trustManagerForCertificates(inputStream);
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{ trustManager }, null);
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
     *
     * @param password
     * @return KeyStore
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

    /**
     * 创建任务
     *
     * @param body  需要post的request body
     */
    public void createTask(RequestBody body) {
        AuthInfo authInfo = Storage.getInstance().getAuthInfo(mContext);

        Log.i(TAG, authInfo.toString());
        Log.i(TAG, "[x] create task");

        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        // 获取签名
        String signature = new XindeSignatureBuilder(authInfo.getAppSecret())
                .addParamStringPair("appid", authInfo.getAppId())
                .addParamStringPair("time", timestamp)
                .build();

        HttpUrl.Builder httpBuilder = HttpUrl.parse(XINDE_API_URL).newBuilder()
                .addQueryParameter("appid", authInfo.getAppId())
                .addQueryParameter("time", timestamp)
                .addQueryParameter("signature", signature);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .post(body)
                .build();

        Callback callback = mCreateTaskCallback != null ? mCreateTaskCallback : defaultCallback;
        client.newCall(request).enqueue(callback);

    }

    /**
     * callback assignment
     *
     * @param callback  callback for task creation
     */
    public void setCreateTaskCallback(Callback callback) {
        mCreateTaskCallback = callback;
    }

    /**
     * 查询任务状态
     *
     * @param tid   需要查询的任务的tid
     */
    public void checkTaskStatus(String tid) {
        AuthInfo authInfo = Storage.getInstance().getAuthInfo(mContext);

        Log.i(TAG, "[x] check task status");

        // 获取当前时间
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        // 获取signature
        String signature = new XindeSignatureBuilder(authInfo.getAppSecret())
                .addParamStringPair("tid", tid)
                .addParamStringPair("appid", authInfo.getAppId())
                .addParamStringPair("time", timestamp)
                .build();

        // 创建 URL
        HttpUrl.Builder httpBuilder = HttpUrl.parse(XINDE_API_URL).newBuilder()
                .addQueryParameter("tid", tid)
                .addQueryParameter("appid", authInfo.getAppId())
                .addQueryParameter("time", timestamp)
                .addQueryParameter("signature", signature);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .get()
                .build();

        Callback callback = mCheckTaskCallback != null ? mCheckTaskCallback : defaultCallback;
        client.newCall(request).enqueue(callback);
    }

    /**
     * 设置查询任务时用的callback
     *
     * @param callback callback
     */
    public void setCheckTaskCallback(Callback callback) {
        mCheckTaskCallback = callback;
    }

    /**
     * 继续挂起的任务
     *
     * @param body 需要post的body，用来提供进一步的信息以便任务的继续
     *
     */
    public void resumeTask(RequestBody body) {
        AuthInfo authInfo = Storage.getInstance().getAuthInfo(mContext);

        Log.i(TAG, authInfo.toString());
        Log.i(TAG, "[x] resume Task");


        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        String signature = new XindeSignatureBuilder(authInfo.getAppSecret())
                .addParamStringPair("appid", authInfo.getAppId())
                .addParamStringPair("time", timestamp)
                .build();


        HttpUrl.Builder httpBuilder = HttpUrl.parse(XINDE_API_URL).newBuilder()
                .addQueryParameter("appid", authInfo.getAppId())
                .addQueryParameter("time", timestamp)
                .addQueryParameter("signature", signature);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .post(body)
                .build();

        Callback callback = mResumeTaskCallback != null ? mResumeTaskCallback : null;

        client.newCall(request).enqueue(callback);

    }

    /**
     * setup callback used by task resuming routine
     *
     * @param callback callback
     */
    public void setResumeTaskCallback(Callback callback) {
        mResumeTaskCallback = callback;
    }

    /**
     * callback by default
     */
    private final Callback defaultCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(TAG, "[-] IOException - " + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                Log.i(TAG, "[-] successful response received.");
            } else {
                Log.e(TAG, "[-] failed response received.");
            }
        }
    };
}
