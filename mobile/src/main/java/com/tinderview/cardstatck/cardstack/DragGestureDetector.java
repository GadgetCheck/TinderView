package com.tinderview.cardstatck.cardstack;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;


//detect both tap and drag based on the gesture
/*
* in this class DragListener interface is defined which is going to use to return the boolean value based on the scope of CardStack
* whether card is start, dragging , dum, etc.
*
*
*
* */


public class DragGestureDetector {
    public static String DEBUG_TAG = "DragGestureDetector";
    GestureDetectorCompat mGestrueDetector;
    DragListener mListener;
    private boolean mStarted = false;
    private boolean mDown = false;

    private MotionEvent mOriginalEvent;

    public DragGestureDetector(Context context, DragListener myDragListener) {
        mGestrueDetector = new GestureDetectorCompat(context, new MyGestureListener());
        mListener = myDragListener;
    }

    public void onTouchEvent(MotionEvent event) {
        mGestrueDetector.onTouchEvent(event);
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case (MotionEvent.ACTION_UP):
                Log.d(DEBUG_TAG, "Action was UP " + mStarted);
                if (mStarted) {
                    mListener.onDragEnd(mOriginalEvent, event);
                    mStarted = false;
                }
                break;

            case (MotionEvent.ACTION_DOWN):
                Log.d(DEBUG_TAG, "Action was down " + mStarted);


                if (mDown) {
                    mListener.onDragEnd(mOriginalEvent, event);
                    mDown = false;

                }
                break;
            case (MotionEvent.ACTION_SCROLL):
                Log.d(DEBUG_TAG, "Scroll " + mStarted);
                if (mStarted) {
                    mListener.onDragEnd(mOriginalEvent, event);

                }
                // break;

                //need to set this, quick tap will not generate drap event, so the
                //originalEvent may be null for case action_up
                //which lead to null pointer
                mOriginalEvent = event;

        }
    }

    public interface DragListener {
        boolean onDragStart(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY);

        boolean onDragContinue(MotionEvent e1, MotionEvent e2, float distanceX,
                               float distanceY);

        boolean onDragEnd(MotionEvent e1, MotionEvent e2);

        boolean onTapUp();
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                float distanceY) {
            if (mListener == null) {
                Log.d(DEBUG_TAG, "NULL");
                return true;
            }
            if (mStarted == false) {
                Log.d(DEBUG_TAG, "onDragStart " + mStarted);
                mListener.onDragStart(e1, e2, distanceX, distanceY);
                mStarted = true;
            } else {
                Log.d(DEBUG_TAG, "onDragContinue " + mStarted);
                mListener.onDragContinue(e1, e2, distanceX, distanceY);
                //mOriginalEvent = e1;
                return true;
            }
            mOriginalEvent = e1;
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d(DEBUG_TAG, "OnSingTapUp" + mStarted);
            return mListener.onTapUp();
        }
    }


}
