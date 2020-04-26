package com.shlsoft.fooddelivery.app;

import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.appcompat.app.AppCompatActivity;

import com.shlsoft.fooddelivery.service.ConnectivityReceiver;

public class BaseActivity extends AppCompatActivity {
    ConnectivityReceiver receiver = new ConnectivityReceiver();

    //Tracking the connection
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }
}
