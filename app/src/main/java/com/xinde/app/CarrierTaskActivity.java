package com.xinde.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xinde.adapter.CarrierAdapter;
import com.xinde.network.Transporter;
import com.xinde.reponse.TaskCreationResponse;
import com.xinde.reponse.TaskStatusResponse;
import com.xinde.reponse.taskresult.*;
import com.xinde.resume.PasswordItem;
import com.xinde.resume.SmsCodeItem;
import com.xinde.resume.UserNameAndIDItem;
import com.xinde.storage.Storage;
import com.xinde.storage.item.CarrierInfo;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * 执行运营商业务并显示相应的结果
 */
public class CarrierTaskActivity extends AppCompatActivity {
    private static final String TAG = "CarrierTaskActivity";

    private static final int MY_REQ_CODE = 0x200;

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
    private static final int SUSPENDED_TYPE_NEED_PASSWORD = 0x01;
    private static final int SUSPENDED_TYPE_NEED_SMSCODE = 0x02;
    private static final int SUSPENDED_TYPE_NEED_AUTHINFO = 0x03;

    private Context mContext = null;
    private Transport mTransport = null;

    // widgets
    private View mProcessView = null;
    private View mSuspendedView = null;
    private View mFailureView = null;
    private RecyclerView mSuccessView = null;
    private CarrierAdapter mCarrierAdapter = null;

    // 查询Task状态时返回的数据类型
    private TaskStatusResponse<CarrierResult> mCurrentTaskStatusResp = null;

