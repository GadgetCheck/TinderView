package com.tinderview.cardstatck.cardstack;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.tinderview.R;
import com.tinderview.activities.MainScreenActivity;
import com.tinderview.cardstatck.animation.RelativeLayoutParamsEvaluator;

import java.util.ArrayList;
import java.util.HashMap;


/*
*  CardAnimator class is used to give animation on Card eg: dislike and like animation of card
*  dragging and dropping animation .
*  setting view of Card and arrange it in the form of stack.
*  shadow effect
*  curve effect of card
*  stack view
* */

public class CardAnimator {
    private static final String DEBUG_TAG = "CardAnimator";
    private static final int REMOTE_DISTANCE = 1000;
    public ArrayList<View> mCardCollection;
    private float mRotation; //dislike rotation value
    private HashMap<View, RelativeLayout.LayoutParams> mLayoutsMap;//used to store the list of card's view
    private RelativeLayout.LayoutParams[] mRemoteLayouts = new RelativeLayout.LayoutParams[4];//creating the four list of stack
    private RelativeLayout.LayoutParams baseLayout;
    private int mStackMargin = 21;//set the margin of cardstack


    //passing list of cardStack view to mCardCollection. and jumping to setup method
    public CardAnimator(ArrayList<View> viewCollection) {
        mCardCollection = viewCollection;
        setup();

    }


    //calling to setup the each card based on view
    private void setup() {
        mLayoutsMap = new HashMap<>();

        for (View v : mCardCollection) {
            //setup basic card layout
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.width = LayoutParams.MATCH_PARENT;
            params.height = LayoutParams.MATCH_PARENT;

            v.setLayoutParams(params);

        }

        baseLayout = (RelativeLayout.LayoutParams) mCardCollection.get(0).getLayoutParams();
        baseLayout = new RelativeLayout.LayoutParams(baseLayout);

        initLayout();//calling this method to arrange all the card based on StackView

        for (View v : mCardCollection) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
            RelativeLayout.LayoutParams paramsCopy = new RelativeLayout.LayoutParams(params);
            mLayoutsMap.put(v, paramsCopy);
        }

