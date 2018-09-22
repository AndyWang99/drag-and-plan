package com.sodirea.drag_and_plan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }

    public void onClick(View v) {
        Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        EditText groupSizeText = (EditText) findViewById(R.id.groupSize);
        int amntOfPpl = Integer.parseInt(groupSizeText.toString());
        String category = categorySpinner.getSelectedItem().toString();

    }
}
