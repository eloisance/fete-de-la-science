package project.istic.com.fetedelascience.helper;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import project.istic.com.fetedelascience.global.Constants;
import project.istic.com.fetedelascience.model.Event;

public class DBManager {

    private SQLiteHelper helper;
    private static DBManager instance;

    public static void init(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
    }

    public static DBManager getInstance() {
        return instance;
    }

    private DBManager(Context context) {
        helper = new SQLiteHelper(context);
    }

    private SQLiteHelper getHelper() {
        return helper;
    }


    /** Methods [Event] **/

    public List<Event> getAllEvents() {
        try {
            return getHelper().getEventDAO().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Event>();
        }
    }

    public void createEvent(Event event) {
        try {
            getHelper().getEventDAO().create(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}