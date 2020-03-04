package com.pool.tronik.pooltronik.utils;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

public class IntentHelper {

    public static Intent getIntent(Context context, Class aClass) {
        return new Intent(context,aClass);
    }

    public static Intent getIntent(Context context, Class aClass, Object extra) {
        Intent intent = new Intent(context,aClass);
        intent.putExtra(StringUtils.EXTRA_DATA, (Serializable) extra);
        return intent ;
    }
}
