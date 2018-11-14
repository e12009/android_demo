package com.xinde.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xinde.network.Transporter;
import com.xinde.reponse.TaskCreationResponse;
import com.xinde.reponse.ZhimafenTaskStatusResponse;
import com.xinde.reponse.taskresult.ZhimafenDetail;
import com.xinde.storage.Storage;
import com.xinde.storage.item.ZhimafenInfo;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;

public class ZhimafenTaskActivity extends AppCompatActivity {
    private static final String TAG = "ZhimafenTaskActivity";

    private static final int MY_REQ_CODE = 0x200;

    private static final int MSG_CREATE_TASK = 0x01;
    private static final int MSG_CHECK_TASK_STATUS = 0x02;
    private static final int MSG_CHECK_TASK_STATUS_FEEDBACK = 0x22;
    private static final int MSG_TASK_SUSPENDED = 0x03;
    private static final int MSG_TASK_ABORT = 0x04;
    private static final int MSG_TASK_DONE = 0x05;

    private Context mContext = null;
    private ZhimafenTaskActivity.Transport mTransport = null;
    private ZhimafenTaskStatusResponse<ZhimafenDetail> mCurrentTaskStatusResp = null;

    private View mProcessView = null;
    private View mFailureView = null;
    private View mSuccessView = null;

