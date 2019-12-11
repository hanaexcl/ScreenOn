package com.sobored.ScreenOn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.os.Bundle;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {
    SharedPreferences mSharedPreferences = null;
    Intent intent = new Intent();
    private  myReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Switch sw1 = findViewById(R.id.switch1);
        if (getSetting() == 1) {
            sw1.setChecked(true);
        }

        intent.setClass(this, KeepScreenOnService.class);

        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    setSetting(1);
                    startService(intent);
                } else {
                    setSetting(0);
                    startService(intent);
                }
            }
        });

        receiver = new myReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.sobored.screenOn.intent");
        registerReceiver(receiver, filter);
    }

    private int getSetting() {
        if (mSharedPreferences == null) mSharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        return mSharedPreferences.getInt("Screen", 0);
    }

    private void setSetting(int sw) {
        if (mSharedPreferences == null) mSharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        mSharedPreferences.edit()
                .putInt("Screen", sw)
                .apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
        setSetting(0);
    }

    public class myReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int getSetting = bundle.getInt("setting");

            Switch sw1 = findViewById(R.id.switch1);
            if (getSetting == 1) {
                sw1.setChecked(true);
            } else {
                sw1.setChecked(false);
            }
        }
    }
}
