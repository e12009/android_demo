package com.xinde.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ActionListActivity extends AppCompatActivity {
    private static final String TAG = "ActionListActivity";

    private Context mContext = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;


        Button button = (Button) findViewById(R.id.demo_carrier);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Log.i(TAG, "start to demonstrate xinde data carrier.");

                mContext.startActivity(new Intent(ActionListActivity.this, CarrierMainActivity.class));

            }
        });

        button = (Button) findViewById(R.id.demo_zhimafen_api);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Log.i(TAG, "start to demonstrate xinde data zhimafen with direct API mode.");

                mContext.startActivity(new Intent(ActionListActivity.this, CarrierMainActivity.class));

            }
        });
    }

}
