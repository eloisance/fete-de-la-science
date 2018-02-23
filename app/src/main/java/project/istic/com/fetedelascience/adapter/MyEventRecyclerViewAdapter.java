package project.istic.com.fetedelascience.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.j256.ormlite.stmt.PreparedQuery;

import java.util.List;

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.activity.DetailEventActivity;
import project.istic.com.fetedelascience.model.Event;

public class MyEventRecyclerViewAdapter extends OrmliteCursorRecyclerViewAdapter<Event, MyEventRecyclerViewAdapter.ViewHolder> {

    private Context context;

    public MyEventRecyclerViewAdapter(Context c, Cursor cursor, PreparedQuery<Event> preparedQuery) {
        super(cursor, preparedQuery);


        this.context = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final Event event) {

        holder.mTitle.setText(event.getTitle());
        holder.mCity.setText(event.getVille());

        holder.mView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailEventActivity.class);
            intent.putExtra("event", event);
            context.startActivity(intent);
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mTitle;
        private TextView mCity;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.event_title);
            mCity = (TextView) view.findViewById(R.id.event_city);
        }
    }



}