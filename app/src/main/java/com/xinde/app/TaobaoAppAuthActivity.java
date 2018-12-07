package com.xinde.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xinde.network.Transporter;
import com.xinde.reponse.TaobaoAppAuthTaskStatusResponse;
import com.xinde.reponse.TaskCreationResponse;
import com.xinde.reponse.TaskStatusResponse;
import com.xinde.reponse.taskresult.TaobaoDetail;
import com.xinde.resume.SmsCodeItem;
import com.xinde.storage.Storage;
import com.xinde.storage.item.AuthInfo;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.TreeMap;

public class TaobaoAppAuthActivity extends AppCompatActivity {
    private static final String TAG = "TaobaoAppAuthActivity";

    private static final int MY_REQ_CONFIG_CODE = 0x200;
    private static final int MY_REQ_RESET_CODE = 0x211;

    /*
     * 消息类别
     */
    private static final int MSG_CREATE_TASK = 0x01;
    private static final int MSG_CHECK_TASK_STATUS = 0x02;
    private static final int MSG_CHECK_TASK_STATUS_FEEDBACK = 0x22;
    private static final int MSG_TASK_SUSPENDED = 0x03;
    private static final int MSG_TASK_ABORT = 0x44;
    private static final int MSG_TASK_DONE = 0x05;

    /*
     * Suspended Task Type
     */
    private static final int SUSPENDED_TYPE_NULL = 0x00;
    private static final int SUSPENDED_TYPE_NEED_SMSCODE = 0x01;

    private Context mContext = this;

    private View mSuspendedView = null;
    private View mProcessView = null;

    private TaobaoAppAuthTaskStatusResponse<TaobaoDetail> mCurrentTaskStatusResp = null;
    private int mSuspendedType = SUSPENDED_TYPE_NULL;
    private Transport mTransport = null;
    private boolean waitingForAppAuth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taobao_app_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

        mProcessView = (View) findViewById(R.id.progress_view);
        mSuspendedView = (View) findViewById(R.id.suspended_view);

        mTransport = new TaobaoAppAuthActivity.Transport();

