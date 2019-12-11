package com.sobored.ScreenOn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

public class TileSrv extends TileService {
    SharedPreferences mSharedPreferences = null;
    Intent intent = new Intent();

    @Override
    public void onCreate() {
        super.onCreate();

        intent.setClass(this, KeepScreenOnService.class);
        Log.d("TileSrv", "onCreate");
    }

    @Override
    public void onClick() {
        super.onClick();
        Intent broadcastIntent = new  Intent("com.sobored.screenOn.intent");

        if (getQsTile().getState() == Tile.STATE_INACTIVE) {
            //啟動
            getQsTile().setState(Tile.STATE_ACTIVE);
            getQsTile().updateTile();
            setSetting(1);
            startService(intent);
            broadcastIntent.putExtra("setting", 1);
        } else {
            //關閉
            getQsTile().setState(Tile.STATE_INACTIVE);
            getQsTile().updateTile();
            setSetting(0);
            startService(intent);
            broadcastIntent.putExtra("setting", 0);
        }

        sendBroadcast(broadcastIntent);
        Log.d("TileSrv", "onClick");
    }

    @Override
    public void onStopListening() {
        super.onStopListening();

        Log.d("TileSrv", "onStopListening");
    }

    @Override
    public void onStartListening() {
        if (getSetting() == 1) {
            getQsTile().setState(Tile.STATE_ACTIVE);
            getQsTile().updateTile();
        } else {
            getQsTile().setState(Tile.STATE_INACTIVE);
            getQsTile().updateTile();
        }
        super.onStartListening();

        Log.d("TileSrv", "onStopListening");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TileSrv", "onStartCommand");

        return super.onStartCommand(intent, flags, startId);
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
}
