package com.tinderview.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.tinderview.R;

import java.util.ArrayList;

/**
 * Created by Aradh Pillai on 14/10/15.
 * <p/>
 * This class is creating for  Background Ripple Animation
 */

public class RippleAnimation extends RelativeLayout {

    private static final int DEFAULT_RIPPLE_COUNT = 6;
    private static final int DEFAULT_DURATION_TIME = 3000;
    private static final float DEFAULT_SCALE = 6.0f;
    private static final int DEFAULT_FILL_TYPE = 0;

    private int rippleColor;
    private float rippleStrokeWidth;
    private float rippleRadius;
    private int rippleDurationTime;
    private int rippleAmount;
    private int rippleDelay;
    private float rippleScale;
    private int rippleType;
    private Paint paint;
    private boolean animationRunning = false;
    private AnimatorSet animatorSet;
    private ArrayList<Animator> animatorList;
    private LayoutParams rippleParams;
    private ArrayList<RippleView> rippleViewList = new ArrayList<RippleView>();


    //constructor with single parameter
    public RippleAnimation(Context context) {
        super(context);
    }

    public RippleAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    //Parameter Constructor
    public RippleAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //initialized and sets all the properties of RippleAnimation
    private void init(final Context context, final AttributeSet attrs) {
        if (isInEditMode())
            return;

        if (null == attrs) {
            throw new IllegalArgumentException("Attributes should be provided to this view,");
        }

        //created typedArray and setting all properties to typedArray object
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleAnimation);

        //it will return ripple color even we have specified into xml or else it will get the default ripple Color
        rippleColor = typedArray.getColor(R.styleable.RippleAnimation_rb_color, getResources().getColor(R.color.rippleColor));

        //getting rippleStrokeWidth from typedArray object and putting it into rippleStrokeWidth variable
        rippleStrokeWidth = typedArray.getDimension(R.styleable.RippleAnimation_rb_strokeWidth, getResources().getDimension(R.dimen.rippleStrokeWidth));

        //rippleRadius contains the default the specified radius even we specified or else it takes default radius...which is 64dp
        rippleRadius = typedArray.getDimension(R.styleable.RippleAnimation_rb_radius, getResources().getDimension(R.dimen.rippleRadius));

        //setting rippleAnimation duration is specified or else it takes default Duration Time.
        rippleDurationTime = typedArray.getInt(R.styleable.RippleAnimation_rb_duration, DEFAULT_DURATION_TIME);

        //number of ripple if we have specified into design part or else default value it will take.
        rippleAmount = typedArray.getInt(R.styleable.RippleAnimation_rb_rippleAmount, DEFAULT_RIPPLE_COUNT);

        //setting scale of ripple
        rippleScale = typedArray.getFloat(R.styleable.RippleAnimation_rb_scale, DEFAULT_SCALE);

        //its getting the ripple type and putting it into rippleType variable.
        rippleType = typedArray.getInt(R.styleable.RippleAnimation_rb_type, DEFAULT_FILL_TYPE);

        /**
         * Recycles the TypedArray, to be re-used by a later caller. After calling
         * this function you must not ever touch the typed array again.
         *
         * @throws RuntimeException if the TypedArray has already been recycled.
         */
        typedArray.recycle();

        rippleDelay = rippleDurationTime / rippleAmount;

        paint = new Paint();//creating an object of paint to make ripple based on the ripple type
        paint.setAntiAlias(true);
        if (rippleType == DEFAULT_FILL_TYPE) {
            rippleStrokeWidth = 0;
            paint.setStyle(Paint.Style.FILL);
        } else
            paint.setStyle(Paint.Style.STROKE);
        paint.setColor(rippleColor);

        rippleParams = new LayoutParams((int) (2 * (rippleRadius + rippleStrokeWidth)), (int) (2 * (rippleRadius + rippleStrokeWidth)));
        rippleParams.addRule(CENTER_IN_PARENT, TRUE);//setting ripple into center

        animatorSet = new AnimatorSet();// creates animationSet object
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorList = new ArrayList<Animator>();


        /*based on the number of ripple this for loop create ripple View and put three different animation for this view..
        *
        * */
        for (int i = 0; i < rippleAmount; i++) {
            RippleView rippleView = new RippleView(getContext());//created ripple
            addView(rippleView, rippleParams);//adding view
            rippleViewList.add(rippleView);//putting this view into rippleViewList object


            //creating objectAnimator object and setting some X Scale properties to object and putting it into animatorList<Animator>
            final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleX", 1.0f, rippleScale);
            scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            scaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleXAnimator.setStartDelay(i * rippleDelay);
            scaleXAnimator.setDuration(rippleDurationTime);
            animatorList.add(scaleXAnimator);

            //creating objectAnimator object and setting some Y Scale properties to object and putting it into animatorList<Animator>
            final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleY", 1.0f, rippleScale);
            scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            scaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleYAnimator.setStartDelay(i * rippleDelay);
            scaleYAnimator.setDuration(rippleDurationTime);
            animatorList.add(scaleYAnimator);

            //setting alpha of the animation and putting it into animatorList<Animator>
            final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(rippleView, "Alpha", 1.0f, 0f);
            alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
            alphaAnimator.setStartDelay(i * rippleDelay);
            alphaAnimator.setDuration(rippleDurationTime);
            animatorList.add(alphaAnimator);
        }

        //played animator
        animatorSet.playTogether(animatorList);
    }

    //used to start ripple Animation
    public void startRippleAnimation() {
        if (!isRippleAnimationRunning()) {
            for (RippleView rippleView : rippleViewList) {
                rippleView.setVisibility(VISIBLE);
            }
            animatorSet.start();//animation started
            animationRunning = true;//setting value as true... this will support to check isRippleAnimation is running or not.
        }
    }

    public void stopRippleAnimation() {
        if (isRippleAnimationRunning()) {
            animatorSet.end();
            animationRunning = false;
        }
    }

    //stopping ripple Animation using end() method and setting boolean value false to animationRunning

    /*call this method to check whether animation is running or not.
    * then basically it return the animationRunning boolean value.
    *
    * even we have stopped animation then it will return false value.
    * or even we have not stopped animation then it will return true value.
    */
    public boolean isRippleAnimationRunning() {
        return animationRunning;
    }

    //this class is using to draw the circle
    private class RippleView extends View {

        public RippleView(Context context) {
            super(context);
            this.setVisibility(View.INVISIBLE);
        }

        //it's drawing ripple based on radius value.
        @Override
        protected void onDraw(Canvas canvas) {
            int radius = (Math.min(getWidth(), getHeight())) / 2;
            canvas.drawCircle(radius, radius, radius - rippleStrokeWidth, paint);
        }
    }
}
