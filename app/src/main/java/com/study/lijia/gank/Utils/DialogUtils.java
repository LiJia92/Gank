package com.study.lijia.gank.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.study.lijia.gank.R;
import com.study.lijia.gank.widget.RotatingDialog;

/**
 * 加载提示框
 */
public class DialogUtils {

    /**
     * 显示并且生成一个dialog
     *
     * @param context 上下文
     * @return dialog对象
     */
    public static Dialog showCircleLoading(Context context) {
        return showCircleLoading(context, null);
    }

    /**
     * 显示并且生成一个dialog
     *
     * @param context 上下文
     * @param msg     文字信息
     * @return dialog对象
     */
    public static Dialog showCircleLoading(Context context, String msg) {
        return showCircleLoading(context, msg, false);
    }

    /**
     * 显示并且生成一个dialog
     *
     * @param context 上下文
     * @param msg     文字信息
     * @param enlarge 是否加大dialog大小
     * @return dialog对象
     */
    public static Dialog showCircleLoading(Context context, String msg, boolean enlarge) {
        if (context == null) {
            return null;
        }
        RotatingDialog dialog = new RotatingDialog(context, R.style.progress_loading);
        if (enlarge) {
            dialog.enlarge();
        }
        if (TextUtils.isEmpty(msg)) {
            msg = context.getString(R.string.loading);
        }
        dialog.setText(msg);
        dialog.show();
        return dialog;
    }

    //  =================== 展示AlterDialog Start===================

    public static AlertDialog showAlertDialog(Context ctx, String title, String msg) {
        return showAlertDialog(ctx, title, msg, "知道了");
    }

    public static AlertDialog showAlertDialog(Context ctx, String title, String msg, DialogInterface.OnClickListener listener) {
        return showAlertDialog(ctx, title, msg, "知道了", listener);
    }

    public static AlertDialog showAlertDialog(Context ctx, String title, String msg, String okText) {
        return showAlertDialog(ctx, title, msg, okText, null);
    }

    public static AlertDialog showAlertDialog(Context ctx, String title, String msg, String okText, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setMessage(msg);
        builder.setPositiveButton(okText, listener);
        builder.setCancelable(false);
        return builder.show();
    }

    public static AlertDialog showAlertDialog(Context ctx, String title, String msg,
                                              String positiveBtnText, String negativeBtnText,
                                              DialogInterface.OnClickListener positiveListener,
                                              DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveBtnText, positiveListener);
        builder.setNegativeButton(negativeBtnText, negativeListener);
        builder.setCancelable(false);
        return builder.show();
    }

    //  =================== 展示AlterDialog End ===================

    /**
     * 关闭对话框
     */
    public static void closeDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
