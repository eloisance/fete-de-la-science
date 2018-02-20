package project.istic.com.fetedelascience.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.fragment.ListviewFragment;
import project.istic.com.fetedelascience.global.PrefManager;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.model.Event;
import project.istic.com.fetedelascience.model.Route;
import project.istic.com.fetedelascience.task.DataAsyncTask;

public class MainActivity extends AppCompatActivity {

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            DBManager.init(this);
            DBManager manager = DBManager.getInstance();

            DataAsyncTask task = new DataAsyncTask(getApplicationContext(), manager);
            task.execute();

            List<Event> events = manager.getAllEvents();

            if (events.size() > 0) {
                System.out.println("total: " + events.size());
            } else {
                System.out.println("debug: empty");
            }

            prefManager.setFirstTimeLaunchToFalse();
        }

        Fragment listFragment = new ListviewFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, listFragment)
                .commit();
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

}
