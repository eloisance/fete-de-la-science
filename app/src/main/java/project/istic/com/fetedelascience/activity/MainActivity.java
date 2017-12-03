package project.istic.com.fetedelascience.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.global.Constants;
import project.istic.com.fetedelascience.global.PrefManager;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.model.Event;

public class MainActivity extends AppCompatActivity {

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager = new PrefManager(this);

        if (prefManager.isFirstTimeLaunch()) {
            initDatabase();
            prefManager.setFirstTimeLaunchToFalse();
        }
    }

    /**
     * Only for the first launch
     */
    private void initDatabase() {
        DBManager.init(this);
        DBManager manager = DBManager.getInstance();

        readDataAndSave(manager);

        List<Event> events = manager.getAllEvents();

        if (events.size() > 0) {
            System.out.println("total: " + events.size());
        } else {
            System.out.println("debug: empty");
        }
    }

    /**
     * Create Event and save it in database
     * for each fields entry from json data file
     * @param manager for ORMSQLite
     */
    private void readDataAndSave(DBManager manager) {
        Gson gson = new GsonBuilder().create();
        JsonParser jsonParser = new JsonParser();
        StringBuilder sb = new StringBuilder();

        InputStream inputStream = getResources().openRawResource(R.raw.data_2017);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line, result;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();
            JSONArray json = new JSONArray(result);
            // For each item event, get fields item and create Event model
            // from it to create new entry in database
            for (int i = 0; i < json.length(); i++) {
                JSONObject one = json.getJSONObject(i);
                JSONObject fields = one.getJSONObject("fields");
                JsonElement item = jsonParser.parse(fields.toString());
                Event event = gson.fromJson(item, Event.class);
                System.out.println("event:" + event);
                manager.createEvent(event);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
