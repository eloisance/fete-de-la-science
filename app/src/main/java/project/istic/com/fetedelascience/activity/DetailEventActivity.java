package project.istic.com.fetedelascience.activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.model.Event;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailEventActivity extends AppCompatActivity {
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

//    @BindView(R.id.imageEvent)
//    ImageView imageEvent;

    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        ButterKnife.bind(this);

        setTitle("Détail event");
        if(getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        event = this.getIntent().getParcelableExtra("event");
//        Picasso.with(getBaseContext()).load(this.event.getApercu()).into(imageEvent);
        if(event == null) {
            finish();
        }else {
            this.title.setText(event.getTitle());
            this.description.setText(event.getDescription());
            this.adresse.setText(event.getAdresse());
            this.horaire.setText(event.getResume_dates_fr());
            this.lien.setText(event.getLien());
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
}
