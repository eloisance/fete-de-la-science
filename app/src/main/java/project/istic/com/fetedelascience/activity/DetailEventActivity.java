package project.istic.com.fetedelascience.activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.global.Constants;
import project.istic.com.fetedelascience.model.Event;

import android.content.Intent;
import android.provider.Settings;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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

    @BindView(R.id.seekBar)
    SeekBar seekBar;

    @BindView(R.id.txtSeekBar)
    TextView txtSeekBar;

    @BindView(R.id.txtFillingRate)
    TextView txtFillingRate;

//    @BindView(R.id.imageEvent)
//    ImageView imageEvent;

    Event event;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        ButterKnife.bind(this);
        idUser = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        setTitle("Détail événement");
        if(getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        event = this.getIntent().getParcelableExtra("event");
//        Picasso.with(getBaseContext()).load(this.event.getApercu()).into(imageEvent);
        if(event == null) {
            finish();
            return;
        }


        // Filling rate feature
        this.getFillingRate();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    txtSeekBar.setText(String.format(getString(R.string.detail_event_organisateur_txt), seekBar.getProgress()));
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("fillingRate");
                    myRef.child(event.getId()+"").setValue(seekBar.getProgress());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
            });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                share(this.event.getTitle(), this.event.getVille());
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(DetailEventActivity.this, SettingsActivity.class);
                startActivity(intent);
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

    /**
     * Note moyenne de l'event
     */
    private void noteGlobaleUpdate(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("notation");
        myRef.child(event.getId()+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String,String> notes = (HashMap<String,String>)dataSnapshot.getValue();
                if(notes == null){
                    noteGlobale.setText(getString(R.string.not_note));
                } else {
                    double noteMoyenne = 0;
                    for(HashMap.Entry<String, String> entry : notes.entrySet()) {
                        String note = entry.getValue();
                        if (note != null) {
                            noteMoyenne += Double.parseDouble(note);
                        }
                    }
                    noteGlobale.setText("Note moyenne "+ (double)Math.round((noteMoyenne / notes.size() )*10)/10 );
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    /**
     * Note de l'utilisateur sur l'event
     */
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

    /**
     * Retourne le taux de remplissage de l'event si il a été
     * renseigné
     */
    private void getFillingRate() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("fillingRate").child(event.getId()+"");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object snap = dataSnapshot.getValue();
                if(snap != null) {
                    Long f = (Long) snap;
                    Integer fillingRate = f.intValue();
                    seekBar.setProgress(fillingRate);
                    txtSeekBar.setText(String.format(getString(R.string.detail_event_organisateur_txt), fillingRate));
                    txtFillingRate.setText(String.format(getString(R.string.detail_event_filling_rate), fillingRate));
                } else {
                    seekBar.setProgress(0);
                    txtSeekBar.setText(String.format(getString(R.string.detail_event_organisateur_txt), 0));
                    txtFillingRate.setText(String.format(getString(R.string.detail_event_filling_rate), 0));
                }
                if (mAuth.getCurrentUser() != null) {
                    seekBar.setVisibility(View.VISIBLE);
                    txtSeekBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void share(String title, String city) {
        String text = String.format(getString(R.string.share_txt), title, city);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Partager via"));
    }
}
