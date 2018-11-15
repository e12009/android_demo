package com.xinde.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.xinde.storage.Storage;
import com.xinde.storage.item.AuthInfo;

public class AuthInfoActivity extends AppCompatActivity {
    private static final String TAG = "AuthInfoActivity";

    private Context mContext = null;
    private EditText mAppIdView = null;
    private EditText mAppSecretView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_info);

        mContext = this;

        mAppIdView = (EditText) findViewById(R.id.appid);
        mAppIdView.setOnEditorActionListener(onEditorActionListener);

        mAppSecretView = (EditText) findViewById(R.id.appsecret);
        mAppSecretView.setOnEditorActionListener(onEditorActionListener);

        Button button = (Button) findViewById(R.id.btn_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSaveAuthInfo();
            }
        });

        populateAuthInfoIfNeeded();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);

        super.onBackPressed();
    }

    private void populateAuthInfoIfNeeded() {
        AuthInfo authInfo = Storage.getInstance().getAuthInfo(mContext);
        if (null != authInfo) {
            mAppIdView.setText(authInfo.getAppId());
            mAppSecretView.setText(authInfo.getAppSecret());
        }
    }

    private void attemptSaveAuthInfo() {
       mAppIdView.setError(null);
       mAppSecretView.setError(null);

       boolean cancel = false;
       View focusView = null;

       String appId = mAppIdView.getText().toString();
       String appSecret = mAppSecretView.getText().toString();

       if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appId.trim())) {
           mAppIdView.setError(getString(R.string.error_invalid_appid));
           cancel = true;
           focusView = mAppIdView;
       }
       else if (TextUtils.isEmpty(appSecret) || TextUtils.isEmpty(appSecret.trim())) {
           mAppSecretView.setError(getString(R.string.error_invalid_appsecret));
           cancel = true;
           focusView = mAppSecretView;
       }

       if (cancel) {
           focusView.requestFocus();
       } else {
           AuthInfo authInfo = new AuthInfo(appId, appSecret);
           Log.i(TAG, "start to save auth info - " + authInfo);
           Storage.getInstance().saveAuthInfo(mContext, authInfo);

           setResult(Activity.RESULT_OK, null);
           finish();
       }
    }

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                attemptSaveAuthInfo();
                return true;
            }
            return false;
        }
    };
}
