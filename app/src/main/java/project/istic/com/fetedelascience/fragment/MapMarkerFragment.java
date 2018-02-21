//package project.istic.com.fetedelascience.fragment;
//
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import butterknife.BindView;
//import project.istic.com.fetedelascience.R;
//
///**
// * Created by jnsll on 21/02/18.
// */
//
//public class MapMarkerFragment extends Fragment implements OnMapReadyCallback {
//
//    @BindView(R.id.map) SupportMapFragment map;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //setHasOptionsMenu(true);
////        setContentView(R.layout.fragment_map);
//
////        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
////                .findFragmentById(R.id.map);
//        map.getMapAsync(this);
//
//        //map.getMapAsync(this);
//    }
//
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
////        View rootView = inflater.inflate(R.layout.fragment_listview, container, false);
////
////        ButterKnife.bind(this, rootView);
////
////
////        return rootView;
////    }
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        // Add a marker in Sydney, Australia,
//        // and move the map's camera to the same location.
//        LatLng sydney = new LatLng(-33.852, 151.211);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
//
//}
