package com.prateekraina.gamersden;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Prateek Raina on 02-12-2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DataObjectHolder> {

    private static String LOG_TAG = "RecyclerViewAdapter";
    private ArrayList<com.prateekraina.gamersden.DataObject> mDataset;
    private ArrayList<com.prateekraina.gamersden.DataObject> duplicateDataset;
    //private String address;

    public RecyclerViewAdapter(ArrayList<com.prateekraina.gamersden.DataObject> myDataset) {
        this.mDataset = myDataset;
        this.duplicateDataset = new ArrayList<com.prateekraina.gamersden.DataObject>();
        this.duplicateDataset = this.mDataset;

    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_card, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        holder.name_textview.setText(mDataset.get(position).getNAME());
        holder.location_textview.setText(mDataset.get(position).getLOCATION());
        holder.city_textview.setText(mDataset.get(position).getCITY());
//        address = mDataset.get(position).getADDRESS();

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        this.mDataset = new ArrayList<com.prateekraina.gamersden.DataObject>();
        if (charText.length() == 0) {
            this.mDataset.addAll(this.duplicateDataset);
        } else {
            for (com.prateekraina.gamersden.DataObject item : this.duplicateDataset) {
                if (item.getLOCATION().toLowerCase(Locale.getDefault()).contains(charText.toLowerCase()) ||
                        item.getCITY().toLowerCase(Locale.getDefault()).contains(charText.toLowerCase())) {
                    this.mDataset.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView name_textview;
        TextView location_textview;
        TextView city_textview;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name_textview = (TextView) itemView.findViewById(R.id.displayCafeName);
            location_textview = (TextView) itemView.findViewById(R.id.displayCafeLocation);
            city_textview = (TextView) itemView.findViewById(R.id.displayCafeCity);

            Log.i(LOG_TAG, "Adding Listener");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    String jsonString = new Gson().toJson(mDataset.get(position));
//                    Intent intent = new Intent(v.getContext(),DetailActivity.class);
//                    intent.putExtra("object",jsonString);
//                    v.getContext().startActivity(intent);

                    int position = getAdapterPosition();
                    Intent intent = new Intent(v.getContext(), com.prateekraina.gamersden.MapsActivity.class);

                    intent.putExtra("completeAddress", mDataset.get(position).getADDRESS().trim()
                            + "," + " " + mDataset.get(position).getLOCATION().trim()
                            + "," + " " + mDataset.get(position).getCITY().trim());
                    intent.putExtra("title",mDataset.get(position).getNAME().trim());
                    v.getContext().startActivity(intent);

//                    Toast.makeText(v.getContext(), "This is : " + mDataset.get(getAdapterPosition()).getNAME(),
//                            Toast.LENGTH_LONG).show();

                }
            });

        }
    }

}
