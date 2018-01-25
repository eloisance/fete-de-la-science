package project.istic.com.fetedelascience.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.global.Constants;
import project.istic.com.fetedelascience.global.PrefManager;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.model.Event;
import project.istic.com.fetedelascience.model.Route;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.myTextView)
    TextView hello;

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Mandatory
        ButterKnife.bind(this);

        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("routes");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Route Route = postSnapshot.getValue(Route.class);
                    Log.i("Message",Route.getId());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Merde", "loadPost:onCancelled", databaseError.toException());
            }


        });

        */

        prefManager = new PrefManager(this);

        if (prefManager.isFirstTimeLaunch()) {
            initDatabase();
            prefManager.setFirstTimeLaunchToFalse();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_icon_map:
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
