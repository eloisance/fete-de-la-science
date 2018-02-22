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

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.activity.CreateParcours;
import project.istic.com.fetedelascience.activity.DetailEventActivity;
import project.istic.com.fetedelascience.model.Event;

public class ParcoursEventRecyclerViewAdapter extends OrmliteCursorRecyclerViewAdapter<Event, ParcoursEventRecyclerViewAdapter.ViewHolder> implements Filterable {

    private CreateParcours context;

    public ParcoursEventRecyclerViewAdapter(CreateParcours c, Cursor cursor, PreparedQuery<Event> preparedQuery) {
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

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            context.addEventToParcours(event);
            }
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                Log.d("ds", "performFiltering");
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                // contactListFiltered = (ArrayList<Contact>) filterResults.values;
                Log.d("ds", "publishResults");
                notifyDataSetChanged();
            }
        };
    }

}