package com.pushi.pushi08.list;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.pushi.pushi08.R;

public class MainActivity extends Activity{

	private ListView listView;
	private List<String> list;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        list = getData();
        //new Timer().schedule(new TimeTaskScroll(this, listView,list), 20, 20);
    }

    public List<String> getData(){
    	List<String> list =  new ArrayList<String>();
    	for (int i = 0; i < 10; i++) {
			list.add("textview---------"+i);
		}
    	return list;
    }

}