        Button button = (Button) findViewById(R.id.action_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTransport.createTask();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        AuthInfo authInfo = Storage.getInstance().getAuthInfo(mContext);
        if (null == authInfo || null == authInfo.getAppId()) {
            Intent intent = new Intent(TaobaoAppAuthActivity.this, AuthInfoActivity.class);
            startActivityForResult(intent, MY_REQ_CONFIG_CODE);
        }
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
            Intent intent = new Intent(TaobaoAppAuthActivity.this, AuthInfoActivity.class);
            startActivityForResult(intent, MY_REQ_RESET_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (MY_REQ_CONFIG_CODE != requestCode) return;

        if (Activity.RESULT_CANCELED == resultCode) {
            finish();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 隐藏软键盘
     *
     * @param activeView 当前有焦点的View
     * @return 如果键盘被成功隐藏则返回true, 否则返回false
     */
    private boolean hideSoftKeyboard(View activeView) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm && null != activeView) {
            imm.hideSoftInputFromWindow(activeView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        }

        return false;
    }

    /**
     * 隐藏对应的Views
     *
     * @param views 需要被隐藏的View列表
     */
    private void hideViews(View... views) {
        for (View view : views) {
            if (null == view) continue;

            if (View.VISIBLE == view.getVisibility()) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 显示View
     *
     * @param view 需要被显示的View
     */
    private void showView(View view) {
        if (null != view) {
            if (View.VISIBLE != view.getVisibility()) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 显示‘任务进行中’的UI
     *
     * @param msg 显示的提示信息
     */
    private void showOngoingMessage(String msg) {
        hideViews(mSuspendedView);

        TextView textView = (TextView) mProcessView.findViewById(R.id.show_message);
        textView.setText(msg);
        showView(mProcessView);

        ProgressBar progressBar = (ProgressBar) mProcessView.findViewById(R.id.progressbar);
        showView(progressBar);

        Button button = (Button) mProcessView.findViewById(R.id.action_button);
        button.setEnabled(false);
    }

    private void showTaobaoResult() {
        hideViews(mSuspendedView, mProcessView);

        View resultView = findViewById(R.id.show_result_detail);

        TaobaoDetail detail = mCurrentTaskStatusResp.getResult();
        if (detail != null) {
            // account name
            TextView v = resultView.findViewById(R.id.taobao_accountname);
            v.setText("账号: " + detail.getUserinfo().getAccountName());

            // taobao user id
            v = resultView.findViewById(R.id.taobao_userid);
            v.setText("taobao ID:" + detail.getUserinfo().getUserId());

            // delivery address list count
            v = resultView.findViewById(R.id.taobao_delivery_addresses_count);
            v.setText("收货地址条目: " +
                    (detail.getUserinfo().getDeliveryAddresses() != null ?
                            detail.getUserinfo().getDeliveryAddresses().size()
                            : 0));

            // taobao history count
            int totalCount = 0;
            TreeMap<String, Integer> historyMap = new TreeMap<>();
            for (TaobaoDetail.PurchaseHistory perm : detail.getTaobaoHistory()) {
                Integer perc = 0;
                if (null != perm.getData()) perc = Integer.valueOf(perm.getData().size());

                totalCount += perc;

                historyMap.put(perm.getMonth(), perc);
            }

            StringBuilder stringBuilder = new StringBuilder("在");
            for (Iterator<String> iterator = historyMap.keySet().iterator(); iterator.hasNext();) {
                stringBuilder.append(iterator.next());
                stringBuilder.append(",");
            }
            stringBuilder.append("月中，共有").append(totalCount).append("个订单被抓取到。");
            v = resultView.findViewById(R.id.taobao_history_count);
            v.setText(stringBuilder.toString());


            Button button = resultView.findViewById(R.id.confirm_btn);
            button.setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when a view has been clicked.
                 *
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        showView(resultView);

    }

    /**
     * 显示任务错误信息
     *
     * @param msg 需要显示的内容
     */
    private void showAbortMessage(String msg) {
        ProgressBar progressBar = (ProgressBar) mProcessView.findViewById(R.id.progressbar);
        hideViews(mSuspendedView, progressBar);

        TextView textView = (TextView) mProcessView.findViewById(R.id.show_message);
        textView.setText(msg);
        showView(mProcessView);

        Button button = (Button) mProcessView.findViewById(R.id.action_button);
        button.setEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * 任务暂停，需要用户提交进一步的信息，比如短信验证码，正确的密码，等等
     * 此方法中对输入内容的有效性进行检查，如果数据有效，则继续当前暂停的任务
     *
     * @param input1    输入数据域1
     * @param input2    输入数据域2
     *
     * @return 如果数据没有通过有效性检查，则返回true，否则返回false
     */
    private boolean attemptResumeTask(EditText input1, EditText input2) {

        boolean cancel = false;
        View focusView = null;

        String primaryInput = input1.getText().toString();
        String secondaryInput = input2.getText().toString();

        if (TextUtils.isEmpty(primaryInput) || TextUtils.isEmpty(primaryInput.trim())) {
            cancel = true;
            focusView = input1;
            input1.setError(getString(R.string.error_no_input));
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            hideViews(mSuspendedView);
            showView(mProcessView);

            if (input1.isFocused()) {
                hideSoftKeyboard(input1);
            } else {
                hideSoftKeyboard(input2);
            }

            // 继续当前挂起的任务
            mTransport.resumeTask(mSuspendedType, primaryInput, secondaryInput);
        }

        return !cancel;
    }

    /**
     * 显示任务挂起的详细信息，并提供输入域供用户输入进一步的信息，以便继续当前的任务
     *
     * @param response 任务挂起的详细原因
     */
    private void showSuspendedUI(TaskStatusResponse<TaobaoDetail> response) {
        if (null == response) {
            Log.e(TAG, "response is not passed in, we cannot process the suspended case.");
            return;
        }

        hideViews(mProcessView);

        final EditText input1 = (EditText) mSuspendedView.findViewById(R.id.suspended_input1);
        final EditText input2 = (EditText) mSuspendedView.findViewById(R.id.suspended_input2);
        TextView textView = (TextView) mSuspendedView.findViewById(R.id.suspended_message);
        int mSuspendedType = SUSPENDED_TYPE_NULL;

        TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    attemptResumeTask(input1, input2);
                    return true;
                }

                return false;
            }
        };

        Button button = (Button) mSuspendedView.findViewById(R.id.action_go);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                attemptResumeTask(input1, input2);
            }
        });

        // clear previous content if present
        input1.setText(null);
        input2.setText(null);

        hideViews(input2);
        textView.setText(response.getReason());

        // 对于各个数据项的介绍，请参照对应的API文档
        switch (response.getNeed()) {
            case "SMS":
            case "SMSJilinTelecom":
            case "SMSAgain":
            case "newSMS":
            case "loginSMS":
            case "loginSMSAgain":
            case "newLoginSMS":
                input1.setHint(R.string.hint_input_sms_code);
                mSuspendedType = SUSPENDED_TYPE_NEED_SMSCODE;
                break;

            default:
                Log.e(TAG, "unsupported need type - " + response.getNeed());
                //TODO: add more error handling logic
                break;

        }

        showView(mSuspendedView);
    }

    // 为了便于更新UI，此处创建一个Message Handler,并使用当前的UI线程的Looper
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
                    showOngoingMessage(getString(R.string.appauth_get_auth_url));
                    mTransport.createTask();
                    break;

                case MSG_CHECK_TASK_STATUS:
                    mTransport.checkTaskStatus();
                    break;

                case MSG_CHECK_TASK_STATUS_FEEDBACK:
                    mCurrentTaskStatusResp = (TaobaoAppAuthTaskStatusResponse<TaobaoDetail>) msg.obj;

                    if (null == mCurrentTaskStatusResp) break;

                    if (mCurrentTaskStatusResp.isProcessing()) {
                        if (mCurrentTaskStatusResp.isLogin()) {
                            showOngoingMessage(getString(R.string.progress_status_login));
                        } else if (mCurrentTaskStatusResp.isFetching()) {
                            showOngoingMessage(getString(R.string.progress_status_fetching));
                        }

                        mHandler.sendEmptyMessageDelayed(MSG_CHECK_TASK_STATUS, 5000L);
                    }
                    else if (mCurrentTaskStatusResp.isFailed()) {
                        String errorInfo = "task " + mCurrentTaskStatusResp.getStatus() + ", "
                                + "reason:" + mCurrentTaskStatusResp.getReason() + ", "
                                + "failedCode:" + mCurrentTaskStatusResp.getFailCode();

                        mHandler.sendMessage(mHandler.obtainMessage(MSG_TASK_ABORT, errorInfo));
                    }
                    else if (mCurrentTaskStatusResp.isSuspended()) {
                        mHandler.sendMessage(mHandler.obtainMessage(MSG_TASK_SUSPENDED, mCurrentTaskStatusResp));
                    }
                    else if (mCurrentTaskStatusResp.isDone()) {
                        String hintMessage = "task done";
                        mHandler.sendMessage(mHandler.obtainMessage(MSG_TASK_DONE, mCurrentTaskStatusResp));
                    }

                    break;

                case MSG_TASK_SUSPENDED:
                    mCurrentTaskStatusResp = (TaobaoAppAuthTaskStatusResponse<TaobaoDetail>) msg.obj;

                    if (null == mCurrentTaskStatusResp) break;
                    else if ("waitForTaobaoAppAuth".equals(mCurrentTaskStatusResp.getNeed())) {
                        if (waitingForAppAuth) {
                            mHandler.sendEmptyMessageDelayed(MSG_CHECK_TASK_STATUS, 5000L);
                            break;
                        }

                        if (!launchTaobao(mContext, mCurrentTaskStatusResp.getQR().getUrl())) {
                            AlertDialog alertDialog = new AlertDialog.Builder(TaobaoAppAuthActivity.this)
                                    .setTitle("唤起淘宝App失败")
                                    .setMessage("您手机可能未安装淘宝App，请安装后重试！")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    }).create();

                            alertDialog.show();
                        } else {
                            waitingForAppAuth = true;
                            Toast.makeText(mContext, getString(R.string.launch_alipay_app), Toast.LENGTH_SHORT).show();
                            mHandler.sendEmptyMessageDelayed(MSG_CHECK_TASK_STATUS, 5000L);
                        }

                    }
                    else showSuspendedUI(mCurrentTaskStatusResp);

