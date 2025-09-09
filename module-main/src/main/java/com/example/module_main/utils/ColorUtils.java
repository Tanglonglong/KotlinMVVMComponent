package com.example.module_main.utils;

import android.content.Context;

import androidx.core.content.ContextCompat;

/**
 * 应用模块:
 * <p>
 * 类描述:
 * <p>
 *
 */
public class ColorUtils {
    public static int getColor(Context context,int colorId){
       return ContextCompat.getColor(context,colorId);
    }
}
