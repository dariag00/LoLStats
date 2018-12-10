package com.example.klost.lolstats;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.klost.lolstats.models.Summoner;
import com.example.klost.lolstats.models.matches.Match;

public class MatchDetailAdapter extends RecyclerView.Adapter<MatchDetailAdapter.MatchDetailAdapterViewHolder>{

    private String[] data;

    private final MatchDetailAdapter.MatchAdapterOnClickHandler mClickHandler;

    public interface MatchAdapterOnClickHandler{
        void onClick(String playerPosition);
    }

    MatchDetailAdapter(MatchAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public class MatchDetailAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        MatchDetailAdapterViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("Prueba", "PULSADO Y LLAMADO");
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(String.valueOf(adapterPosition));
        }

    }

    @NonNull
    @Override
    public MatchDetailAdapter.MatchDetailAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Context context =  viewGroup.getContext();
        int layoutIdForListItem = R.layout.details_match_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new MatchDetailAdapter.MatchDetailAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchDetailAdapter.MatchDetailAdapterViewHolder matchDetailAdapterViewHolder, int position) {

    }


    @Override
    public int getItemCount() {
        if (null == data) return 0;
        return data.length;
    }

    public void setData(String[] mData) {
        data = mData;
        notifyDataSetChanged();
    }
}
