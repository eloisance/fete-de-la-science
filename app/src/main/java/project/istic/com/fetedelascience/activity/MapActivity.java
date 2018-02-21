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
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import project.istic.com.fetedelascience.MapItem;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.model.Event;

/**
 * Created by jnsll on 21/02/18.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    //@BindView(R.id.map) SupportMapFragment map;
    List<Event> events;

    // Declare a variable for the cluster manager.
    private ClusterManager<MapItem> mClusterManager;


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

        int i = 0;
        setUpClusterer(googleMap);
        for (Event event : events) {
            Double lat = event.getLatitude();
            Double longitude = event.getLongitude();
            String title = event.getTitle();
            LatLng atelier = null;
            //setUpClusterer(googleMap);
            if (lat != null && longitude != null) {
                atelier = new LatLng(lat, longitude);
//                googleMap.addMarker(new MarkerOptions().position(atelier)
//                        .title(title));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(atelier));
                MapItem item = new MapItem(lat, longitude, title, null);
                mClusterManager.addItem(item);
                //i++;
//                if (i == 1) {
//
//                    MapItem item = new MapItem(lat, longitude, title, null);
//                    mClusterManager.addItem(item);
//                } else
//                if (i < 10) {
//                    MapItem item = new MapItem(lat, longitude, title, null);
//                    mClusterManager.addItem(item);
//                } else {
//                    i = 0;
//                }
            }

        }
//        LatLng sydney = new LatLng(-33.852, 151.211);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("truc"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void setUpClusterer(GoogleMap googleMap) {
//        // Position the map.
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.859489,2.320582), 20));
//
//        // Initialize the manager with the context and the map.
//        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MapItem>(this, googleMap);
//
//        // Point the map's listeners at the listeners implemented by the cluster
//        // manager.
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
//
//        // Add cluster items (markers) to the cluster manager.
//        //addItems();
    }
//
//    private void addItems() {
//
//        // Set some lat/lng coordinates to start with.
//        double lat = 48.859489;
//        double lng = 2.320582;
//
//        // Add ten cluster items in close proximity, for purposes of this example.
//        for (int i = 0; i < 20; i++) {
//            double offset = i / 60d;
//            lat = lat + offset;
//            lng = lng + offset;
//            MapItem offsetItem = new MapItem(lat, lng);
//            mClusterManager.addItem(offsetItem);
//        }
//    }


}
