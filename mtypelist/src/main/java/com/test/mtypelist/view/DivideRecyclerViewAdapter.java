package com.test.mtypelist.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.test.mtypelist.R;

public  abstract class DivideRecyclerViewAdapter<VH extends DivideRecyclerViewHolder> extends RecyclerView.Adapter<VH> {
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.irem_view_with_title, parent, false);
        FrameLayout itemContent = (FrameLayout) itemView.findViewById(R.id.itemContent);
        itemContent.addView(getItemView());
        return getViewHolder(itemView);
    }
    
    
    @Override
    public void onBindViewHolder(VH holder, int position) {
        if(position == 0) {
            holder.head.setVisibility(View.VISIBLE);
        } else {
            if(getItemViewType(position - 1) == getItemViewType(position)) {
                holder.head.setVisibility(View.GONE);
            } else {
                holder.head.setVisibility(View.VISIBLE);
            }
        }
        holder.head.setText(getTitle(getItemViewType(position)));
        onBindViewDatas(holder, position);
    }
    
    
    /**
     * 设置条目数据
     *
     * @param holder
     * @param position
     */
    protected abstract void onBindViewDatas(VH holder, int position);
    
    /**
     * 获取item样式View
     *
     * @return
     */
    public abstract View getItemView();
    
    /**
     * 获取ViewHolder
     *
     * @param itemView
     *
     * @return
     */
    protected abstract VH getViewHolder(View itemView);
    
    /**
     * 设置item的头
     *
     * @param itemViewType
     *
     * @return
     */
    protected abstract String getTitle(int itemViewType);
}