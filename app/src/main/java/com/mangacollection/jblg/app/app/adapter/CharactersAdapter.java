package com.mangacollection.jblg.app.app.adapter;

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

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.ViewHolder> {
    private MangaResponse manga;
    private Context context;

    public CharactersAdapter(Context context, MangaResponse manga) {
        this.context = context;
        this.manga = manga;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_item, parent, false);
        ButterKnife.bind(this, itemView);

        return new CharactersAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CharactersAdapter.ViewHolder holder, int position) {
        holder.characterName.setText(manga.getCharacters().get(position).getName());
        holder.characterRole.setText(manga.getCharacters().get(position).getRole());

        Picasso.with(context).load(manga.getCharacters().get(position).getImageUrl().
                replace("r/46x64/", "")).placeholder(R.drawable.ic_cloud_download_black_24dp)
                .resize(350, 450).error(R.drawable.ic_error_outline_black_24dp).into(holder.characterIV);

    }
    @Override
    public int getItemCount() {
        return manga.getCharacters().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.CharacterName)
        TextView characterName;
        @BindView(R.id.CharacterRole)
        TextView characterRole;
        @BindView(R.id.characterIV)
        ImageView characterIV;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
