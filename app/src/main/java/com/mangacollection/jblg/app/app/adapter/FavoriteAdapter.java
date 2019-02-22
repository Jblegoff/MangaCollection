package com.mangacollection.jblg.app.app.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mangacollection.jblg.myapplication.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class  FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>  {
    public List<String> favoriteList;

    public FavoriteAdapter(List<String> favoriteList){
        this.favoriteList=favoriteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_item,parent,false);
        ButterKnife.bind(this,itemView);

        return new FavoriteAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
            holder.favoriteTitleTV.setText(favoriteList.get(position));
    }

    public List<String> getAllItemsFromList(){
        return favoriteList;
    }

    @Override
    public int getItemCount() {

        return favoriteList.size();
    }


    public void removeFavorite(int position) {
        favoriteList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.favoriteTitleTV)
        TextView favoriteTitleTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
