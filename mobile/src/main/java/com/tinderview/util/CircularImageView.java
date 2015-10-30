package com.tinderview.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * Created by Aradh Pillaion 07/10/15.
 * <p/>
 * setting the context and image resource from drawable and converting it into bitmap
 */
public class CircularImageView {

    public static Drawable makeCircleFromResources(Context context, int resource) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                context.getResources(), bitmapToSquare(bitmap));
        roundedBitmapDrawable.setCornerRadius(bitmap.getWidth() / 2);
        roundedBitmapDrawable.setAntiAlias(true);
        return roundedBitmapDrawable;
    }


    //
    private static Bitmap bitmapToSquare(Bitmap bitmap) {
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            bitmap = Bitmap.createBitmap(bitmap,
                    bitmap.getWidth() / 2 - bitmap.getHeight() / 2, 0,
                    bitmap.getHeight(), bitmap.getHeight());
        } else {
            bitmap = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() / 2
                            - bitmap.getWidth() / 2, bitmap.getWidth(),
                    bitmap.getWidth());
        }

        return bitmap;
    }

}
