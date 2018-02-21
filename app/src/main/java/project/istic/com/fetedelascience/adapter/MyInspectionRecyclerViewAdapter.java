package project.istic.com.fetedelascience.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.j256.ormlite.stmt.PreparedQuery;

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.activity.DetailEventActivity;
import project.istic.com.fetedelascience.model.Event;

public class MyInspectionRecyclerViewAdapter extends OrmliteCursorRecyclerViewAdapter<Event, MyInspectionRecyclerViewAdapter.ViewHolder> {

    private Context context;

    public MyInspectionRecyclerViewAdapter(Context c, Cursor cursor, PreparedQuery<Event> preparedQuery) {
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
        holder.mEvent = event;
        holder.mTitle.setText(event.getTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailEventActivity.class);
                intent.putExtra("event", event);
                context.startActivity(intent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Event mEvent;
        private View mView;
        private TextView mTitle;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.event_title);
        }
    }
}