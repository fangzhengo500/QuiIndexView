package com.test.quickindexviewlibrary.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.test.quickindexviewlibrary.R;


public class DivideRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView head;
    public FrameLayout contentView;
    
    public DivideRecyclerViewHolder(View itemView) {
        super(itemView);
        head = (TextView) itemView.findViewById(R.id.head);
        contentView = (FrameLayout) itemView.findViewById(R.id.itemContent);
        contentView.setTag(this);
    }
}