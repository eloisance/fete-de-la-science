package project.istic.com.fetedelascience.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.j256.ormlite.stmt.PreparedQuery;

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.model.Event;

public class MyInspectionRecyclerViewAdapter extends OrmliteCursorRecyclerViewAdapter<Event, MyInspectionRecyclerViewAdapter.ViewHolder> {

    public MyInspectionRecyclerViewAdapter(Cursor cursor, PreparedQuery<Event> preparedQuery) {
        super(cursor, preparedQuery);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, Event event) {
        holder.mEvent = event;
        holder.mTitle.setText(event.getTitle());
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