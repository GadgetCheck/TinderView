package com.tinderview.cardstatck.animation;

import android.animation.TypeEvaluator;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/*
* when card discard or reverse then this animation class is calling
* and this class is overloading the evaluate method of TypeEvaluator class
* */
public class RelativeLayoutParamsEvaluator implements TypeEvaluator<LayoutParams> {


    @Override
    public LayoutParams evaluate(float fraction, LayoutParams start,
                                 LayoutParams end) {

        LayoutParams result = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            result = new LayoutParams(start);
        }else{
            result = LayoutParamsALT(start);
        }
        result.leftMargin += ((end.leftMargin - start.leftMargin) * fraction);
        result.rightMargin += ((end.rightMargin - start.rightMargin) * fraction);
        result.topMargin += ((end.topMargin - start.topMargin) * fraction);
        result.bottomMargin += ((end.bottomMargin - start.bottomMargin) * fraction);
        return result;
    }
    public LayoutParams LayoutParamsALT(LayoutParams source) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(source.width, source.height);
        layoutParams.width = source.width;
        layoutParams.height = source.height;

        layoutParams.leftMargin = source.leftMargin;
        layoutParams.topMargin = source.topMargin;
        layoutParams.rightMargin = source.rightMargin;
        layoutParams.bottomMargin = source.bottomMargin;
        layoutParams.alignWithParent = source.alignWithParent;

        System.arraycopy(source.getRules(), 0, layoutParams.getRules(), 0, 22);
        return layoutParams;
    }

}
