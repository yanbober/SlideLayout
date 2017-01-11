package cn.yan.slidelayout;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by yan on 17-1-8.
 */

public class DemoPageAdapter extends PagerAdapter {
    private List<View> mListData;
    private int[] mColors = {Color.GREEN, Color.YELLOW, Color.RED};

    public DemoPageAdapter(Context context) {
        mListData = new ArrayList<>();
        for (int index=0; index<3; index++) {
            View page = LayoutInflater.from(context).inflate(R.layout.item_demo_page, null);
            mListData.add(page);
        }
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        container.removeView(mListData.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView view = (TextView) mListData.get(position).findViewById(R.id.content_id);
        view.setText("ViewPager"+position);
        view.setBackgroundColor(mColors[position%mColors.length]);
        container.addView(mListData.get(position), container.getLayoutParams());
        return mListData.get(position);
    }
}
