package project.istic.com.fetedelascience.activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.model.Event;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DetailEventActivity extends AppCompatActivity {
    @BindView(R.id.title_event)
    TextView title;

    @BindView(R.id.description_event)
    TextView description;

    @BindView(R.id.horaire_event)
    TextView horaire;

    @BindView(R.id.lien_event)
    TextView lien;

    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);


        ButterKnife.bind(this);
        event = this.getIntent().getParcelableExtra("Event");

        this.title.setText(event.getTitle());// Parcelable
        this.description.setText(event.getDescription());// Parcelable
        this.horaire.setText(event.getResume_dates_fr());// Parcelable
        this.lien.setText(event.getLien());// Parcelable
    }
}