        setupRemotes();

    }

    /* finding the index of view and creating layoutParams using baseLayout object and those height and width setting to view
    *  using method of v.setLayoutParams
    *  and setting card scale size to give curve shape to card
    *  and moving card toward top of others card, and gives look of stack.
    * */
    public void initLayout() {
        int size = mCardCollection.size();
        for (View v : mCardCollection) {
            int index = mCardCollection.indexOf(v);
            if (index != 0) {
                index -= 1;
            }
            LayoutParams params = new LayoutParams(baseLayout);
            v.setLayoutParams(params);

            //calling scale method of CardUtils class to set the margin and than put layout into v
            CardUtils.scale(v, -(size - index - 1) * 8);

            /*calling move method of CardUtils class which is going to move the card one by on based on the mStackMargin
            * first index would be 0 so it won't move card and the based on the index it will multiply with the given margin
                   * and move card to down.
            */
            CardUtils.move(v, index * mStackMargin, 0);

            v.setRotation(0);//setting the rotation of card as 0

            //here calling 3 different shadow layout for each Card
            //which will give shadow effect dynamically ...
            if (index == 0)// setting 0 used to add different layout shadow...
                v.setBackground(MainScreenActivity.context.getResources().getDrawable(R.drawable.card_stack_bg_shadow3));
            else if (index == 1)
                v.setBackground(MainScreenActivity.context.getResources().getDrawable(R.drawable.card_stack_bg_shadow2));
            else if (index == 2)
                v.setBackground(MainScreenActivity.context.getResources().getDrawable(R.drawable.card_stack_bg_shadow));
        }
    }

    //setting the each card margins and putting view into array of mRemoteLayouts
    private void setupRemotes() {
        View topView = getTopView();
        mRemoteLayouts[0] = CardUtils.getMoveParams(topView, REMOTE_DISTANCE, -REMOTE_DISTANCE);
        mRemoteLayouts[1] = CardUtils.getMoveParams(topView, REMOTE_DISTANCE, REMOTE_DISTANCE);
        mRemoteLayouts[2] = CardUtils.getMoveParams(topView, -REMOTE_DISTANCE, -REMOTE_DISTANCE);
        mRemoteLayouts[3] = CardUtils.getMoveParams(topView, -REMOTE_DISTANCE, REMOTE_DISTANCE);
    }


    //return the top view of stack.
    private View getTopView() {
        return mCardCollection.get(mCardCollection.size() - 1);
    }


    private void moveToBack(View child) {
        final ViewGroup parent = (ViewGroup) child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    //rearrange the mCardCollection list...
    //
    private void reorder() {
        View temp = getTopView();
        moveToBack(temp);

        for (int i = (mCardCollection.size() - 1); i > 0; i--) {

            View current = mCardCollection.get(i - 1);
            //current replace next
            mCardCollection.set(i, current);


        }
        mCardCollection.set(0, temp);

        // temp = getTopView();

    }

    //using to set the discard animation of CardStack into ArrayList<Animator>
    public void discard(int direction, final AnimatorListener al) {
        AnimatorSet as = new AnimatorSet();
        ArrayList<Animator> aCollection = new ArrayList<>();


        final View topView = getTopView();
        RelativeLayout.LayoutParams topParams = (RelativeLayout.LayoutParams) topView.getLayoutParams();
        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(topParams);
        ValueAnimator discardAnim = ValueAnimator.ofObject(new RelativeLayoutParamsEvaluator(), layout, mRemoteLayouts[direction]);

        discardAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator value) {
                topView.setLayoutParams((LayoutParams) value.getAnimatedValue());
            }
        });

        discardAnim.setDuration(100);//setting the discard removed animation time in ms.
        aCollection.add(discardAnim);//putting the discardAnimation to animation ArrayLIst

        for (int i = 0; i < mCardCollection.size(); i++) {
            final View v = mCardCollection.get(i);

            if (v == topView) continue;
            final View nv = mCardCollection.get(i + 1);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
            RelativeLayout.LayoutParams endLayout = new RelativeLayout.LayoutParams(layoutParams);
            ValueAnimator layoutAnim = ValueAnimator.ofObject(new RelativeLayoutParamsEvaluator(), endLayout, mLayoutsMap.get(nv));
            layoutAnim.setDuration(100);
            layoutAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator value) {
                    v.setLayoutParams((LayoutParams) value.getAnimatedValue());
                }
            });
            aCollection.add(layoutAnim);
        }

        as.addListener(new AnimatorListenerAdapter() {


            @Override
            public void onAnimationEnd(Animator animation) {
                reorder();
                if (al != null) {
                    al.onAnimationEnd(animation);
                }
                mLayoutsMap = new HashMap<View, RelativeLayout.LayoutParams>();
                for (View v : mCardCollection) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    RelativeLayout.LayoutParams paramsCopy = new RelativeLayout.LayoutParams(params);
                    mLayoutsMap.put(v, paramsCopy);
                }

            }

        });


        as.playTogether(aCollection);
        as.start();


    }


    //even card is dragged and not discarded by the user then again this card will come on the top of the stack..
    //this method will give the animation of reverse along with the reverse animation duration
    // time in ms... which is I defined as 250

    public void reverse(MotionEvent e1, MotionEvent e2) {
        final View topView = getTopView();
        ValueAnimator rotationAnim = ValueAnimator.ofFloat(mRotation, 0f);
        rotationAnim.setDuration(250);
        rotationAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator v) {
                topView.setRotation(((Float) (v.getAnimatedValue())));
            }
        });

        rotationAnim.start();//start the animation

        for (final View v : mCardCollection) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
            RelativeLayout.LayoutParams endLayout = new RelativeLayout.LayoutParams(layoutParams);
            ValueAnimator layoutAnim = ValueAnimator.ofObject(new RelativeLayoutParamsEvaluator(), endLayout, mLayoutsMap.get(v));
            layoutAnim.setDuration(250);
            layoutAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator value) {
                    v.setLayoutParams((LayoutParams) value.getAnimatedValue());
                }
            });
            layoutAnim.start();
        }

    }


    //dragging method...it's gives the position of object and does the secondary animation of CardStack
    public void drag(MotionEvent e1, MotionEvent e2, float distanceX,
                     float distanceY) {

        View topView = getTopView();

        float rotation_coefficient = 20f;//

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) topView.getLayoutParams();
        RelativeLayout.LayoutParams topViewLayouts = mLayoutsMap.get(topView);
        int x_diff = (int) ((e2.getRawX() - e1.getRawX()));
        int y_diff = (int) ((e2.getRawY() - e1.getRawY()));

        layoutParams.leftMargin = topViewLayouts.leftMargin + x_diff;
        layoutParams.rightMargin = topViewLayouts.rightMargin - x_diff;
        layoutParams.topMargin = topViewLayouts.topMargin + y_diff;
        layoutParams.bottomMargin = topViewLayouts.bottomMargin - y_diff;

        mRotation = (x_diff / rotation_coefficient);
        topView.setRotation(mRotation);
        topView.setLayoutParams(layoutParams);

        //animate secondary views.
        for (View v : mCardCollection) {
            int index = mCardCollection.indexOf(v);
            if (v != getTopView() && index != 0) {
                LayoutParams l = CardUtils.scaleFrom(v, mLayoutsMap.get(v), (int) (Math.abs(x_diff) * 0.05));
                CardUtils.moveFrom(v, l, 0, (int) (Math.abs(x_diff) * 0.1));
            }
        }
    }

    //using to set the margin into cardStack
    public void setStackMargin(int margin) {
        mStackMargin = margin;//passing the value to mStackMargin
        initLayout(); //and then calling this method to set the margin into view.
    }


}
