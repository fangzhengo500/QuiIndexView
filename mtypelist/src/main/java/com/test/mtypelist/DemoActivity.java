package com.test.mtypelist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.test.mtypelist.view.QuickIndexView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * @project： ListDemo
 * @package： com.test.mtypelist
 * @class: DemoActivity
 * @author: 陆伟
 * @Date: 2017/2/15 10:01
 * @desc： TODO
 */
public class DemoActivity extends AppCompatActivity {
    private static final String TAG = "DemoActivity";
    private Context mActivityContext;
    private QuickIndexView mQuickIndexView;
    private List<ItemBean> mDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityContext = this;
        setContentView(R.layout.activity_demo);

        Random random = new Random();

        mDatas = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            int itemType = random.nextInt(27);
            String str = String.valueOf((char) ('A' + itemType));
            mDatas.add(new ItemBean(itemType, str + "_demo_" + i));
        }
        Collections.sort(mDatas, new Comparator<ItemBean>() {
            @Override
            public int compare(ItemBean o1, ItemBean o2) {
                int result = o1.itemType - o2.itemType;
                if(result == 0) result = 1;
                return result;
            }
        });

        mQuickIndexView = (QuickIndexView) findViewById(R.id.quickIndexView);
        mQuickIndexView.setAdapter(new MyAdapter(mDatas));
    }

    public class MyAdapter extends QuickIndexView.Adapter<MyAdapter.ViewHolder> {

        private List<ItemBean> mDatas;

        public MyAdapter(List<ItemBean> datas) {
            mDatas = datas;
        }


        @Override
        protected void onBindViewDatas(ViewHolder holder, int position) {
            ItemBean itemBean = getItemBean(position);
            if(itemBean.itemType % 2 == 0) {
                holder.icon.setImageResource(R.mipmap.ic_launcher);
            } else {
                holder.icon.setImageResource(R.mipmap.house);
            }

            holder.text.setText(itemBean.str);
        }

        @Override
        public View getItemView() {
            return View.inflate(DemoActivity.this, R.layout.demo_item, null);
        }

        @Override
        protected ViewHolder getViewHolder(View itemView) {
            return new ViewHolder(itemView);
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        @Override
        protected String getTitle(int itemViewType) {
            return String.valueOf((char) ('A' + itemViewType));
        }

        @Override
        public int getItemViewType(int position) {
            return getItemBean(position).itemType;
        }

        private ItemBean getItemBean(int position) {
            return mDatas.get(position);
        }

        public class ViewHolder extends QuickIndexView.ViewHolder {
            ImageView icon;
            TextView text;

            public ViewHolder(View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.icon);
                text = (TextView) itemView.findViewById(R.id.text);
            }
        }
    }
}
