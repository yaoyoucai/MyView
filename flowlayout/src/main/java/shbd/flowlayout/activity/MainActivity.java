package shbd.flowlayout.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import shbd.flowlayout.R;
import shbd.flowlayout.view.FlowLayout;

public class MainActivity extends AppCompatActivity {
    private List<String> mDatas = Arrays.asList("LinearLayout", "MainActivity", "Service", "GridLayout", "ContentProvider", "BrocastReceiver", "android interface definition language"
            , "setContentView", "savedInstanceState");
    private FlowLayout mFlowLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlowLayout= (FlowLayout) findViewById(R.id.id_flowLayout);
        initData();
    }

    private void initData() {
        for (int i = 0; i < mDatas.size(); i++) {
            View view= (View) View.inflate(getApplicationContext(),R.layout.item_text,null);
            TextView textView= (TextView) view.findViewById(R.id.textview);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            textView.setText(mDatas.get(i));
            mFlowLayout.addView(view,params);
            Log.e("tag", "initData: ");
        }
    }
}
