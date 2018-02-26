package project.istic.com.fetedelascience.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.adapter.StatsRecyclerViewAdapter;

/**
 * Created by jnsll on 26/02/18.
 */

public class StatsActivity  extends AppCompatActivity {

    HashMap<Double, Integer> notesGlobales= new HashMap<Double, Integer>();
//    @BindView(R.id.title_stats)
//    TextView title;
//
//    @BindView(R.id.user_parcours)
//    TextView user;
//
//    @BindView(R.id.)
//    RecyclerView mRecycler;
    @BindView(R.id.chart)
    BarChart chart;

    private StatsRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mManager;

    //Parcours parcours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ButterKnife.bind(this);


        setTitle("Statistiques des notes moyennes des événements");
        if(getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        mRecycler.setHasFixedSize(true);

        mManager = new LinearLayoutManager(this);
//        mRecycler.setLayoutManager(mManager);
        noteGlobale();


    }

    private void noteGlobale(){
        //notesGlobales = new HashMap<Double, Integer>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("notation");

        final ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    HashMap<String, String> notes = (HashMap<String, String>) postSnapshot.getValue();
                    double noteMoyenne = 0;
                    for (HashMap.Entry<String, String> entry : notes.entrySet()) {
                        //Log.d("NOTE1", entry.getValue());
                        String note = entry.getValue();
                        if (note != null) {
                            noteMoyenne += Double.parseDouble(note);
                        }
                    }
                    noteMoyenne= (double)Math.round((noteMoyenne / notes.size() )*10)/10;
                    Log.d("NOTESBUG", "notemoyenne: " + noteMoyenne);
                    Integer count = (Integer) notesGlobales.get(noteMoyenne);

                    if (count == null) {
                        notesGlobales.put(noteMoyenne, 1);
                    } else {
                        Log.d("NOTESBUG", "count : " + count);
                        notesGlobales.put(noteMoyenne, notesGlobales.get(noteMoyenne) + 1);
                        Log.d("NOTESBUG", "else");
                    }
                }

                //graph();
                List<BarEntry> entries = new ArrayList<BarEntry>();
                // turn your data into Entry objects
                for (HashMap.Entry<Double, Integer> data : notesGlobales.entrySet()) {
                    Log.d("ENTRYVALUE", "" + data.getValue());
                    Log.d("ENTRYKEY", data.getKey().toString());
                    //Log.d("ENTRYHASH", data.hashCode());
                    entries.add(new BarEntry(data.getKey().floatValue(), data.getValue()));
                }
                BarDataSet dataSet = new BarDataSet(entries, "Répartition des notes moyennes");
                //dataSet.setColor(0);
                //dataSet.setValueTextColor(1);
                //dataSet.setColor(R.color.colorAccent);
                //dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                dataSet.setDrawValues(false);
                //dataSet.setBarBorderWidth(2);
                        //

                BarData barData = new BarData(dataSet);
                barData.setBarWidth(0.1f);
                Log.d("CHART", "width"+ barData.getBarWidth());
                Description descp = new Description();
                descp.setText("Répartition des notes moyennes");
                chart.setDescription(descp);
                chart.getDescription().setEnabled(false);
                chart.setData(barData);
                chart.setDrawValueAboveBar(false);

                chart.setPinchZoom(false);

                chart.setDrawBarShadow(false);
                chart.setDrawGridBackground(false);

                XAxis xAxis = chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);

                chart.getAxisLeft().setDrawGridLines(false);
                chart.getLegend().setEnabled(false);



                chart.invalidate();
                Log.d("NOTES", notesGlobales.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(StatsActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
