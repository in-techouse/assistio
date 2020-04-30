package lcwu.fyp.asistio.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.director.Helpers;

public class ViewLastLocation extends AppCompatActivity {
    private MapView map;
    private GoogleMap googleMap;
    private Helpers helpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_last_location);

        helpers = new Helpers();
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
                }
            });
        }
        catch (Exception e)
        {
            helpers.showError(ViewLastLocation.this, "ERROR", "Something went wrong.\nPlease try again later.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
