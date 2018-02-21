package project.istic.com.fetedelascience;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by jnsll on 21/02/18.
 */

public class MapItem implements ClusterItem {

    private final LatLng mPosition;
    private String mTitle;
    private String mSnippet;


    public MapItem(double lat, double lng) {

        this.mPosition = new LatLng(lat, lng);
    }

    public MapItem(double lat, double lng, String title, String snippet) {
        this.mPosition = new LatLng(lat, lng);
        this.mTitle = title;
        this.mSnippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }
}
