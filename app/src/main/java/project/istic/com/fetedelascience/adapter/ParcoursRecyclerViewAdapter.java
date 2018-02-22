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
import project.istic.com.fetedelascience.model.Parcours;

public class ParcoursRecyclerViewAdapter extends RecyclerView.Adapter< ParcoursRecyclerViewAdapter.ViewHolder> implements Filterable {

    ArrayList<Parcours> parcours;

    private Context context;

    public ParcoursRecyclerViewAdapter(Context c, ArrayList<Parcours> parcours) {
        this.context = c;
        if(parcours == null){
            this.parcours = new ArrayList<>();
        } else {
            this.parcours = parcours;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parcours, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitle.setText(this.parcours.get(position).getName());
        holder.mSize.setText("Nombre d'évenement : "+this.parcours.get(position).numberEvent());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, DetailEventActivity.class);
//                intent.putExtra("event", event);
//                context.startActivity(intent);
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

    public void setParcours(ArrayList<Parcours> parcours) {
        this.parcours = parcours;

    }
}