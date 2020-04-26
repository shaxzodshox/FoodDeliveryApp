package com.shlsoft.fooddelivery.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import androidx.appcompat.app.AlertDialog;

import com.shlsoft.fooddelivery.R;

public class ConnectivityReceiver extends BroadcastReceiver {

    AlertDialog dialog;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean noConnection = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY,false
            );

            if(noConnection){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Internet")
                        .setMessage("Iltimos dasturdan foydalanish uchun internetga ulaning")
                        .setCancelable(false)
                        .setIcon(R.drawable.connection_icon)
                ;
                dialog = builder.create();
                dialog.show();
            }
            else {
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        }
    }


}
