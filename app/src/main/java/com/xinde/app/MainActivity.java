package com.xinde.app;

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
import com.xinde.storage.item.CarrierInfo;
import com.xinde.util.Validator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Context mContext = null;
    private EditText mUserNameView = null;
    private EditText mPasswordView = null;
    private EditText mUserIdView = null;
    private EditText mPhoneNoView = null;
    private EditText mCallbackURLView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        buildWidgets();
        populateCarrierInfoIfNeeded();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

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
            Intent intent = new Intent(MainActivity.this, AuthInfoActivity.class);
            startActivityForResult(intent, 100);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // just ignore result from AuthInfoActivity
    }

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

        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_invalid_username));
            cancel = true;
            focusView = mUserNameView;
        }
        else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            cancel = true;
            focusView = mPasswordView;
        }
        else if (TextUtils.isEmpty(phoneNo) || !Validator.isMobile(phoneNo)) {
            mPhoneNoView.setError(getString(R.string.error_invalid_phoneno));
            cancel = true;
            focusView = mPhoneNoView;
        }
        else if (TextUtils.isEmpty(userID) || !Validator.isIDCard(userID)) {
            mUserIdView.setError(getString(R.string.error_invalid_userid));
            cancel = true;
            focusView = mUserIdView;
        }
        else if (!TextUtils.isEmpty(callback) && !Validator.isUrl(callback)) {
            mCallbackURLView.setError(getString(R.string.error_invalid_callback));
            cancel = true;
            focusView = mCallbackURLView;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            CarrierInfo carrierInfo = new CarrierInfo(
                    userName,
                    phoneNo,
                    password,
                    userID,
                    callback
            );

            Log.i(TAG, "start to save carrier info - " + carrierInfo);
            Storage.getInstance().saveCarrierInfo(mContext, carrierInfo);

            Intent intent = new Intent(MainActivity.this, TaskActivity.class);
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
