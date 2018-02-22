package project.istic.com.fetedelascience.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.activity.CreateParcours;
import project.istic.com.fetedelascience.model.Event;
import project.istic.com.fetedelascience.model.Parcours;

public class ParcoursEventAddRecyclerViewAdapter extends RecyclerView.Adapter< ParcoursEventAddRecyclerViewAdapter.ViewHolder> implements Filterable {

    ArrayList<Event> parcours;



    public ParcoursEventAddRecyclerViewAdapter() {
        this.parcours = new ArrayList<>();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTitle.setText(this.parcours.get(position).getTitle());
        holder.mCity.setText(this.parcours.get(position).getVille());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parcours.remove(parcours.get(position));
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.parcours.size();
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

    public void addEvent(Event event){
        parcours.add(event);
        notifyDataSetChanged();
    }

    public ArrayList<Event> getParcours() {
        return parcours;
    }
}