    private boolean misDetectingAlipayApp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhimafen_task);

        mContext = this;

        mProcessView = findViewById(R.id.progress_view);
        mFailureView = findViewById(R.id.failure_view);
        mSuccessView = (CardView) findViewById(R.id.show_score);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTransport = new ZhimafenTaskActivity.Transport();

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
            Intent intent = new Intent(ZhimafenTaskActivity.this, AuthInfoActivity.class);
            startActivityForResult(intent, MY_REQ_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (MY_REQ_CODE != requestCode) return;

        super.onActivityResult(requestCode, resultCode, data);
        // just ignore result from AuthInfoActivity
    }

    private boolean hideSoftKeyboard(View activeView) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm && null != activeView) {
            imm.hideSoftInputFromWindow(activeView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        }

        return false;
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
        hideViews(mFailureView, mSuccessView);

        TextView textView = (TextView) mProcessView.findViewById(R.id.progress_message);
        textView.setText(msg);
        showView(mProcessView);

        ProgressBar progressBar = (ProgressBar) mProcessView.findViewById(R.id.progressbar);
        showView(progressBar);
    }

    private void processSuspendedStatus() {
        if (null == mCurrentTaskStatusResp) {
            Log.e(TAG, "no suspended status available");
            return;
        }

        String url = mCurrentTaskStatusResp.getQR().getUrl();
        handleAlipayProtocol(mContext, url);

        if (misDetectingAlipayApp) {
            mHandler.sendEmptyMessageDelayed(MSG_CHECK_TASK_STATUS, 5000L);
        }
    }

    private void showAbortMessage(String msg) {
        ProgressBar progressBar = (ProgressBar) mProcessView.findViewById(R.id.progressbar);
        hideViews(mFailureView, mSuccessView, progressBar);

        TextView textView = (TextView) mProcessView.findViewById(R.id.progress_message);
        textView.setText(msg);
        showView(mProcessView);

    }

    private void showTaskResult() {
        hideViews(mFailureView, mProcessView);
        showView(mSuccessView);

        TextView textView = (TextView) mSuccessView.findViewById(R.id.show_zhimafen_score);
        textView.setText(String.valueOf(mCurrentTaskStatusResp.getResult().getZhimafen()));
    }

    public void handleAlipayProtocol(final Context context, String url) {

        if(url.startsWith("alipays:") || url.startsWith("alipay:")) {
            try {
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                Log.i("TAG", "OK, alipay app should be launched now.");

                misDetectingAlipayApp = true;

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showOngoingMessage(getString(R.string.launch_alipay_app));
                        Toast.makeText(mContext, getString(R.string.launch_alipay_app), Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                Log.e("TAG", "No alipay app on this device!");

                // we are in UI thread context
                new AlertDialog.Builder(context)
                        .setMessage("未检测到支付宝客户端，请安装后重试。")
                        .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri alipayUrl = Uri.parse("https://d.alipay.com");
                                context.startActivity(new Intent("android.intent.action.VIEW", alipayUrl));

                                misDetectingAlipayApp = true;
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }


        }

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
                    mCurrentTaskStatusResp = (ZhimafenTaskStatusResponse<ZhimafenDetail>) msg.obj;

                    if (null == mCurrentTaskStatusResp) break;

                    if (mCurrentTaskStatusResp.isFailed()) {
                        String errorInfo = "task " + mCurrentTaskStatusResp.getStatus() + ", "
                                + "reason:" + mCurrentTaskStatusResp.getReason() + ", "
                                + "failedCode:" + mCurrentTaskStatusResp.getFailCode();

                        mHandler.sendMessage(mHandler.obtainMessage(MSG_TASK_ABORT, errorInfo));
                    }
                    else if (mCurrentTaskStatusResp.isSuspended()) {
                        if (!misDetectingAlipayApp) {
                            mHandler.sendMessage(mHandler.obtainMessage(MSG_TASK_SUSPENDED, mCurrentTaskStatusResp));
                        } else {
                            Log.i(TAG, "keep on checking task status after trying to launch or install alipay app");
                            mHandler.sendEmptyMessageDelayed(MSG_CHECK_TASK_STATUS, 5000L);
                        }
                    }
                    else if (mCurrentTaskStatusResp.isDone()) {
                        mHandler.sendMessage(mHandler.obtainMessage(MSG_TASK_DONE, mCurrentTaskStatusResp));
                    }

                    break;

                case MSG_TASK_SUSPENDED:
                    mCurrentTaskStatusResp = (ZhimafenTaskStatusResponse<ZhimafenDetail>) msg.obj;

                    if (null == mCurrentTaskStatusResp) break;
                    else processSuspendedStatus();

                    break;

                case MSG_TASK_ABORT:
                    Log.i(TAG, "task terminated, please check out the reason in detail");
                    String abortMsg = (String) msg.obj;
                    showAbortMessage(abortMsg);
                    break;

                case MSG_TASK_DONE:
                    mCurrentTaskStatusResp = (ZhimafenTaskStatusResponse<ZhimafenDetail>) msg.obj;

                    Log.i(TAG, "task succeeded, and we get the final result, just show it now.");
                    showTaskResult();
                    break;

            }

            super.handleMessage(msg);

        }
    };

    private class Transport {
        private Transporter transporter;
        private String tid = null;

        public Transport() {
            transporter = new Transporter(mContext, genericCallback, genericCallback, checkTaskStatusCallback);
        }


        public void createTask() {
            ZhimafenInfo zhimafenInfo = Storage.getInstance().getZhimafenInfo(mContext);

            Log.i(TAG, zhimafenInfo.toString());

            String jsonBody = new Gson().toJson(zhimafenInfo);
            RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonBody);

            transporter.createTask(body);
        }

        public void checkTaskStatus() {
            transporter.checkTaskStatus(tid);
        }


        private final Callback genericCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "[-] zhimafen failure - " + e.getMessage());
                Message abortMsg = mHandler.obtainMessage(MSG_TASK_ABORT, e.getMessage());
                mHandler.sendMessage(abortMsg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    TaskCreationResponse result = new Gson().fromJson(response.body().charStream(), TaskCreationResponse.class);
                    tid = result.getTid();

                    Log.i(TAG, "[-] zhimafen succeeded - " + result.toString());

                    mHandler.sendEmptyMessage(MSG_CHECK_TASK_STATUS);
                } else {
                    String errorMsg = response.body().string();
                    Log.e(TAG, "[-] zhimafen failure - " + errorMsg);
                    Message abortMsg = mHandler.obtainMessage(MSG_TASK_ABORT, errorMsg);
                    mHandler.sendMessage(abortMsg);
                }
            }
        };

        private final Callback checkTaskStatusCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "[-] zhimafen failure - " + e.getMessage());
                Message abortMsg = mHandler.obtainMessage(MSG_TASK_ABORT, e.getMessage());
                mHandler.sendMessage(abortMsg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    Type targetClz = new TypeToken<ZhimafenTaskStatusResponse<ZhimafenDetail>>(){}.getType();
                    ZhimafenTaskStatusResponse<ZhimafenDetail> result = new Gson().fromJson(response.body().charStream(), targetClz);
                    Log.i(TAG, "[-] zhimafen succeeded - " + result.toString());

                    Message msg = mHandler.obtainMessage(MSG_CHECK_TASK_STATUS_FEEDBACK);
                    msg.obj = result;

                    mHandler.sendMessage(msg);
                } else {
                    String errorMsg = response.body().string();
                    Log.e(TAG, "[-] zhimafen failure - " + errorMsg);
                    Message abortMsg = mHandler.obtainMessage(MSG_TASK_ABORT, errorMsg);
                    mHandler.sendMessage(abortMsg);
                }
            }
        };
    }
}
