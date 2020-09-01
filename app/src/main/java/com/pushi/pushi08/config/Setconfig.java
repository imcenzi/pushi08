package com.pushi.pushi08.config;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.pushi.pushi08.R;

public class Setconfig extends Activity implements View.OnClickListener{
    private String DATABASE_NAME = "notebook.db";
    private String TABLE_NAME = "info";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setconfig);


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
