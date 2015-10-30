package com.tinderview.cardstatck.cardstack;


/*this is using to put the listener event into cardStack

 */
public class DefaultStackEventListener implements CardStack.CardEventListener {

    private float mThreshold = 0;

    public DefaultStackEventListener(int i) {
        mThreshold = i;
    }

    @Override
    public boolean swipeEnd(int section, float distance) {
        return distance > mThreshold;
    }

    @Override
    public boolean swipeStart(int section, float distance) {

        return true;
    }

    @Override
    public boolean swipeContinue(int section, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void discarded(int mIndex, int direction) {

    }

    @Override
    public void topCardTapped() {

    }


}
