package com.android.msahakyan.marsrover.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class MyOnclickListener implements View.OnClickListener {

    private Activity mActivity;

    public MyOnclickListener(Activity activity) {
        mActivity = activity;
    }

    public void onClick(View view) {
        Intent intent = new Intent("a.unique.string");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        mActivity.startActivityForResult(intent, 1);
    }
}
