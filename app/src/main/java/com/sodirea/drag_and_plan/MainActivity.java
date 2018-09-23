package com.sodirea.drag_and_plan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {//implements OnMapReadyCallback {

    private static final int NUM_PEOPLE = 2;

    private Socket socket;

    {
        try {
            socket = IO.socket("http://192.168.43.189:8080");
        } catch (URISyntaxException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView counter = findViewById(R.id.counter);
        final TextView message = findViewById(R.id.message);
        configureSocketEvents();
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(socket);
                if (!socket.connected()) { // if not connected, then connect and set text of button to cancel
                    socket.connect();
                    button.setText("Cancel");
                    message.setText("Finding...");
                } else {
                    socket.disconnect();
                    button.setText("Find People!");
                    counter.setText("");
                    message.setText("Name");
                }
            }
        });
    }

    public void configureSocketEvents() {
        final ConstraintLayout layout = findViewById(R.id.layout);
        final TextView counter = findViewById(R.id.counter);

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = new JSONObject();
                try {
                    data.put("interests", "BLAH");
                    data.put("latitude", 43.761539);
                    data.put("longitude", -79.411079);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("receieveProperties", data); // give server the client's properties
            }
        }).on("giveCurrentQueueSize", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                MainActivity.this.runOnUiThread(new Runnable() {                                    // run on UI thread so that views can be added or modified
                    @Override
                    public void run() {
                        int numPeople = (Integer) args[0];
                        counter.setText(numPeople+1 + "/" + NUM_PEOPLE);
                    }
                });
            }
        }).on("userModifiedQueue", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                MainActivity.this.runOnUiThread(new Runnable() {                                    // run on UI thread so that views can be added or modified
                    @Override
                    public void run() {
                        int numPeople = (Integer) args[0];
                        counter.setText(numPeople + "/" + NUM_PEOPLE);
                    }
                });
            }
        }).on("goToLocation", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                JSONObject data = (JSONObject) args[0];
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                try {
                    intent.putExtra("latitude", (double) data.get("latitude"));
                    intent.putExtra("longitude", (double) data.get("longitude"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
    }
    /*private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient mFusedLocationClient;

    private static final String GOOGLE_PLACES_API_KEY = "AIzaSyB4tCN4rAimWOBA4qXZhucLJhm6brcLKwg";

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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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
    public void onMapReady(final GoogleMap googleMap) {
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
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng yourLoc = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(yourLoc)
                                        .title("Your Location"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(yourLoc));
                            }
                        }
                    });
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
    }*/
}
