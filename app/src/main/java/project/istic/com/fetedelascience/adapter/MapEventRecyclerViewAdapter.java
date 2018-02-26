package project.istic.com.fetedelascience.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.j256.ormlite.stmt.PreparedQuery;

import java.util.ArrayList;
import java.util.List;

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.activity.DetailEventActivity;
import project.istic.com.fetedelascience.model.Event;
import project.istic.com.fetedelascience.model.Parcours;

public class MapEventRecyclerViewAdapter extends RecyclerView.Adapter< MapEventRecyclerViewAdapter.ViewHolder> implements Filterable {

    ArrayList<Event> events;
    ArrayList<Event> eventsfilter;

    private Context context;

    private Filter filter;

    public MapEventRecyclerViewAdapter(Context c, ArrayList<Event> events) {

        this.context = c;
        if(events == null){
            this.events = new ArrayList<>();
            this.eventsfilter = new ArrayList<>();
        } else {
            this.events = events;
            this.eventsfilter = events;
        }
        filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterString = charSequence.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final List<Event> list = events;

                int count = list.size();
                final ArrayList<Event> nlist = new ArrayList<Event>(count);

                String filterableString ;

                for (int i = 0; i < count; i++) {
                    filterableString = list.get(i).getTitle();
                    if (filterableString.toLowerCase().contains(filterString)) {
                        nlist.add(list.get(i));
                    }
                }

                results.values = nlist;
                results.count = nlist.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                eventsfilter = (ArrayList<Event>) results.values;
                notifyDataSetChanged();
            }
        };
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mTitle.setText(this.eventsfilter.get(position).getTitle());
        holder.mCity.setText(this.eventsfilter.get(position).getVille());

        holder.mView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailEventActivity.class);
            intent.putExtra("event", this.eventsfilter.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new MapEventRecyclerViewAdapter.ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return this.eventsfilter.size();
    }

    @Override
    public Filter getFilter() {
        return this.filter;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mTitle;
        private TextView mCity;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = view.findViewById(R.id.event_title);
            mCity = view.findViewById(R.id.event_city);
        }
    }



}