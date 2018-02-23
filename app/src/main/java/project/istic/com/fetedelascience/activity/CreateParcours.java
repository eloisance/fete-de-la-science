package project.istic.com.fetedelascience.activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.adapter.MyEventRecyclerViewAdapter;
import project.istic.com.fetedelascience.adapter.ParcoursEventAddRecyclerViewAdapter;
import project.istic.com.fetedelascience.adapter.ParcoursEventRecyclerViewAdapter;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.model.Event;
import project.istic.com.fetedelascience.model.Parcours;
import project.istic.com.fetedelascience.util.UIHelper;

public class CreateParcours extends AppCompatActivity {

    @BindView(R.id.search_parcours)
    TextView search;

    @BindView(R.id.valider_parcours)
    Button valider;

    @BindView(R.id.create_parcours_list_event)
    RecyclerView listEvent;

    private ParcoursEventRecyclerViewAdapter mAdapterEvent;
    private ParcoursEventAddRecyclerViewAdapter mAdapterParcours;

    @BindView(R.id.create_parcours_list_add)
    RecyclerView listAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parcours);
        ButterKnife.bind(this);

        setTitle("Création de parcours");
        if(getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        search.setFocusable(false);
        listEvent.setHasFixedSize(true);
        listAddEvent.setHasFixedSize(true);


        LinearLayoutManager mManager1 = new LinearLayoutManager(this);
        LinearLayoutManager mManager2 = new LinearLayoutManager(this);
        listEvent.setLayoutManager(mManager1);
        listAddEvent.setLayoutManager(mManager2);
        DBManager manager = DBManager.getInstance();
        final Dao<Event, Integer> daoEvent = manager.getHelper().getEventDAO();
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
        mAdapterEvent = new ParcoursEventRecyclerViewAdapter(this, cursor, preparedQuery);
        listEvent.setAdapter(mAdapterEvent);

        mAdapterParcours = new ParcoursEventAddRecyclerViewAdapter();
        listAddEvent.setAdapter(mAdapterParcours);

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAdapterParcours.getParcours().size()!=0) {
                    enterName();
                } else {
                    UIHelper.showSnackbar(findViewById(android.R.id.content), getApplicationContext(), getString(R.string.text_popup_error), "OK");
                }
            }
        });
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

    public void addEventToParcours(Event event){
        this.mAdapterParcours.addEvent(event);
    }


    public void enterName(){
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.popup_name_parcours, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        (dialog, id) -> {
                            // get user input and set it to result
                            // edit text
                            String idUser = Settings.Secure.getString(getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("parcours");
                            ArrayList<Integer> idEvent = new ArrayList<>();
                            for (Event event : mAdapterParcours.getParcours()){
                                idEvent.add(event.getId());
                            }

                            myRef.push().setValue(new Parcours(userInput.getText().toString(),idEvent,idUser));
                            finish();

                        }).setNegativeButton("Cancel",
                (dialog, id) -> dialog.cancel());


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
