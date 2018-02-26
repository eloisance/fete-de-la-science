package project.istic.com.fetedelascience.activity;

import android.database.Cursor;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.util.FilteredCursor;
import project.istic.com.fetedelascience.util.FilteredCursorFactory;
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

    /**
     * Liste de tous les events
     */
    @BindView(R.id.create_parcours_list_event)
    RecyclerView listEvent;

    /**
     * Liste d'Event ajouté au parcours
     */
    @BindView(R.id.create_parcours_list_add)
    RecyclerView listAddEvent;


    private ParcoursEventRecyclerViewAdapter mAdapterEvent;
    private ParcoursEventAddRecyclerViewAdapter mAdapterParcours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parcours);
        ButterKnife.bind(this);

        setTitle("Création de parcours");
        if(getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

        mAdapterParcours = new ParcoursEventAddRecyclerViewAdapter(this,listAddEvent);
        listAddEvent.setAdapter(mAdapterParcours);

    }



    @OnTextChanged(value = R.id.search_parcours,callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void  textChangedSearch(Editable editable) {
        if(editable == null || editable.toString().equals("")){
            resetResearch();
        }
    }

    @OnEditorAction(R.id.search_parcours)
    public boolean validerResearch(int actionId){
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            research(search.getText().toString());
            return true;
            }
        return  false;
    }

    @OnClick(R.id.valider_parcours)
    public void valider() {
        if(mAdapterParcours.getParcours().size()!=0) {
            enterName();
        } else {
            UIHelper.showSnackbar(findViewById(android.R.id.content), getApplicationContext(), getString(R.string.text_popup_error), "OK");
        }
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

    /**
     * Ajout un event à la liste de création de parcours
     * @param event
     */
    public void addEventToParcours(Event event){
        this.mAdapterParcours.addEvent(event);
    }

    /**
     *  Filtre sur le nom des events de la liste des events
     * @param query nom des events recherché
     */
    private void research(String query){
        FilteredCursor filtered = FilteredCursorFactory.createUsingSelector(mAdapterEvent.getCursorOriginal(), new FilteredCursorFactory.Selector() {
            int nameIndex = -1;

            @Override
            public boolean select(Cursor cursor) {

                if (nameIndex == -1) {
                    nameIndex = cursor.getColumnIndex(Event.TITLE_FIELD_NAME);

                }
                if(query == null || query.equals("")) {
                    return true;
                }

                if (cursor.getString(nameIndex).toLowerCase().contains(query.toLowerCase())) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        mAdapterEvent.swapCursor(filtered);
    }

    /**
     * on remet tous les events dans la liste d'event (de gauche)
     */
    private void resetResearch(){
        mAdapterEvent.swapCursor(mAdapterEvent.getCursorOriginal());
    }

    /**
     * Popup pour entrer le nom du parcours lors de la validation du parcours
      */
    private void enterName(){
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
