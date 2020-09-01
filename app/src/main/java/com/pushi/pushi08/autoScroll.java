package com.pushi.pushi08;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.pushi.pushi08.until.AutoScrollView;

public class autoScroll extends AppCompatActivity {

    TextView textView;
    AutoScrollView autoScrollView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autoscroll);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        textView = findViewById(R.id.textView);
        autoScrollView = findViewById(R.id.scrollView);

    }

    private void initData() {
        autoScrollView.setAutoToScroll(true);//设置可以自动滑动
        autoScrollView.setFistTimeScroll(2000);//设置第一次自动滑动的时间
        autoScrollView.setScrollRate(50);//设置滑动的速率
        autoScrollView.setScrollLoop(false);//设置是否循环滑动
    }

    // 监听是否达到头部或底部
    private void initEvent() {
        autoScrollView.setScanScrollChangedListener(new AutoScrollView.ISmartScrollChangedListener() {
            @Override
            public void onScrolledToBottom() {
                Toast.makeText(autoScroll.this, "底部", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScrolledToTop() {
                Toast.makeText(autoScroll.this, "顶部", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 给文本设置长字符串
    public void setLongText(View view) {
        textView.setText("1aaaaaaaaaaa\n2aaaaaaaaaaa\n3aaaaaaaaaaa\n4aaaaaaaaaaa\n5aaaaaaaaaaa\n" +
                "6aaaaaaaaaaa\n7aaaaaaaaaaa\n8aaaaaaaaaaa\n9aaaaaaaaaaa\n10aaaaaaaaaaa\n" +
                "11aaaaaaaaaaa\n12aaaaaaaaaaa\n13aaaaaaaaaaa\n14aaaaaaaaaaa\n15aaaaaaaaaaa");
    }

    // 给文本设置短字符串
    public void setShortText(View view) {
        textView.setText("1aaaaaaaaaaa\n2aaaaaaaaaaa");
    }

}


