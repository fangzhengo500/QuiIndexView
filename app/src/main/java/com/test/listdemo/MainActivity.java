package com.test.listdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.quickindexviewlibrary.view.QuickIndexView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private QuickIndexView mQuickIndexView;
    private List<ItemBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDatas();
        mQuickIndexView = (QuickIndexView) findViewById(R.id.quickIndexView);
        mQuickIndexView.setAdapter(new MyAdapter(mDatas));
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < 100; i++) {
            int type = random.nextInt(27);
            String name = ((char) (type + 'A')) + "_name_" + i;
            String phone = random.nextInt(10) + "1005684";
            mDatas.add(new ItemBean(type, name, phone));
        }

        Collections.sort(mDatas, new Comparator<ItemBean>() {
            @Override
            public int compare(ItemBean o1, ItemBean o2) {
                int result = o1.type - o2.type;
                if(result == 0) result = 1;
                return result;
            }
        });
    }

    class MyAdapter extends QuickIndexView.Adapter<MyAdapter.ViewHolder> {

        List<ItemBean> mDatas;

        public MyAdapter(List<ItemBean> datas) {
            mDatas = datas;
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).type;
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }


        @Override
        public View getItemView() {
            return View.inflate(MainActivity.this, R.layout.item_view, null);
        }

        @Override
        protected ViewHolder getViewHolder(View itemView) {
            return new ViewHolder(itemView);
        }

        @Override
        protected String getTitle(int itemViewType) {
            char indexChar = (char) ('A' + itemViewType);
            if(indexChar < 'A' || indexChar > 'Z') {
                indexChar = '#';
            }
            return String.valueOf(indexChar);
        }

        @Override
        protected void onBindViewDatas(ViewHolder holder, int position) {
            ItemBean itemBean = mDatas.get(position);
            if(itemBean.type % 2 == 0) {
                holder.icon.setImageResource(R.mipmap.ic_launcher);
            } else {
                holder.icon.setImageResource(R.mipmap.house);
            }

            holder.name.setText(itemBean.name);
            holder.phone.setText(itemBean.phone);
        }

        public class ViewHolder extends QuickIndexView.ViewHolder {
            ImageView icon;
            TextView name;
            TextView phone;

            public ViewHolder(View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.icon);
                name = (TextView) itemView.findViewById(R.id.name);
                phone = (TextView) itemView.findViewById(R.id.phone);
            }
        }
    }
}
