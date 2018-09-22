package com.sodirea.drag_and_plan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void cancelOnClick(View v) {
        Intent back = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(back);
    }
}
