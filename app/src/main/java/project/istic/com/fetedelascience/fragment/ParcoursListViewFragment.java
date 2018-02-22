package project.istic.com.fetedelascience.fragment;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.adapter.ParcoursRecyclerViewAdapter;
import project.istic.com.fetedelascience.model.Parcours;

public class ParcoursListViewFragment extends Fragment {

    private ParcoursRecyclerViewAdapter mAdapter;

    @BindView(R.id.recycler_parcour)
    RecyclerView mRecycler;


    private SearchView searchView;

    private  ArrayList<Parcours> parcours;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.getParcours();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listparcours, container, false);

        ButterKnife.bind(this, rootView);
        mRecycler.setHasFixedSize(true);

        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mManager);


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


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

    private void getParcours(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("parcours");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<HashMap<String,Parcours>> t = new GenericTypeIndicator<HashMap<String,Parcours>>() {};
                HashMap<String,Parcours> yourStringArray = dataSnapshot.getValue(t);
                if(yourStringArray == null){
                    parcours = new ArrayList<>();
                } else {
                    parcours = new ArrayList<>(yourStringArray.values());
                }

                mAdapter = new ParcoursRecyclerViewAdapter(getContext(),parcours);

                mRecycler.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }
}
