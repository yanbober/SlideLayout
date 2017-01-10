package cn.yan.slidelayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.yan.library.MiniSlideRightLayout;
import cn.yan.library.SlideLayout;

/**
 * Created by yan on 17-1-8.
 */

public class DemoListAdapter extends BaseAdapter {
    private List<String> mListData;

    public DemoListAdapter() {
        mListData = new ArrayList<>();
        mListData.add("AbsListView 测试案例");
        mListData.add("AbsListView 快来试试");
        mListData.add("AbsListView 不服来一局");
        mListData.add("AbsListView 来呀来呀，来呀");
        mListData.add("AbsListView 我了个去");
        mListData.add("AbsListView 山炮玩意儿");
        mListData.add("AbsListView 支持是一种鼓励");
        mListData.add("AbsListView 坚持");
        mListData.add("AbsListView 加油");
        mListData.add("AbsListView 哈哈，我去");
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public String getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        //TODO
        final SlideLayout itemView = (SlideLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demo_list, parent, false);
        TextView content = (TextView) itemView.findViewById(R.id.content_id);
        TextView slide = (TextView) itemView.findViewById(R.id.slide_id);
        content.setText(mListData.get(position));

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemView.getSlideState() == MiniSlideRightLayout.STATE_OPEN) {
                    itemView.smoothCloseSlide();
                } else {
                    Toast.makeText(parent.getContext().getApplicationContext(), mListData.get(position), Toast.LENGTH_SHORT).show();
                }
            }
        });
        slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(parent.getContext().getApplicationContext(), "Slide Clicked!!!!", Toast.LENGTH_SHORT).show();
            }
        });
        return itemView;
    }
}
