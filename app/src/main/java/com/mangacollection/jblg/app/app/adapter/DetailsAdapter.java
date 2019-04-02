package com.mangacollection.jblg.app.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mangacollection.jblg.app.R;
import com.mangacollection.jblg.app.app.models.manga.manga.MangaResponse;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    private MangaResponse mangaResponse;
    private Context context;

    public DetailsAdapter(Context context, MangaResponse mangaResponse) {
        this.context=context;
        this.mangaResponse=mangaResponse;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType){
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.content_details,parent,true);
        ButterKnife.bind(this,view);
        return new DetailsAdapter.ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.titleTV.setText(mangaResponse.getTitle());
        holder.numberOfVolumesTV.setText(mangaResponse.getVolumes().toString());
        holder.synopsisTV.setText(mangaResponse.getSynopsis());
        holder.authorTV.setText(mangaResponse.getAuthors().get(i).toString());
        Picasso.with(context).load(mangaResponse.getImageUrl())
                .resize(350, 450)
                .placeholder(R.drawable.ic_cloud_download_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp).into(holder.posterIV);
    }

    @Override
    public int getItemCount() {
        return mangaResponse.getVolumes();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.titleTV)
        TextView titleTV;
        @BindView(R.id.numberOfVolumesTV)
        TextView numberOfVolumesTV;
        @BindView(R.id.posterIV)
        ImageView posterIV;
        @BindView(R.id.synopsisTV)
        TextView synopsisTV;
        @BindView(R.id.authorTV)
        TextView authorTV;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
