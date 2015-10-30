package com.tinderview.tabbarview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tinderview.R;

/*
* created by Aradh Pillai on 1.10.15
* This TabView class using to create tab and setting ImageView and Text into TabView
* and adding ripple animation into View.
*
*
* */


public class TabView extends LinearLayout {

    private ImageView mImageView;
    private TextView mTextView;

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.actionBarTabStyle);
    }

    public TabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarTabTextStyle, outValue, true);

        int txtstyle = outValue.data;

        int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
                .getDisplayMetrics());


        mImageView = new ImageView(context);//dynamically create an object of imageView and setting param into it...
        mImageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mImageView.setScaleType(ScaleType.CENTER_INSIDE);

        mTextView = new TextView(context); //dynamically create a text view and setting these properties into it...
        mTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setCompoundDrawablePadding(pad);
        mTextView.setTextAppearance(context, txtstyle);


        this.addView(mImageView);// adding the imageview object into view
        this.addView(mTextView);// adding textview object into view
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));//setting param into view

        this.setBackgroundResource(R.drawable.list_selector_white);//setting ripple animation into this View.


    }

    public void setIcon(int resId) {
        setIcon(getContext().getResources().getDrawable(resId));
    }

    public void setIcon(Drawable icon) {
        if (icon != null) {
            mImageView.setVisibility(View.VISIBLE);
            mImageView.setImageDrawable(icon);
        } else {
            mImageView.setImageResource(View.GONE);
        }
    }

    public void setText(int resId, int ico) {
        setText(getContext().getString(resId), ico);
    }

    public void setText(CharSequence text, int ico) {
        mTextView.setText(text);
        mTextView.setCompoundDrawablesWithIntrinsicBounds(ico, 0, 0, 0);
    }

}