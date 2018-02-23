package project.istic.com.fetedelascience.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.activity.DetailParcoursActivity;
import project.istic.com.fetedelascience.model.Parcours;

public class ParcoursRecyclerViewAdapter extends RecyclerView.Adapter< ParcoursRecyclerViewAdapter.ViewHolder> implements Filterable {

    ArrayList<Parcours> parcours;
    ArrayList<Parcours> parcoursfilter;

    private Context context;

    private Filter filter;

    public ParcoursRecyclerViewAdapter(Context c, ArrayList<Parcours> parcours) {
        this.context = c;
        if(parcours == null){
            this.parcours = new ArrayList<>();
            this.parcoursfilter = new ArrayList<>();
        } else {
            this.parcours = parcours;
            this.parcoursfilter = parcours;
        }
        filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterString = charSequence.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final List<Parcours> list = parcours;

                int count = list.size();
                final ArrayList<Parcours> nlist = new ArrayList<Parcours>(count);

                String filterableString ;

                for (int i = 0; i < count; i++) {
                    filterableString = list.get(i).getName();
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
                parcoursfilter = (ArrayList<Parcours>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parcours, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitle.setText(this.parcoursfilter.get(position).getName());
        holder.mSize.setText("Nombre d'évenement : "+this.parcours.get(position).numberEvent());
        holder.mView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailParcoursActivity.class);
            intent.putExtra("parcours", (Parcelable) parcoursfilter.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.parcoursfilter.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mTitle;
        private TextView mSize;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = view.findViewById(R.id.parcour_title);
            mSize = view.findViewById(R.id.parcour_size);

        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public void reset() {

    }

}