    private int mSuspendedType = SUSPENDED_TYPE_NULL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_carrier);

        mContext = this;
        mProcessView = findViewById(R.id.progress_view);
        mSuspendedView = findViewById(R.id.suspended_view);
        mFailureView = findViewById(R.id.suspended_view);
        mSuccessView = (RecyclerView) findViewById(R.id.show_result_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mSuccessView.setLayoutManager(layoutManager);

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
            Intent intent = new Intent(CarrierTaskActivity.this, AuthInfoActivity.class);
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
        hideViews(mFailureView, mSuspendedView, mSuccessView);

        TextView textView = (TextView) mProcessView.findViewById(R.id.progress_message);
        textView.setText(msg);
        showView(mProcessView);

        ProgressBar progressBar = (ProgressBar) mProcessView.findViewById(R.id.progressbar);
        showView(progressBar);
    }

    /**
     * 显示任务错误信息
     *
     * @param msg 需要显示的内容
     */
    private void showAbortMessage(String msg) {
        ProgressBar progressBar = (ProgressBar) mProcessView.findViewById(R.id.progressbar);
        hideViews(mFailureView, mSuspendedView, mSuccessView, progressBar);

        TextView textView = (TextView) mProcessView.findViewById(R.id.progress_message);
        textView.setText(msg);
        showView(mProcessView);

    }

    /**
     * 显示查询的结果
     */
    private void showTaskResult() {
        hideViews(mFailureView, mSuspendedView, mProcessView);
        showView(mSuccessView);

        if (null == mCurrentTaskStatusResp) {
            Log.e(TAG, "task succeeded, but we do not find out result data.");
        }
        else {
            Log.i(TAG, "task succeeded, start to compose the UI data");

            List<CarrierAdapter.DataItem> itemList = new ArrayList<>();

            CarrierResult result = (CarrierResult) mCurrentTaskStatusResp.getResult();
            if (null == result) {
                Log.e(TAG, "task succeeded, not carrier result field holds null");
            }
            else {
                if (null == result.getCallHistory()) {
                    CarrierAdapter.DataItem dataItem = new CarrierAdapter.DataItem(
                            "六个月",
                            "通话记录",
                            0
                    );

                    itemList.add(dataItem);
                }
                else {
                    for (CallHistory callHistory : result.getCallHistory()) {
                        int count = callHistory.getDetails() != null ? callHistory.getDetails().size() : 0;
                        CarrierAdapter.DataItem dataItem = new CarrierAdapter.DataItem(
                                callHistory.getMonth(),
                                "通话记录",
                                count
                        );

                        itemList.add(dataItem);
                    }
                }

                if (null == result.getSmsHistory()) {
                    CarrierAdapter.DataItem dataItem = new CarrierAdapter.DataItem(
                            "六个月",
                            "短信记录",
                            0
                    );

                    itemList.add(dataItem);

                }
                else {
                    for (SmsHistory smsHistory : result.getSmsHistory()) {
                        int count = smsHistory.getDetails() != null ? smsHistory.getDetails().size() : 0;
                        CarrierAdapter.DataItem dataItem = new CarrierAdapter.DataItem(
                                smsHistory.getMonth(),
                                "短信记录",
                                count
                        );

                        itemList.add(dataItem);
                    }
                }

                if (null == result.getBillHistory()) {
                    CarrierAdapter.DataItem dataItem = new CarrierAdapter.DataItem(
                            "六个月",
                            "月账单",
                            0
                    );

                    itemList.add(dataItem);

                }
                else {
                    for (BillHistory billHistory : result.getBillHistory()) {
                        int count = billHistory.getDetails() != null ? billHistory.getDetails().size() : 0;
                        CarrierAdapter.DataItem dataItem = new CarrierAdapter.DataItem(
                                billHistory.getMonth(),
                                "月账单",
                                count
                        );

                        itemList.add(dataItem);
                    }
                }

                if (null == result.getNetFlowHistory()) {
                    CarrierAdapter.DataItem dataItem = new CarrierAdapter.DataItem(
                            "六个月",
                            "网络流量",
                            0
                    );

                    itemList.add(dataItem);

                }
                else {
                    for (NetFlowHistory netFlowHistory : result.getNetFlowHistory()) {
                        int count = netFlowHistory.getDetails() != null ? netFlowHistory.getDetails().size() : 0;
                        CarrierAdapter.DataItem dataItem = new CarrierAdapter.DataItem(
                                netFlowHistory.getMonth(),
                                "网络流量",
                                count
                        );

                        itemList.add(dataItem);
                    }
                }

                if (null == result.getWebsiteHistory()) {
                    CarrierAdapter.DataItem dataItem = new CarrierAdapter.DataItem(
                            "六个月",
                            "上网记录",
                            0
                    );

                    itemList.add(dataItem);
                }
                else {
                    for (WebsiteHistory websiteHistory : result.getWebsiteHistory()) {
                        int count = websiteHistory.getDetails() != null ? websiteHistory.getDetails().size() : 0;
                        CarrierAdapter.DataItem dataItem = new CarrierAdapter.DataItem(
                                websiteHistory.getMonth(),
                                "上网记录",
                                count
                        );

                        itemList.add(dataItem);
                    }
                }

                if (null == mCarrierAdapter) {
                    mCarrierAdapter = new CarrierAdapter(mContext, itemList);
                    mSuccessView.setAdapter(mCarrierAdapter);
                } else {
                    mCarrierAdapter.setDataItems(itemList);
                    mCarrierAdapter.notifyDataSetChanged();
                }
            }
        }


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
        else if (SUSPENDED_TYPE_NEED_AUTHINFO == mSuspendedType) {
            cancel = true;
            focusView = input2;
            input2.setError(getString(R.string.error_no_input));
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            hideViews(mFailureView, mSuccessView, mSuspendedView);
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
    private void showSuspendedUI(TaskStatusResponse<CarrierResult> response) {
        if (null == response) {
            Log.e(TAG, "response is not passed in, we cannot process the suspended case.");
            return;
        }

        hideViews(mProcessView, mFailureView, mSuccessView);

        final EditText input1 = (EditText) mSuspendedView.findViewById(R.id.suspended_input1);
        final EditText input2 = (EditText) mSuspendedView.findViewById(R.id.suspended_input2);
        TextView textView = (TextView) mSuspendedView.findViewById(R.id.suspended_message);
        mSuspendedType = SUSPENDED_TYPE_NULL;

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

            case "password":
            case "passwordInOne":
            case "passwordInTwo":
            case "passwordInThree":
            case "passwordInFour":
            case "passwordInFive":
            case "passwordTooSimple":
                input1.setHint(R.string.short_password);
                mSuspendedType = SUSPENDED_TYPE_NEED_PASSWORD;
                break;

            case "nameAndID":
                showView(input2);
                input1.setHint(R.string.carrier_user_name);
                input2.setHint(R.string.carrier_user_id);
                mSuspendedType = SUSPENDED_TYPE_NEED_AUTHINFO;
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
                    showOngoingMessage(getString(R.string.progress_status_login));
                    mTransport.createTask();
                    break;

                case MSG_CHECK_TASK_STATUS:
                    mTransport.checkTaskStatus();;
                    break;

                case MSG_CHECK_TASK_STATUS_FEEDBACK:
                    mCurrentTaskStatusResp = (TaskStatusResponse<CarrierResult>) msg.obj;

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
                    mCurrentTaskStatusResp = (TaskStatusResponse<CarrierResult>) msg.obj;

                    if (null == mCurrentTaskStatusResp) break;
                    else showSuspendedUI(mCurrentTaskStatusResp);

                    break;

                case MSG_TASK_ABORT:
                    Log.i(TAG, "task terminated, please check out the reason in detail");
                    String abortMsg = (String) msg.obj;
                    showAbortMessage(abortMsg);
                    break;

                case MSG_TASK_DONE:
                    mCurrentTaskStatusResp = (TaskStatusResponse<CarrierResult>) msg.obj;

                    Log.i(TAG, "task succeeded, and we get the final result, just show it now.");
                    showTaskResult();
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
            CarrierInfo carrierInfo = Storage.getInstance().getCarrierInfo(mContext);

            Log.i(TAG, carrierInfo.toString());

            String jsonBody = new Gson().toJson(carrierInfo);
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

                case SUSPENDED_TYPE_NEED_PASSWORD:
                    jsonBody = new Gson().toJson(new PasswordItem(tid, primaryInput));
                    break;

                case SUSPENDED_TYPE_NEED_AUTHINFO:
                    jsonBody = new Gson().toJson(new UserNameAndIDItem(tid, primaryInput, secondaryInput));
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

                    Log.i(TAG, "[-] carrier succeeded - " + result.toString());

                    mHandler.sendEmptyMessage(MSG_CHECK_TASK_STATUS);
                } else {
                    String errorMsg = response.body().string();
                    Log.e(TAG, "[-] carrier failure - " + errorMsg);
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

                    Type targetClz = new TypeToken<TaskStatusResponse<CarrierResult>>(){}.getType();
                    TaskStatusResponse<CarrierResult> result = new Gson().fromJson(response.body().charStream(), targetClz);
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
}
