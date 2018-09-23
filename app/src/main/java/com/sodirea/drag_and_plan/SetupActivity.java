package com.sodirea.drag_and_plan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        LinearLayout.LayoutParams layout_param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.fill_parent,
                height * 2);
        mLayout = (LinearLayout) findViewById(R.id.layout_menu);
        mLayout.setLayoutParams(layout_param);
    }

    public void onClick(View v) {

    }

    public void toLogin(View v) {
        Intent redir = new Intent(SetupActivity.this, LoginActivity.class);
        startActivity(redir);
    }
}
