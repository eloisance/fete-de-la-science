package project.istic.com.fetedelascience.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.adapter.MyEventRecyclerViewAdapter;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.model.Event;
import project.istic.com.fetedelascience.model.Parcours;


public class DetailParcoursActivity extends AppCompatActivity {


    @BindView(R.id.title_parcours)
    TextView title;

    @BindView(R.id.list_event_detail_parcours)
    RecyclerView mRecycler;

    private MyEventRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mManager;

    Parcours parcours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_parcours);
        ButterKnife.bind(this);


        setTitle("Détail Parcours");
        if(getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        parcours = this.getIntent().getParcelableExtra("parcours");
//        Picasso.with(getBaseContext()).load(this.event.getApercu()).into(imageEvent);
        if(parcours == null) {
            finish();
            return;
        }


        this.title.setText(parcours.getName());

        mRecycler.setHasFixedSize(true);

        mManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mManager);


        DBManager manager = DBManager.getInstance();
        Dao<Event, Integer> daoEvent = manager.getHelper().getEventDAO();
        Where<Event, Integer> queryBuilder = daoEvent.queryBuilder().where();
        List<Integer> listEvent = parcours.getListEvent();
        for(int i =0; i<listEvent.size();i++){
            try {
                if(i<listEvent.size()-1) {
                    queryBuilder = queryBuilder.eq(Event.ID_FIELD_NAME, listEvent.get(i)).or();
                } else {
                    queryBuilder = queryBuilder.eq(Event.ID_FIELD_NAME, listEvent.get(i));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        Cursor cursor = null;
        PreparedQuery<Event> preparedQuery = null;

        try {
            preparedQuery = queryBuilder.prepare();
            CloseableIterator<Event> iterator = daoEvent.iterator(preparedQuery);
            AndroidDatabaseResults results = (AndroidDatabaseResults) iterator.getRawResults();
            cursor = results.getRawCursor();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mAdapter = new MyEventRecyclerViewAdapter(this, cursor, preparedQuery);
        mRecycler.setAdapter(mAdapter);


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
