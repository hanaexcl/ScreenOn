package com.sobored.ScreenOn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.os.Bundle;
import android.content.SharedPreferences;


public class MainActivity extends AppCompatActivity {
    SharedPreferences mSharedPreferences = null;
    uiReceiver uireceiver = new uiReceiver();
    Intent intent = new Intent();
    boolean updateUi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent.setClass(this, KeepScreenOnService.class);

        Switch sw1 = findViewById(R.id.switch1);
        sw1.setChecked(getSetting());
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (updateUi) return;

                if (isChecked) {
                    Log.d("MainActivity", "onClick On");
                    setSetting(true);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent);
                    } else {
                        startService(intent);
                    }
                } else {
                    Log.d("MainActivity", "onClick Off");
                    setSetting(false);

                    stopService(intent);
                }
            }
        });

        registerReceiver(uireceiver, new IntentFilter("com.sobored.screenOn.intent")); //註冊更新UI服務
    }

    private boolean getSetting() {
        if (mSharedPreferences == null) mSharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean("Screen", false);
    }

    private void setSetting(boolean sw) {
        if (mSharedPreferences == null)
            mSharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
            mSharedPreferences.edit()
                .putBoolean("Screen", sw)
                .apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(uireceiver); //卸載更新UI服務
    }


    public class uiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int getSetting = bundle.getInt("setting");

            updateUi = true;
            Switch sw1 = findViewById(R.id.switch1);
            if (getSetting == 1) {
                sw1.setChecked(true);
            } else {
                sw1.setChecked(false);
            }
            updateUi = false;
        }
    }
}