package project.istic.com.fetedelascience.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.adapter.FilteredCursor;
import project.istic.com.fetedelascience.adapter.FilteredCursorFactory;
import project.istic.com.fetedelascience.adapter.MyEventRecyclerViewAdapter;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.model.Event;

public class EventListviewFragment extends Fragment {

    private MyEventRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mManager;
    private RecyclerView mRecycler;
    /**
     * Popup filter
     */
    private AlertDialog alertDialogFilter;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listevent, container, false);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_event);
        mRecycler.setHasFixedSize(true);

        mManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mManager);

        alertDialogFilter = createPopupFilter();


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

        inflater.inflate(R.menu.list_event_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchView =  menu.findItem(R.id.action_search);
        searchView.setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                alertDialogFilter.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    public AlertDialog createPopupFilter(){
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.popup_filter_event, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( getContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText title = promptsView
                .findViewById(R.id.input_filter_title);
        final EditText ville = promptsView
                .findViewById(R.id.input_filter_ville);
        final EditText date = promptsView
                .findViewById(R.id.input_filter_date);

        final CheckBox titleBox = promptsView
                .findViewById(R.id.checkBoxTitle);
        final CheckBox villeBox = promptsView
                .findViewById(R.id.checkBoxVille);
        final CheckBox dateBox = promptsView
                .findViewById(R.id.checkBoxDate);

        // set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        (dialog, id) -> {


                            FilteredCursor filtered = FilteredCursorFactory.createUsingSelector(mAdapter.getCursorOriginal(), new FilteredCursorFactory.Selector() {
                                @Override
                                public boolean select(Cursor cursor) {
                                    boolean filter = true;
                                    if(titleBox.isChecked()){
                                        filter = filter && filter(cursor,Event.TITLE_FIELD_NAME,title.getText().toString());
                                    }

                                    if(villeBox.isChecked()){
                                        filter = filter && filter(cursor,Event.VILLE_FIELD_NAME,ville.getText().toString());
                                    }

                                    if(dateBox.isChecked()){
                                        filter = filter && filter(cursor,Event.DATE_FIELD_NAME,date.getText().toString());
                                    }


                                    return filter;
                                }
                            });
                            mAdapter.swapCursor(filtered);



                        }).setNegativeButton("Pas de filtre",
                (dialog, id) -> {
                    mAdapter.swapCursor(mAdapter.getCursorOriginal());
                    dialog.cancel();

                                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
       return alertDialog;
    }

    private boolean filter(Cursor cursor,String nameColumn,String query){
        int nameIndex = -1;
        if (nameIndex == -1) {
            nameIndex = cursor.getColumnIndex(nameColumn);

        }
        if(query == null || query.equals("")) {
            return true;
        }

        if (cursor.getString(nameIndex) != null && cursor.getString(nameIndex).toLowerCase().contains(query.toLowerCase())) {
            return true;
        } else {
            return false;
        }

    }


}
