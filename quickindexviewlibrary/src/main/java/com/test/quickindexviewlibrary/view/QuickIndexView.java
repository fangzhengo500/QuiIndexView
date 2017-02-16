package com.test.quickindexviewlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.test.quickindexviewlibrary.R;

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
    private static final String TAG = "QuickIndexView";
    private static final float TEXT_SIZE = 15;
    private static final int INDEX_TEXT_COLOR = 0xff555555;
    private static final int INDEX_SELECTED_TEXT_COLOR = 0xffffffff;
    private static final int TOSAT_WIDTH = 50;
    private static final int TOSAT_HEIGHT = 50;

    private Context mContext;

    private View mRootView;
    private RecyclerView mRecyclerView;
    private IndexView mIndexView;

    private TreeMap<Integer, IndexView.Bean> mIndexMap = new TreeMap<>();
    private TextView mToastView;

    private float mIndexTextSize = TEXT_SIZE;
    private int mIndexTextColor = INDEX_TEXT_COLOR;
    private int mIndexSelectedTextColor = INDEX_SELECTED_TEXT_COLOR;
    private float mToastWidth = TOSAT_WIDTH;
    private float mToastHeight = TOSAT_HEIGHT;
    private float mToastTextSize = TEXT_SIZE;
    private int mToastTextColor = INDEX_TEXT_COLOR;
    private Drawable mToastBackground;
    private int mToastBackgroundColor;


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

        initAttrs(context, attrs);

        initView();

        initListener();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuickIndexView);
        //右侧索引的字体大小
        mIndexTextSize = typedArray.getDimension(R.styleable.QuickIndexView_indexTextSize, mIndexTextSize);

        //右侧索引的字体颜色
        int indexTextColorId = typedArray.getResourceId(R.styleable.QuickIndexView_indexTextColor, - 1);
        if(indexTextColorId != - 1) {
            mIndexTextColor = getResources().getColor(indexTextColorId);
        } else {
            mIndexTextColor = typedArray.getColor(R.styleable.QuickIndexView_indexTextColor, mIndexTextColor);
        }

        //右侧索引的选择时字体颜色
        int indexSelectedTextColorId = typedArray.getResourceId(R.styleable.QuickIndexView_indexSelectedTextColor, - 1);
        Log.e(TAG, "QuickIndexView: indexSelectedTextColorId=" + indexSelectedTextColorId);
        if(indexTextColorId != - 1) {
            mIndexSelectedTextColor = getResources().getColor(indexSelectedTextColorId);
        } else {
            mIndexSelectedTextColor = typedArray.getColor(R.styleable.QuickIndexView_indexSelectedTextColor, mIndexSelectedTextColor);
        }

        //中部toast宽度
        mToastWidth = typedArray.getDimension(R.styleable.QuickIndexView_toastWidth, mToastWidth);
        //中部toast宽度
        mToastHeight = typedArray.getDimension(R.styleable.QuickIndexView_toastHeight, mToastHeight);
        //中部toast字体大小
        mToastTextSize = typedArray.getDimension(R.styleable.QuickIndexView_toastTextSize, mToastTextSize);
        //中部toast字体颜色
        int toastTextColorId = typedArray.getResourceId(R.styleable.QuickIndexView_toastTextColor, - 1);
        if(toastTextColorId != - 1) {
            mToastTextColor = getResources().getColor(toastTextColorId);
        } else {
            mToastTextColor = typedArray.getColor(R.styleable.QuickIndexView_toastTextColor, mToastTextColor);
        }
        //中部toast背景
        int toastBackgroundId = typedArray.getResourceId(R.styleable.QuickIndexView_toastBackground, - 1);
        if(toastBackgroundId != - 1) {
            mToastBackground = getResources().getDrawable(toastBackgroundId);
        } else {
            mToastBackgroundColor = typedArray.getColor(R.styleable.QuickIndexView_toastBackground, mToastBackgroundColor);
        }

        typedArray.recycle();
    }


    /**
     * 初始化View
     */
    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        //右侧IndexView
        mIndexView.setIndexTextSize(mIndexTextSize);
        mIndexView.setIndexTextColor(mIndexTextColor);
        mIndexView.setIndexSelectedTextColor(mIndexSelectedTextColor);

        //中部toast
        Log.e(TAG, "initView: mToastWidth="+mToastWidth+" ,mToastHeight="+mToastHeight );
        ViewGroup.LayoutParams toastParams = mToastView.getLayoutParams();
        toastParams.width = (int) mToastWidth;
        toastParams.height = (int) mToastHeight;
        mToastView.setLayoutParams(toastParams);
        mToastView.requestLayout();

        mToastView.setTextSize(mToastTextSize);
        mToastView.setTextColor(mToastTextColor);
        if(mToastBackground != null) {
            mToastView.setBackground(mToastBackground);
        } else {
            mToastView.setBackgroundColor(mToastBackgroundColor);
        }
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
        layoutManager.scrollToPositionWithOffset(bean.position, 0);
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
