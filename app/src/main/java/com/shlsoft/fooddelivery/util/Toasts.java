package com.shlsoft.fooddelivery.util;

import android.widget.Toast;

import com.shlsoft.fooddelivery.app.MyApplication;

import es.dmoral.toasty.Toasty;

public class Toasts {
    public static void showSuccessToast(String toast_message){
        Toasty.success(MyApplication.getInstance(),toast_message, Toast.LENGTH_SHORT).show();
    }

    public static void showErrorToast(String toast_message){
        Toasty.error(MyApplication.getInstance(),toast_message, Toast.LENGTH_SHORT).show();
    }
}
