package cn.yan.slidelayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mContentText;
    private TextView mSlideText;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContentText = (TextView) this.findViewById(R.id.content_id);
        mSlideText = (TextView) this.findViewById(R.id.slide_id);

        mContentText.setOnClickListener(this);
//        mSlideText.setOnClickListener(this);

        mListView = (ListView) this.findViewById(R.id.listview_id);
        DemoListAdapter adapter = new DemoListAdapter();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Item Clicked!!!!", Toast.LENGTH_SHORT).show();
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Item long Clicked!!!!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_id:
                Toast.makeText(getApplicationContext(), "Content Clicked!!!!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.slide_id:
                Toast.makeText(getApplicationContext(), "Slide Clicked!!!!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
