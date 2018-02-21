package project.istic.com.fetedelascience.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.model.Event;

/**
 * Created by jnsll on 21/02/18.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    //@BindView(R.id.map) SupportMapFragment map;
    List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setTitle("Maps");
        if (getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get events
        DBManager.init(this);
        DBManager manager = DBManager.getInstance();
        events = manager.getAllEvents();
        Log.d("truc", events.get(0).toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        for (Event event : events) {
            Double lat = event.getLatitude();
            Double longitude = event.getLongitude();
            String title = event.getTitle();
            LatLng atelier = null;
            if (lat != null && longitude != null) {
                atelier = new LatLng(lat, longitude);
                googleMap.addMarker(new MarkerOptions().position(atelier)
                        .title(title));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(atelier));
            }

        }
//        LatLng sydney = new LatLng(-33.852, 151.211);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("truc"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
