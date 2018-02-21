package project.istic.com.fetedelascience.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.adapter.MyEventRecyclerViewAdapter;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.model.Event;

public class ListviewFragment extends Fragment {

    private MyEventRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mManager;
    private RecyclerView mRecycler;

    private SearchView searchView;

    private Dao<Event, Long> eventDao;
    private PreparedQuery<Event> preparedQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listview, container, false);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_event);
        mRecycler.setHasFixedSize(true);

        mManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mManager);


        DBManager manager = DBManager.getInstance();
        Dao<Event, Integer> daoEvent = manager.getHelper().getEventDAO();

        QueryBuilder<Event, Integer> queryBuilder = daoEvent.queryBuilder();

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

        mAdapter = new MyEventRecyclerViewAdapter(getContext(), cursor, preparedQuery);
        mRecycler.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // listening to search query text change
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            default:
                break;
        }

        return false;
    }
}
