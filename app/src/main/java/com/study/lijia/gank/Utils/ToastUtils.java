package com.study.lijia.gank.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * ToastUtils帮助类
 */
public class ToastUtils {

    private static Toast sToast;
    private static boolean sDebug = false;

    public static void setDebug(boolean debug) {
        sDebug = debug;
    }

    public static void showLong(Context ctx, final String message) {
        cancel();
        showMessage(ctx, message, Toast.LENGTH_LONG);
    }

    public static void showShort(Context ctx, int resId) {
        showShort(ctx, ctx.getString(resId));
    }

    public static void showShort(Context ctx, final String message) {
        cancel();
        showMessage(ctx, message, Toast.LENGTH_SHORT);
    }

    public static void debug(Context ctx, String message) {
        if (sDebug) {
            showMessage(ctx, "Debug: " + message, Toast.LENGTH_LONG);
        }
    }

    private static void showMessage(Context ctx, final String message, final int duration) {
        final Context appContext = ctx.getApplicationContext();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                sToast = Toast.makeText(appContext, message, duration);
                sToast.show();
            }
        });
    }

    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }
}