package com.sureshcs50.popularmovies_p1.utils;

import android.app.Activity;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sureshkumar on 14/03/16.
 */
public class Utils {

    public static Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        return headers;
    }

    public static Drawable getColoredDrawable(Activity mActivity, int drawableResId, int color) {
        Drawable d = mActivity.getResources().getDrawable(drawableResId);
        ColorFilter filter = new LightingColorFilter(
                color,
                color);
        d.setColorFilter(filter);
        return d;
    }
}