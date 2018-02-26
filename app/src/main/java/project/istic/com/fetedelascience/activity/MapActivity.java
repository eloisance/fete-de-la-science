package project.istic.com.fetedelascience.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import butterknife.BindView;
import project.istic.com.fetedelascience.MapItem;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.model.Event;

/**
 * Created by jnsll on 21/02/18.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, ClusterManager.OnClusterClickListener<MapItem>, ClusterManager.OnClusterItemClickListener<MapItem>, ClusterManager.OnClusterInfoWindowClickListener<MapItem>, ClusterManager.OnClusterItemInfoWindowClickListener<MapItem> {

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    List<Event> events;

    // Declare a variable for the cluster manager.
    private ClusterManager<MapItem> mClusterManager;
    private MarkerManager markerManager;


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
        this.mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        this.mapFragment.getMapAsync(this);

        // Get events
        DBManager.init(this);
        DBManager manager = DBManager.getInstance();
        events = manager.getAllEvents();
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
        this.mMap = googleMap;
        this.markerManager= new MarkerManager(this.mMap);
        this.mMap.getUiSettings().setZoomControlsEnabled(true);
        this.mMap.getUiSettings().setMyLocationButtonEnabled(true);

        setUpClusterer(this.mMap);
        for (Event event : events) {
            Double lat = event.getLatitude();
            Double longitude = event.getLongitude();
            String title = event.getTitle();
            LatLng atelier = null;
            //setUpClusterer(googleMap);
            if (lat != null && longitude != null) {
                atelier = new LatLng(lat, longitude);
                this.mMap.moveCamera(CameraUpdateFactory.newLatLng(atelier));
                MapItem item = new MapItem(lat, longitude, title, event, event.getDescription());
                this.mClusterManager.addItem(item);
                //this.mClusterManager.cluster();
            }
        }
    }

    private void setUpClusterer(GoogleMap googleMap) {
//        // Position the map.
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.859489,2.320582), 20));
//
//        // Initialize the manager with the context and the map.
//        // (Activity extends context, so we can pass 'this' in the constructor.)
        this.mClusterManager = new ClusterManager<MapItem>(this, googleMap, this.markerManager);
//
//        // Point the map's listeners at the listeners implemented by the cluster
//        // manager.
        googleMap.setOnCameraIdleListener(this.mClusterManager);
        googleMap.setOnMarkerClickListener(this.mClusterManager);
        this.mClusterManager.setOnClusterClickListener(this);
        this.mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForClusters());
        this.mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        this.mClusterManager.setOnClusterInfoWindowClickListener(this);
        googleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        googleMap.setOnInfoWindowClickListener(this.mClusterManager);



//        googleMap.setOnMapClickListener(this);
    }


    @Override
    public boolean onClusterClick(Cluster<MapItem> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String title = "";
                //= cluster.getItems().iterator().next().getTitle();
        //Log.d("CLUSTER", title); //String title =
        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            Log.d("ITEM", item.getTitle());
            title += item.getTitle() + "\n";
            builder.include(item.getPosition());
        }
        Toast.makeText(this, "Nombre d'ateliers : " + cluster.getSize() + "\n" + title + "", Toast.LENGTH_SHORT).show();
        // Get the LatLngBounds
        //final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
//        try {
//            this.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return true;
    }


    @Override
    public void onClusterInfoWindowClick(Cluster<MapItem> cluster) {
        // Does nothing, but you could go to a list of the users.
        Log.d("trucWind", "taille: " + cluster.getSize());
    }

    @Override
    public boolean onClusterItemClick(MapItem item) {
        // Does nothing, but you could go into the user's profile page, for example.
        Log.d("TRUCITEM", item.getEvent().getTitle());
//        Intent intent = new Intent(this, DetailEventActivity.class);
//        intent.putExtra("event", item.getEvent());
//        this.startActivity(intent);
        return false;
    }


    @Override
    public void onClusterItemInfoWindowClick(MapItem item) {
        // Does nothing, but you could go into the user's profile page, for example.
        Log.d("WINDOWS", item.getEvent().getDescription());
        //this.mClusterManager.getMarkerManager().
        Intent intent = new Intent(this, DetailEventActivity.class);
        intent.putExtra("event", item.getEvent());
        this.startActivity(intent);
    }

    public class MyCustomAdapterForClusters implements GoogleMap.InfoWindowAdapter {

        //@BindView(R.id.info)
        private final View myContentsView;

        @BindView(R.id.txtTitle) TextView title;
        @BindView(R.id.txtSnippet) TextView snippet;


        MyCustomAdapterForClusters() {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.info_windows, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub


//            TextView tvTitle = ((TextView) myContentsView
//                    .findViewById(R.id.txtTitle));
//            TextView tvSnippet = ((TextView) myContentsView
//                    .findViewById(R.id.txtSnippet));
            title.setText(marker.getTitle());
            snippet.setText(marker.getSnippet());
            snippet.setVisibility(View.GONE);

//            if (clickedCluster != null) {
//                tvTitle.setText(String
//                        .valueOf(clickedCluster.getItems().size())
//                        + " more offers available");
//            }
            return myContentsView;
        }
    }


}
