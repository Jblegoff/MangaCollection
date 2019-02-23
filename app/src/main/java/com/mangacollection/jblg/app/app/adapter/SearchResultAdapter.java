package com.mangacollection.jblg.app.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mangacollection.jblg.app.app.DetailsActivity;
import com.mangacollection.jblg.app.app.api.MangaAPI;
import com.mangacollection.jblg.app.app.api.MangaAPIInterface;
import com.mangacollection.jblg.app.app.models.manga.TempEnumForManga;
import com.mangacollection.jblg.app.app.models.manga.manga.MangaResponse;
import com.mangacollection.jblg.app.app.models.manga.search.SearchResponse;
import com.mangacollection.jblg.app.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private SearchResponse manga;
    private Context myContext;
    private ProgressBar progressBar;

    public SearchResultAdapter(Context myContext, ProgressBar progressBar, SearchResponse manga) {
        this.myContext = myContext;
        this.progressBar = progressBar;
        this.manga = manga;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);
        ButterKnife.bind(this, itemView);

        return new SearchResultAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.with(myContext)
                .load(manga.getResult().get(position).getImageUrl().replace("r/100x140/", ""))
                .placeholder(R.drawable.ic_cloud_download_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp).into(holder.mangaPosterIV);
        holder.mangaTitleTV.setText(manga.getResult().get(position).getTitle());
        holder.mangaTypeTV.setText(manga.getResult().get(position).getType());
    }

    @Override
    public int getItemCount() {
        return manga.getResult().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mangaPosterIV)
        ImageView mangaPosterIV;
        @BindView(R.id.mangaTitleTV)
        TextView mangaTitleTV;
        @BindView(R.id.mangaTypeTV)
        TextView mangaTypeTV;
        @BindView(R.id.card_view)
        CardView cardView;

        private Intent intent;
        private MangaAPIInterface service = MangaAPI.getRetrofit().create(MangaAPIInterface.class);

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            progressBar.setVisibility(View.INVISIBLE);
            intent = new Intent(myContext, DetailsActivity.class);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int malId = manga.getResult().get(getAdapterPosition()).getMalId();
                    progressBar.setVisibility(View.VISIBLE);
                    Call<MangaResponse> call = service.getMangaByID(malId);
                    call.enqueue(new Callback<MangaResponse>() {

                        @Override
                        public void onResponse(@NonNull Call<MangaResponse> call, @NonNull Response<MangaResponse> response) {
                            if (response.body() != null) {
                                TempEnumForManga enumForManga = TempEnumForManga.INSTANCE;
                                enumForManga.setManga(response.body());
                                progressBar.setVisibility(View.INVISIBLE);
                                myContext.startActivity(intent);
                            }
                            else{
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(myContext, myContext.getString(R.string.error_on_no_result_from_server), Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<MangaResponse> call, @NonNull Throwable t) {
                            Toast.makeText(myContext, myContext.getString(R.string.error_on_no_result_from_server), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }
}
