package project.istic.com.fetedelascience.activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.model.Event;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class DetailEventActivity extends AppCompatActivity {

    private static String idUser;
    @BindView(R.id.title_event)
    TextView title;

    @BindView(R.id.description_event)
    TextView description;

    @BindView(R.id.adresse_event)
    TextView adresse;

    @BindView(R.id.horaire_event)
    TextView horaire;

    @BindView(R.id.lien_event)
    TextView lien;

    @BindView(R.id.notation)
    RatingBar notation;

    @BindView(R.id.noteGlobale)
    TextView noteGlobale;

//    @BindView(R.id.imageEvent)
//    ImageView imageEvent;

    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        ButterKnife.bind(this);
        idUser = "Dd";
        setTitle("Détail event");
        if(getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        event = this.getIntent().getParcelableExtra("event");
//        Picasso.with(getBaseContext()).load(this.event.getApercu()).into(imageEvent);
        if(event == null) {
            finish();
            return;
        }
        this.getMyNote();
        this.addListenerRatingBar();
        this.noteGlobaleUpdate();
        this.title.setText(event.getTitle());
        this.description.setText(event.getDescription());
        this.adresse.setText(event.getAdresse());
        this.horaire.setText(event.getResume_dates_fr());
        this.lien.setText(event.getLien());



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

    private void addListenerRatingBar(){
        this.notation.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("notation");
                myRef.child(event.getId()+"").child(idUser+"").setValue(""+rating);



            }
        });

    }

    private void noteGlobaleUpdate(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("notation");
        myRef.child(event.getId()+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String,String> notes = (HashMap<String,String>)dataSnapshot.getValue();
                if(notes == null){
                    noteGlobale.setText("Pas encore de note");
                } else {
                    double noteMoyenne = 0;
                    for(HashMap.Entry<String, String> entry : notes.entrySet()) {
                        String note = entry.getValue();
                        if (note != null) {
                            noteMoyenne += Double.parseDouble(note);
                        }
                    }
//                    for(String note : notes){
//                        if (note != null) {
//                            noteMoyenne += Double.parseDouble(note);
//                        }
//                    }

                    noteGlobale.setText("Note moyenne "+ noteMoyenne / (notes.size() ) );

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    private void getMyNote(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("notation").child(event.getId()+"").child(idUser+"");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Object snap = dataSnapshot.getValue();
               if(snap != null ) {
                   double note = Double.parseDouble((String) snap);
                   notation.setRating((float) note);
                   myRef.removeEventListener(this);
               }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

    }
}
