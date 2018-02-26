package project.istic.com.fetedelascience.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import project.istic.com.fetedelascience.model.Event;

/**
 * Created by jnsll on 21/02/18.
 */

public class MapItem implements ClusterItem {

    private final LatLng mPosition;
    private String mTitle;
    private Event mEvent;
    private String mSnippet;


    public MapItem(double lat, double lng) {

        this.mPosition = new LatLng(lat, lng);
    }

    public MapItem(double lat, double lng, String title, Event event, String snippet) {
        this.mPosition = new LatLng(lat, lng);
        this.mTitle = title;
        this.mEvent = event;
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

    public Event getEvent() { return mEvent; }
    
}
