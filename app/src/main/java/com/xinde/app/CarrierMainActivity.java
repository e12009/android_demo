package com.xinde.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.xinde.storage.Storage;
import com.xinde.storage.item.AuthInfo;
import com.xinde.storage.item.CarrierInfo;
import com.xinde.util.FormatValidator;

/**
 * 编辑并保存运营商业务参数
 */
public class CarrierMainActivity extends AppCompatActivity {
    private static final String TAG = "CarrierMainActivity";

    private static final int MY_REQ_CONFIG_CODE = 0x100;
    private static final int MY_REQ_RESET_CODE = 0x111;

    private Context mContext = null;
    private EditText mUserNameView = null;
    private EditText mPasswordView = null;
    private EditText mUserIdView = null;
    private EditText mPhoneNoView = null;
    private EditText mCallbackURLView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_carrier);

        mContext = this;

        buildWidgets();
        populateCarrierInfoIfNeeded();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 如果此时appId & appSecret为空，则拉起AuthInfoActivity进行信息的输入
        AuthInfo authInfo = Storage.getInstance().getAuthInfo(mContext);
        if (null == authInfo || null == authInfo.getAppId()) {
            Intent intent = new Intent(CarrierMainActivity.this, AuthInfoActivity.class);
            startActivityForResult(intent, MY_REQ_CONFIG_CODE);
        }
    }

    /**
     * 设置toolbar并设置其他Widgets
     */
    private void buildWidgets() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUserNameView = (EditText) findViewById(R.id.user_name);
        mPhoneNoView = (EditText) findViewById(R.id.phone_no);
        mPasswordView = (EditText) findViewById(R.id.password);
        mUserIdView = (EditText) findViewById(R.id.user_id);
        mCallbackURLView = (EditText) findViewById(R.id.callback_url);

        mUserNameView.setOnEditorActionListener(onEditorActionListener);
        mPhoneNoView.setOnEditorActionListener(onEditorActionListener);
        mPasswordView.setOnEditorActionListener(onEditorActionListener);
        mUserIdView.setOnEditorActionListener(onEditorActionListener);
        mCallbackURLView.setOnEditorActionListener(onEditorActionListener);

        Button button = (Button) findViewById(R.id.action_go);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                attemptCreateTask();
            }
        });
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
            Intent intent = new Intent(CarrierMainActivity.this, AuthInfoActivity.class);
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
     *  显示保存过的运营商认证参数信息
     */
    private void populateCarrierInfoIfNeeded() {
        CarrierInfo carrierInfo = Storage.getInstance().getCarrierInfo(mContext);
        if (null != carrierInfo) {
            mUserNameView.setText(carrierInfo.getUserName());
            mPasswordView.setText(carrierInfo.getPassword());
            mUserIdView.setText(carrierInfo.getUserID());
            mPhoneNoView.setText(carrierInfo.getPhoneNo());
            mCallbackURLView.setText(carrierInfo.getCallback());
        }
    }

    /**
     * 检查输入参数的有效性，如果通过参数检查，则保存当前的认证参数，
     * 同时，拉起CarrierTaskActivity来执行业务查询任务
     */
    private void attemptCreateTask() {
        mUserNameView.setError(null);
        mPasswordView.setError(null);
        mPasswordView.setError(null);
        mUserIdView.setError(null);
        mCallbackURLView.setError(null);

        boolean cancel = false;
        View focusView = null;

        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String phoneNo = mPhoneNoView.getText().toString();
        String userID = mUserIdView.getText().toString();
        String callback = mCallbackURLView.getText().toString();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userName.trim())) {
            mUserNameView.setError(getString(R.string.error_invalid_username));
            cancel = true;
            focusView = mUserNameView;
        }
        else if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password.trim())) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            cancel = true;
            focusView = mPasswordView;
        }
        else if (TextUtils.isEmpty(phoneNo) || !FormatValidator.isMobile(phoneNo)) {
            mPhoneNoView.setError(getString(R.string.error_invalid_phoneno));
            cancel = true;
            focusView = mPhoneNoView;
        }
        else if (TextUtils.isEmpty(userID) || !FormatValidator.isIDCard(userID)) {
            mUserIdView.setError(getString(R.string.error_invalid_userid));
            cancel = true;
            focusView = mUserIdView;
        }
        else if (!TextUtils.isEmpty(callback) && !FormatValidator.isUrl(callback)) {
            mCallbackURLView.setError(getString(R.string.error_invalid_callback));
            cancel = true;
            focusView = mCallbackURLView;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // 保存现有数据
            CarrierInfo carrierInfo = new CarrierInfo(
                    userName,
                    phoneNo,
                    password,
                    userID,
                    callback
            );

            Log.i(TAG, "start to save carrier info - " + carrierInfo);
            Storage.getInstance().saveCarrierInfo(mContext, carrierInfo);

            // 拉起CarrierTaskActivity执行业务查询任务
            Intent intent = new Intent(CarrierMainActivity.this, CarrierTaskActivity.class);
            mContext.startActivity(intent);

        }
    }


    // 处理键盘事件
    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                attemptCreateTask();
                return true;
            }
            return false;
        }
    };

}
