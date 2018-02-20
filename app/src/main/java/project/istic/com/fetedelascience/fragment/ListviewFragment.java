package project.istic.com.fetedelascience.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.support.OrmLiteCursorLoader;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.helper.SQLiteHelper;
import project.istic.com.fetedelascience.model.Event;

public class ListviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static int LOADER_ID = 0;

    @BindView(R.id.txtView) TextView txtView;

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

        ButterKnife.bind(this, rootView);
        txtView.setText("Test");

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final SQLiteHelper helper = OpenHelperManager.getHelper(getActivity(), SQLiteHelper.class);
        try {
            eventDao = helper.getDao(Event.class);
            preparedQuery = eventDao.queryBuilder().prepare();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new OrmLiteCursorLoader<Event>(getActivity(), eventDao, preparedQuery);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
