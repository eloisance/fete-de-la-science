package project.istic.com.fetedelascience.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.adapter.MapEventRecyclerViewAdapter;
import project.istic.com.fetedelascience.fragment.EventListviewFragment;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.model.Event;

public class MapListClusterActivity extends AppCompatActivity {

    @BindView(R.id.map_listevent)
    RecyclerView listEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_listevent);

        ButterKnife.bind(this);

        if(getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ArrayList<Event> events = this.getIntent().getParcelableArrayListExtra("events");
        for(int i = 0 ; i<events.size();i++){
            if(events.get(i).getVille() != null){
                setTitle(events.get(i).getVille());
                i = events.size();
            }
        }

        if(events == null ) {
            events = new ArrayList<>();
            setTitle("How ??");
        }

        listEvent.setHasFixedSize(true);
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        listEvent.setLayoutManager(mManager);

        MapEventRecyclerViewAdapter mapEventRecyclerViewAdapter = new MapEventRecyclerViewAdapter(this,events);
        this.listEvent.setAdapter(mapEventRecyclerViewAdapter);
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



}
