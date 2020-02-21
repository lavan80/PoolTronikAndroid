package com.pool.tronik.pooltronik.utils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.ImageButton;

public class ColorUtils {
    public static final String COLOR_ON = "#19a81b";
    public static final String COLOR_OFF = "#e00d0d";

    public static void setColor(ImageButton imageButton, int status) {
        GradientDrawable backgroundGradient = (GradientDrawable)imageButton.getBackground();
        if (status == RelayConfig.STATUS_OFF)
            backgroundGradient.setColor(Color.parseColor(ColorUtils.COLOR_OFF));
        else if (status == RelayConfig.STATUS_ON)
            backgroundGradient.setColor(Color.parseColor(ColorUtils.COLOR_ON));
    }
}
