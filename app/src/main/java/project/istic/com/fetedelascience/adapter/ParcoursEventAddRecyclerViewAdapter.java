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
import project.istic.com.fetedelascience.util.UIHelper;

public class ParcoursEventAddRecyclerViewAdapter extends RecyclerView.Adapter< ParcoursEventAddRecyclerViewAdapter.ViewHolder> implements Filterable {

    ArrayList<Event> parcours;

    Context context;

    View view;

    public ParcoursEventAddRecyclerViewAdapter(Context context,View view) {
        this.parcours = new ArrayList<>();
        this.context = context;
        this.view = view;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_parcours, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTitle.setText(this.parcours.get(position).getTitle());
        holder.mNumber.setText(1+position+"");

        holder.mView.setOnClickListener(v -> {
            parcours.remove(parcours.get(position));
            notifyDataSetChanged();

        });
    }

    @Override
    public int getItemCount() {
        return this.parcours.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mTitle;
        private TextView mNumber;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.event_title);
            mNumber = (TextView) view.findViewById(R.id.number);
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
        Log.d("size",parcours.size()+"");
        boolean contains = false;
        for(Event event1 : parcours){
            if(event.getId() == event1.getId()){
                contains = true;
            }

        }
        if(!contains) {
            parcours.add(event);
            notifyDataSetChanged();
        } else {
            UIHelper.showSnackbar( this.view,this.context, this.context.getString(R.string.text_popup_error_containt), "OK");

        }
    }

    public ArrayList<Event> getParcours() {
        return parcours;
    }
}