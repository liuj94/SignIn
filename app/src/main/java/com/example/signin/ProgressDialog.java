package com.example.signin;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class ProgressDialog extends Dialog {


    public ProgressDialog(@NonNull Context context) {

        super(context, R.style.CustomDialog);
//        setContentView(R.layout.public_dialog_porgress);
        setContentView(R.layout.placeholder_web_02);

//        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha=0.8f;
        getWindow().setAttributes(attributes);
//        setCancelable(false);
    }



}
