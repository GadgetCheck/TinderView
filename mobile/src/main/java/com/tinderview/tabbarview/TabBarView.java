package com.tinderview.tabbarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

import com.tinderview.R;

/*
* created by Aradh Pillai on 1.10.15
* this class is inheriting features of LinearLayout
* using this it's creating TabBarView dynamically
*
*
* */

public class TabBarView extends LinearLayout {
    //strip height has defined here
    private static final int STRIP_HEIGHT = 6;
    public static int mSelectedTab = 0;
    public static int tabCount;//hold the number of tabs
    public static int a;
    public final Paint mPaint;
    //this pager will hold the instance of viewPager
    //and rearrange the pages using the object
    public ViewPager pager;
    public OnPageChangeListener delegatePageListener;
    // TabView class objects
    private TabView tabView1, tabView2, tabView3;
    private int mStripHeight;
    private float mOffset = 0f;
    private View child;

    private View nextChild;

    public TabBarView(Context context) {
        this(context, null);
    }

    public TabBarView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.actionBarTabBarStyle);
    }

    public TabBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT));

        setWillNotDraw(false);

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);

        mPaint.setAntiAlias(false);

        mStripHeight = (int) (STRIP_HEIGHT * getResources().getDisplayMetrics().density + .5f);
    }

    //use to set StripColor
    public void setStripColor(int color) {
        if (mPaint.getColor() != color) {
            mPaint.setColor(color);
            invalidate();
        }
    }

    //Use to set the height of strip
    public void setStripHeight(int height) {
        if (mStripHeight != height) {
            mStripHeight = height;
            invalidate();
        }
    }

    //using to set the tab based on  Tabindex which is need to pass as parameter.
    public void setSelectedTab(int tabIndex) {
        if (tabIndex < 0) {
            tabIndex = 0;
        }
        final int childCount = getChildCount();
        if (tabIndex >= childCount) {
            tabIndex = childCount - 1;
        }
        if (mSelectedTab != tabIndex) {
            mSelectedTab = tabIndex;
            invalidate();
        }
    }

    public void setOffset(int position, float offset) {
        if (mOffset != offset) {
            mOffset = offset;
            invalidate();
        }
    }

    //this method is using to draw the strip
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the strip manually
        child = getChildAt(mSelectedTab);
        int height = getHeight();
        if (child != null) {
            float left = child.getLeft();
            float right = child.getRight();
            if (mOffset > 0f && mSelectedTab < tabCount - 1) {
                nextChild = getChildAt(mSelectedTab + 1);
                if (nextChild != null) {
                    final float nextTabLeft = nextChild.getLeft();
                    final float nextTabRight = nextChild.getRight();
                    left = (mOffset * nextTabLeft + (1f - mOffset) * left);
                    right = (mOffset * nextTabRight + (1f - mOffset) * right);
                }
            }
            canvas.drawRect(left, height - mStripHeight, right, height, mPaint);
        }
    }

    //this method is used to set the pager reference to this.pager object and call the notifyDataSetChanged() method
    //
    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        notifyDataSetChanged();
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        mSelectedTab = position;
        mOffset = positionOffset;

        invalidate();

        if (delegatePageListener != null) {
            delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        //based on the position setting tab alpha as 1 if it's selected or others set is as 0.5f
        if (position == 0) {
            tabView1.setAlpha(1.0f);
            tabView2.setAlpha(0.5f);
            tabView3.setAlpha(0.5f);

        } else if (position == 1) {
            tabView1.setAlpha(0.5f);
            tabView2.setAlpha(1.0f);
            tabView3.setAlpha(0.5f);

        } else if (position == 2) {
            tabView1.setAlpha(0.5f);
            tabView2.setAlpha(0.5f);
            tabView3.setAlpha(1.0f);

        }
    }

    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {

        }

        if (delegatePageListener != null) {
            delegatePageListener.onPageScrollStateChanged(state);
        }
    }

    public void onPageSelected(int position) {
        if (delegatePageListener != null) {
            delegatePageListener.onPageSelected(position);
        }
    }

    // it will remove all previous views and creating new view for tab
    public void notifyDataSetChanged() {

        int resId;

        this.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {

            resId = ((IconTabProvider) pager.getAdapter()).getPageIconResId(i);

            if (getResources().getConfiguration().orientation == 1)
                addTabViewP(i, pager.getAdapter().getPageTitle(i).toString(), resId);
            else
                addTabViewL(i, pager.getAdapter().getPageTitle(i).toString(), resId);
        }

        //removing swiping animation effect from viewPager


        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                getViewTreeObserver().removeOnGlobalLayoutListener(this);

                mSelectedTab = pager.getCurrentItem();

            }
        });

    }

    //used to set the properties for landscape screen like icon,text and click-event into tab and then add this tab into View using addView(tab) method
    private void addTabViewL(final int i, String string, int pageIconResId) {
        // TODO Auto-generated method stub
        TabView tab = new TabView(getContext());
//		tab.setIcon(pageIconResId);
        tab.setText(string, pageIconResId);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(i, false);
            }
        });

        this.addView(tab);
    }

    private void addTabViewP(final int i, final String string, int pageIconResId) {
        // TODO Auto-generated method stub

        if (i == 0) {
            tabView1 = new TabView(getContext());

            tabView1.setIcon(pageIconResId);
            tabView1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(i);


                }
            });
            CheatSheet.setup(tabView1, string);
            this.addView(tabView1);
        } else if (i == 1) {
            tabView2 = new TabView(getContext());
            tabView2.setIcon(pageIconResId);
            tabView2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(i);

                }
            });
            CheatSheet.setup(tabView2, string);
            this.addView(tabView2);
        } else if (i == 2) {
            tabView3 = new TabView(getContext());
            tabView3.setIcon(pageIconResId);
            tabView3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(i);

                }
            });
            CheatSheet.setup(tabView3, string);
            this.addView(tabView3);
        }


    }

    //used to set the properties for portrait screen like icon and click-event into tabView objects
    // and then add this tab object into View using addView(tabView1) method
    //here we setup the tabs properties

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public interface IconTabProvider {
        int getPageIconResId(int position);
    }
}