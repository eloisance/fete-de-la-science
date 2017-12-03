package project.istic.com.fetedelascience.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import project.istic.com.fetedelascience.global.Constants;
import project.istic.com.fetedelascience.model.Event;

public class SQLiteHelper extends OrmLiteSqliteOpenHelper {

    public SQLiteHelper(Context context) {
        super(context, Constants.SQLITE_DATABASE_NAME, null, Constants.SQLITE_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Event.class);
            System.out.println("SQLiteHelper onCreate done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Event.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("SQLiteHelper onUpgrade done from " + oldVersion + " to " + newVersion);
    }

    private Dao<Event, Integer> eventDAO = null;
    public Dao<Event, Integer> getEventDAO() {
        if (eventDAO == null) {
            try {
                eventDAO = getDao(Event.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return eventDAO;
    }
}
