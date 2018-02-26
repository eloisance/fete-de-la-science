package project.istic.com.fetedelascience.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.stmt.PreparedQuery;

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.activity.DetailEventActivity;
import project.istic.com.fetedelascience.model.Event;
import project.istic.com.fetedelascience.util.OrmliteCursorRecyclerViewAdapter;

public class MyEventRecyclerViewAdapter extends OrmliteCursorRecyclerViewAdapter<Event, MyEventRecyclerViewAdapter.ViewHolder> {

    private Context context;

    public MyEventRecyclerViewAdapter(Context c, Cursor cursor, PreparedQuery<Event> preparedQuery) {
        super(cursor, preparedQuery);


        this.context = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_extend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final Event event) {

        holder.mTitle.setText(event.getTitle());
        if (event.getDescription() != null) {
            if (event.getDescription().length() < 100) {
                holder.mDesc.setText(event.getDescription().substring(0, event.getDescription().length()));
            } else {
                holder.mDesc.setText(event.getDescription().substring(0, 100) + "...");
            }
        } else {
            holder.mDesc.setText("");
        }
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
        private TextView mDesc;
        private TextView mCity;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = view.findViewById(R.id.event_title);
            mDesc = view.findViewById(R.id.event_description);
            mCity = view.findViewById(R.id.event_city);
        }
    }



}