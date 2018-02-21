package project.istic.com.fetedelascience.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.istic.com.fetedelascience.R;

/**
 * Created by jnsll on 21/02/18.
 */

public class MapMarkerFragment extends Fragment {

    @BindView(R.id.map) SupportMapFragment map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listview, container, false);

        ButterKnife.bind(this, rootView);


        return rootView;
    }

}
