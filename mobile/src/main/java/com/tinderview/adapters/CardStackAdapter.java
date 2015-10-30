package com.tinderview.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tinderview.R;


/**
 * Created by Aradh Pillaion 07/10/15.
 */

/*
* cardStackAdapter which is going to hold list of information and setting it into CardStack
*
* */
public class CardStackAdapter extends ArrayAdapter<String> {


    public CardStackAdapter(Context context, int resource) {

        super(context, 0);
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent) {
        //supply the layout for your card
        TextView v = (TextView) (contentView.findViewById(R.id.helloText));
        v.setText(getItem(position));
        return contentView;
    }

}
