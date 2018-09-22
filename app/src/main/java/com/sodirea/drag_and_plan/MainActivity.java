package com.sodirea.drag_and_plan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.onCreate(savedInstanceState);

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                mMap.getUiSettings().setAllGesturesEnabled(false);
                // drawerLayout.bringToFront(); // less chunky option
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                mMap.getUiSettings().setAllGesturesEnabled(true);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        Button setNewPlan = (Button) findViewById(R.id.setPlan);
        setNewPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) // if they haven't given permission to use location yet, prompt and check if they gave it again
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast toast = Toast.makeText(MainActivity.this, "Could not get location", Toast.LENGTH_LONG);
            toast.show();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mapFragment.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragment.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapFragment.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapFragment.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }
}
