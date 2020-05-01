package lcwu.fyp.asistio.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.LastLocation;
import lcwu.fyp.asistio.model.User;

public class ViewLastLocation extends AppCompatActivity {
    private MapView map;
    private GoogleMap googleMap;
    private Helpers helpers;
    private User user;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Location");
    private ValueEventListener listener;
    private List<LastLocation> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_last_location);

        helpers = new Helpers();
        Session session = new Session(getApplicationContext());
        user = session.getUser();
        locations = new ArrayList<>();
        // Initialize Map View
        map = findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(ViewLastLocation.this);
            map.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap gM) {
                    Log.e("Maps", "Call back received");
                    googleMap = gM;
                    LatLng defaultPosition = new LatLng(31.5204, 74.3487); // Lahore coordinates
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(defaultPosition).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    loadLocations();
                }
            });
        } catch (Exception e) {
            helpers.showError(ViewLastLocation.this, "ERROR", "Something went wrong.\nPlease try again later.");
        }
    }

    private void loadLocations() {
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (listener != null)
                    reference.child(user.getId()).removeEventListener(listener);
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    LastLocation location = data.getValue(LastLocation.class);
                    if (location != null) {
                        locations.add(location);
                        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(latlng).title(location.getDatetime() + "\n" + location.getAddress())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
                        marker.showInfoWindow();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (listener != null)
                    reference.child(user.getId()).removeEventListener(listener);
                helpers.showError(ViewLastLocation.this, "ERROR", "Something went wrong.\nPlease try again later.");
            }
        };

        reference.child(user.getId()).addValueEventListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listener != null)
            reference.child(user.getId()).removeEventListener(listener);
        map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }
}