                    break;

                case MSG_TASK_ABORT:
                    Log.i(TAG, "task terminated, please check out the reason in detail");
                    String abortMsg = (String) msg.obj;
                    showAbortMessage(abortMsg);
                    break;

                case MSG_TASK_DONE:
                    mCurrentTaskStatusResp = (TaobaoAppAuthTaskStatusResponse<TaobaoDetail>) msg.obj;
                    Log.i(TAG, "task succeeded, and we get the final result, just show it now.");
                    showTaobaoResult();
                    break;

            }
            super.handleMessage(msg);

        }
    };


    /**
     * 执行具体的网络请求的类
     */
    private class Transport {

        private Transporter transporter = null;
        private String tid;

        public Transport() {
            transporter = new Transporter(mContext, genericCallback, genericCallback, checkTaskStatusCallback);
        }

        public void createTask() {
            TaobaoAppAuth taobaoAppAuth = new TaobaoAppAuth();
            Log.i(TAG, taobaoAppAuth.toString());

            String jsonBody = new Gson().toJson(taobaoAppAuth);
            RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonBody);

            transporter.createTask(body);

        }

        public void checkTaskStatus() {
            transporter.checkTaskStatus(tid);
        }

        public void resumeTask(int suspendedType, String primaryInput, String secondaryInput) {
            String jsonBody = null;
            switch (suspendedType) {
                case SUSPENDED_TYPE_NEED_SMSCODE:
                    jsonBody = new Gson().toJson(new SmsCodeItem(tid, primaryInput));
                    break;

                default:
                    Log.e(TAG, "unsupported suspended type - " + suspendedType);
                    //TODO: add more error handling logic
                    return;

            }

            transporter.resumeTask(RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonBody));

        }

        private final Callback genericCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "[-] carrier failure - " + e.getMessage());
                Message abortMsg = mHandler.obtainMessage(MSG_TASK_ABORT, e.getMessage());
                mHandler.sendMessage(abortMsg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    TaskCreationResponse result = new Gson().fromJson(response.body().charStream(), TaskCreationResponse.class);
                    tid = result.getTid();

                    Log.i(TAG, "[-] appauth succeeded - " + result.toString());

                    mHandler.sendEmptyMessage(MSG_CHECK_TASK_STATUS);
                } else {
                    String errorMsg = response.body().string();
                    Log.e(TAG, "[-] appauth failure - " + errorMsg);
                    Message abortMsg = mHandler.obtainMessage(MSG_TASK_ABORT, errorMsg);
                    mHandler.sendMessage(abortMsg);
                }
            }
        };

        private final Callback checkTaskStatusCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "[-] failure - " + e.getMessage());
                Message abortMsg = mHandler.obtainMessage(MSG_TASK_ABORT, e.getMessage());
                mHandler.sendMessage(abortMsg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    Type targetClz = new TypeToken<TaobaoAppAuthTaskStatusResponse<TaobaoDetail>>(){}.getType();
                    TaobaoAppAuthTaskStatusResponse<TaobaoDetail> result = new Gson().fromJson(response.body().charStream(), targetClz);
                    Log.i(TAG, "[-] succeeded - " + result.toString());

                    Message msg = mHandler.obtainMessage(MSG_CHECK_TASK_STATUS_FEEDBACK);
                    msg.obj = result;

                    mHandler.sendMessage(msg);
                } else {
                    String errorMsg = response.body().string();
                    Log.e(TAG, "[-] failure - " + errorMsg);
                    Message abortMsg = mHandler.obtainMessage(MSG_TASK_ABORT, errorMsg);
                    mHandler.sendMessage(abortMsg);
                }
            }
        };

    }

    private class TaobaoAppAuth {
        private String type = null;
        private String subType = null;
        private String callback = null;

        public TaobaoAppAuth() {
            type = "taobao";
            subType = "appauth";
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSubType() {
            return subType;
        }

        public void setSubType(String subType) {
            this.subType = subType;
        }

        public String getCallback() {
            return callback;
        }

        public void setCallback(String callback) {
            this.callback = callback;
        }

        @Override
        public String toString() {
            return "TaobaoAppAuth{" +
                    "type='" + type + '\'' +
                    ", subType='" + subType + '\'' +
                    ", callback='" + callback + '\'' +
                    '}';
        }
    }

    /**
     * 唤起淘宝app
     *
     * @param context Context 实例
     * @param taobaoUrl 唤起淘宝app的URL
     *
     */
    private boolean launchTaobao(Context context, String taobaoUrl) {
        Intent intent = null;
        try {
            intent = Intent.parseUri(taobaoUrl, Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);

            context.startActivity(intent);
            return true;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        //TODO: 没有安装淘宝客户端, 需要客户端根据自己的实际业务逻辑处理此种情况

        return false;
    }
}
