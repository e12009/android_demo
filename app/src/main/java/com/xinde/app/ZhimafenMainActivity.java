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
import com.xinde.storage.item.ZhimafenInfo;
import com.xinde.util.FormatValidator;

public class ZhimafenMainActivity extends AppCompatActivity {
    private static final String TAG = "ZhimafenMainActivity";

    private static final int MY_REQ_CONFIG_CODE = 0x100;
    private static final int MY_REQ_RESET_CODE = 0x111;

    private Context mContext = null;
    private EditText mUserNameView = null;
    private EditText mUserIdView = null;
    private EditText mPhoneNoView = null;
    private EditText mCallbackURLView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhimafen);

        mContext = this;

        buildWidgets();
        populateZhimafenInfoIfNeeded();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AuthInfo authInfo = Storage.getInstance().getAuthInfo(mContext);
        if (null == authInfo || null == authInfo.getAppId()) {
            Intent intent = new Intent(ZhimafenMainActivity.this, AuthInfoActivity.class);
            startActivityForResult(intent, MY_REQ_CONFIG_CODE);
        }
    }

    private void buildWidgets() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUserNameView = (EditText) findViewById(R.id.user_name);
        mPhoneNoView = (EditText) findViewById(R.id.phone_no);
        mUserIdView = (EditText) findViewById(R.id.user_id);
        mCallbackURLView = (EditText) findViewById(R.id.callback_url);

        mUserNameView.setOnEditorActionListener(onEditorActionListener);
        mPhoneNoView.setOnEditorActionListener(onEditorActionListener);
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
            Intent intent = new Intent(ZhimafenMainActivity.this, AuthInfoActivity.class);
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

    private void populateZhimafenInfoIfNeeded() {
        ZhimafenInfo zhimafenInfo = Storage.getInstance().getZhimafenInfo(mContext);
        if (null != zhimafenInfo) {
            mUserNameView.setText(zhimafenInfo.getUserName());
            mUserIdView.setText(zhimafenInfo.getUserID());
            mPhoneNoView.setText(zhimafenInfo.getPhoneNo());
            mCallbackURLView.setText(zhimafenInfo.getCallback());
        }
    }

    private void attemptCreateTask() {
        mUserNameView.setError(null);
        mUserIdView.setError(null);
        mCallbackURLView.setError(null);

        boolean cancel = false;
        View focusView = null;

        String userName = mUserNameView.getText().toString();
        String phoneNo = mPhoneNoView.getText().toString();
        String userID = mUserIdView.getText().toString();
        String callback = mCallbackURLView.getText().toString();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userName.trim())) {
            mUserNameView.setError(getString(R.string.error_invalid_username));
            cancel = true;
            focusView = mUserNameView;
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
            ZhimafenInfo zhimafenInfo = new ZhimafenInfo(userName, phoneNo, userID, callback);

            Log.i(TAG, "start to save zhimafen info - " + zhimafenInfo);
            Storage.getInstance().saveZhimafenInfo(mContext, zhimafenInfo);

            Intent intent = new Intent(ZhimafenMainActivity.this, ZhimafenTaskActivity.class);
            mContext.startActivity(intent);

        }
    }


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
