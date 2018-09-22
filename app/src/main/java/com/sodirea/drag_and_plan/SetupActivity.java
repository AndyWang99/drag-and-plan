package com.sodirea.drag_and_plan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SetupActivity extends AppCompatActivity {

    Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    public void onClick(View v) {
        EditText groupSizeText = (EditText) findViewById(R.id.groupSize);
        int amntOfPpl = Integer.parseInt(groupSizeText.getText().toString());
        String category = categorySpinner.getSelectedItem().toString();

        Intent intent = new Intent(SetupActivity.this, MainActivity.class);
        intent.putExtra("amntOfPpl", amntOfPpl);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
