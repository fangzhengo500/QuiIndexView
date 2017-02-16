package com.test.mtypelist.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.mtypelist.R;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @project： ListDemo
 * @package： com.test.mtypelist
 * @class: QuickIndexView
 * @author: 陆伟
 * @Date: 2017/2/14 10:32
 * @desc： TODO
 */
public class QuickIndexView extends RelativeLayout implements IndexView.IndexListener {

    private Context mContext;

    private View mRootView;
    private RecyclerView mRecyclerView;
    private IndexView mIndexView;

    private TreeMap<Integer, IndexView.Bean> mIndexMap = new TreeMap<>();
    private TextView mToastView;

    public QuickIndexView(Context context) {
        this(context, null);
    }

    public QuickIndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        //挂载View
        mRootView = LayoutInflater.from(context).inflate(R.layout.view_quick_index, this, true);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
        mIndexView = (IndexView) mRootView.findViewById(R.id.indexView);
        mToastView = (TextView) mRootView.findViewById(R.id.toast);
        initView();

        initListener();
    }


    /**
     * 初始化View
     */
    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void initListener() {
        mIndexView.setOnIndexListener(this);
    }

    /**
     * @param adapter
     */
    public void setAdapter(QuickIndexView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);

        initIndexMap();
        mIndexView.initIndexMap(mIndexMap);
    }

    private void initIndexMap() {
        DivideRecyclerViewAdapter adapter = (DivideRecyclerViewAdapter) mRecyclerView.getAdapter();
        int itemCount = adapter.getItemCount();
        for(int poistion = itemCount - 1; poistion >= 0; poistion--) {
            int itemType = adapter.getItemViewType(poistion);
            int index = poistion;
            String indexString = adapter.getTitle(adapter.getItemViewType(poistion));
            mIndexMap.put(itemType, new IndexView.Bean(poistion, indexString));
        }
    }

    @Override
    public void onIndex(int indexType) {
        if(mIndexMap == null)
            return;
        IndexView.Bean bean = mIndexMap.get(indexType);
        mToastView.setText(bean.indexText);
        mToastView.setVisibility(VISIBLE);
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(bean.position,0);
    }

    @Override
    public void onfinishIndex() {
        mToastView.setVisibility(GONE);
    }

    /** QuickAdapter */
    public static abstract class Adapter<VH extends DivideRecyclerViewHolder> extends DivideRecyclerViewAdapter<VH> {

        @Override
        public abstract int getItemViewType(int position);


    }

    /** DivideRecyclerViewHolder */
    public static class ViewHolder extends DivideRecyclerViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
