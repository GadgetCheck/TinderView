package com.tinderview.cardstatck.cardstack;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


/*
* CardUtils is using for set the card scale , get the selected layout param, scaleFrom, moveFrom and direction of card.
* All these method are static which means it is going to access from some others classes and it returns the value based on the
* passed parameter.
*
* */
public class CardUtils {
    final static int DIRECTION_TOP_LEFT = 0;
    final static int DIRECTION_TOP_RIGHT = 1;
    final static int DIRECTION_BOTTOM_LEFT = 2;
    final static int DIRECTION_BOTTOM_RIGHT = 3;

    //it remove some pixels from the each sides of given view and then set that layout to view using setLayoutParams method.
    public static void scale(View v, int pixel) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
        params.leftMargin -= pixel;
        params.rightMargin -= pixel;
        params.topMargin -= pixel;
        params.bottomMargin -= pixel;
        v.setLayoutParams(params);
    }


    //setting the layout margin dynamically to make it as stack view and returning the layoutParams object.
    public static LayoutParams getMoveParams(View v, int upDown, int leftRight) {
        RelativeLayout.LayoutParams original = (RelativeLayout.LayoutParams) v.getLayoutParams();
        //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(original);
        RelativeLayout.LayoutParams params = cloneParams(original);
        params.leftMargin += leftRight;
        params.rightMargin -= leftRight;
        Log.d("moving...", params.leftMargin + "");

        params.topMargin -= upDown;
        params.bottomMargin += upDown;
        return params;
    }

    //used to move the card based on parameter, from this method again it's calling the getMoveParams(v,upDown,leftRight) methods
    public static void move(View v, int upDown, int leftRight) {
        RelativeLayout.LayoutParams params = getMoveParams(v, upDown, leftRight);
        v.setLayoutParams(params);
    }


    //setting the scale of card dynamically based on the parameters
    public static LayoutParams scaleFrom(View v, LayoutParams params, int pixel) {
        Log.d("pixel", "onScroll: " + pixel);
        params = cloneParams(params);
        params.leftMargin -= pixel;
        params.rightMargin -= pixel;
        params.topMargin -= pixel;
        params.bottomMargin -= pixel;
        Log.d("pixel", "onScroll: " + pixel);
        v.setLayoutParams(params);

        return params;
    }


    //setting the card margin based on the values using leftRight and Updown variables
    //this method is calling for secondary view

    public static LayoutParams moveFrom(View v, LayoutParams params, int leftRight, int upDown) {
        params = cloneParams(params);
        params.leftMargin += leftRight;
        params.rightMargin -= leftRight;
        params.topMargin -= upDown;
        params.bottomMargin += upDown;
        v.setLayoutParams(params);

        return params;
    }

    //a copy method for RelativeLayout.LayoutParams for backward compatibility
    public static RelativeLayout.LayoutParams cloneParams(RelativeLayout.LayoutParams params) {
        RelativeLayout.LayoutParams copy = new RelativeLayout.LayoutParams(params.width, params.height);
        copy.leftMargin = params.leftMargin;
        copy.topMargin = params.topMargin;
        copy.rightMargin = params.rightMargin;
        copy.bottomMargin = params.bottomMargin;
        int[] rules = params.getRules();
        for (int i = 0; i < rules.length; i++) {
            copy.addRule(i, rules[i]);
        }
        copy.setMarginStart(params.getMarginStart());
        copy.setMarginEnd(params.getMarginEnd());

        return copy;
    }


    //giving the distance in between the cards
    public static float distance(float x1, float y1, float x2, float y2) {
        Log.d("Data: ", "" + Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }


    // in which direction user dum card, that location this method will return ...
    // we can get to know that whether user has liked or dislike the card...
    //eg:
    // 0 | 1
    //--------
    // 2 | 3
    // dislike value :  even this method return the 0 or 2
    // like value: 1 or 3

    public static int direction(float x1, float y1, float x2, float y2) {
        if (x2 > x1) {//RIGHT
            if (y2 > y1) {//BOTTOM
                return DIRECTION_BOTTOM_RIGHT;
            } else {//TOP
                return DIRECTION_TOP_RIGHT;
            }
        } else {//LEFT
            if (y2 > y1) {//BOTTOM
                return DIRECTION_BOTTOM_LEFT;
            } else {//TOP
                return DIRECTION_TOP_LEFT;
            }
        }
    }


}
