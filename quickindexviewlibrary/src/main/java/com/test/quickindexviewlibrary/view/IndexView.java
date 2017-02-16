package com.test.quickindexviewlibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.test.quickindexviewlibrary.R;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @project： ChatDemo
 * @package： com.test.myapplication
 * @class: IndexView
 * @author: 陆伟
 * @Date: 2017/2/11 15:37
 * @desc： TODO
 */
public class IndexView extends View {

    private static final String TAG = "IndexView";
    private static final float TEXT_SIZE = 20;
    private static final int MAX_ROW_HEIGHT = 25;
    private static final int MIN_ROW_HEIGHT = 15;

    private Paint mPaint;

    private int mViewWidth;
    private int mViewHeight;

    private Map<Integer, Bean> mIndexMap = new TreeMap<>();

    private int mRowHeight = MIN_ROW_HEIGHT;
    private int mYOffset;

    private int mTouchRow = - 1;
    private float mIndexTextSize = TEXT_SIZE;
    private int mIndexTextColor;
    private int mIndexSelectedTextColor;

    public IndexView(Context context) {
        this(context, null);
    }

    public IndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();
    }


    private void initPaint() {
        mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mIndexTextSize);
        mPaint.setColor(0xff666666);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        mRowHeight = (int) mPaint.getTextSize();
        int heightSize = (mRowHeight + 2) * mIndexMap.size() + 20;

        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, heightSize);
        } else if(widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, heightSize);
        } else if(heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mIndexMap == null) {
            return;
        }

        mRowHeight = mViewHeight / mIndexMap.size();
        mRowHeight = mRowHeight > MAX_ROW_HEIGHT ? MAX_ROW_HEIGHT : mRowHeight;

        mYOffset = (mViewHeight - mIndexMap.size() * mRowHeight) / 2;

        Set<Map.Entry<Integer, Bean>> entries = mIndexMap.entrySet();
        int j = 0;
        for(Map.Entry<Integer, Bean> entry : entries) {
            if(j == mTouchRow) {
                mPaint.setColor(mIndexSelectedTextColor);
            } else {
                mPaint.setColor(mIndexTextColor);
            }
            canvas.drawText(entry.getValue().indexText, mViewWidth / 2, mYOffset + mRowHeight * (1 + j), mPaint);
            j++;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(mIndexMap == null) {
            return false;
        }

        float y = event.getY();
        int row = (int) ((y - mYOffset) / mRowHeight);

        int indexType = getIndexBean(row);

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.setBackgroundResource(R.drawable.shape_gray_bg);
                mTouchRow = row;
                if(mIndexListener != null) {
                    mIndexListener.onIndex(indexType);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                mTouchRow = row;
                invalidate();
                if(mIndexListener != null) {
                    mIndexListener.onIndex(indexType);
                }
                break;

            case MotionEvent.ACTION_UP:
                this.setBackground(null);
                mTouchRow = - 1;
                if(mIndexListener != null) {
                    mIndexListener.onfinishIndex();
                }
                break;
        }
        return true;
    }


    private int getIndexBean(int row) {
        if(mIndexMap == null) {
            return - 1;
        }

        if(row < 0) {
            row = 0;
        } else if(row >= mIndexMap.size()) {
            row = mIndexMap.size() - 1;
        }

        int i = 0;
        Set<Map.Entry<Integer, Bean>> entries = mIndexMap.entrySet();
        for(Map.Entry<Integer, Bean> entry : entries) {
            if(i == row)
                return entry.getKey();
            i++;
        }
        return - 1;
    }

    private IndexListener mIndexListener;

    public void initIndexMap(TreeMap<Integer, Bean> indexMap) {
        if(indexMap == null) {
            throw new RuntimeException("indexMap can not null");
        }
        mIndexMap = indexMap;
        requestLayout();
    }

    public void setIndexTextSize(float indexTextSize) {
        mIndexTextSize = indexTextSize;
        mPaint.setTextSize(mIndexTextSize);

    }

    public void setIndexTextColor(int indexTextColor) {
        mIndexTextColor = indexTextColor;
    }

    public void setIndexSelectedTextColor(int indexSelectedTextColor) {
        mIndexSelectedTextColor = indexSelectedTextColor;
    }


    public interface IndexListener {
        void onIndex(int indexType);

        void onfinishIndex();
    }

    public void setOnIndexListener(IndexListener listener) {
        mIndexListener = listener;
    }

    public static class Bean {
        public int position;
        public String indexText;

        public Bean(int position, String indexText) {
            this.position = position;
            this.indexText = indexText;
        }
    }
}
