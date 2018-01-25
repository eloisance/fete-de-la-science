package project.istic.com.fetedelascience.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aferey on 25/01/18.
 */
@IgnoreExtraProperties
public class Route implements Serializable {

    String id;
    List<String> listEvent;

    public Route() {
    }

    public Route(String id, List<String> listEvent) {
        this.id = id;
        this.listEvent = listEvent;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("listEvent", listEvent);
        return result;
    }

    public String getId() {
        return id;
    }

    public List<String> getListEvent() {
        return listEvent;
    }
}
