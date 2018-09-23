package com.sodirea.drag_and_plan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SetupActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        db = FirebaseFirestore.getInstance();
    }

    public void toMain(View v) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        EditText nameText = (EditText) findViewById(R.id.nameText);
        String[] name = nameText.getText().toString().split(" ");
//        TextView profileName = (TextView) findViewById(R.id.NameField);
        TextView education = (TextView) findViewById(R.id.EduField);
        String edValue = education.getText().toString();
        EditText interests = (EditText) findViewById(R.id.InterestField);
        String[] interest = interests.getText().toString().split(" ");

        Map<String, Object> user = new HashMap<>();
        user.put("first", name[0]);
        user.put("last", name[1]);
        user.put("education", edValue);
        for (int i = 1; i < interest.length; ++i) {
            user.put("interest" + i, interest[i]);
        }
        user.put("key", currentUser.getUid());

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("tag", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("tag", "Error adding document", e);
                    }
                });

        Intent redir = new Intent(SetupActivity.this, MainActivity.class);
        startActivity(redir);
    }
}
