package com.mangacollection.jblg.app.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mangacollection.jblg.app.app.DetailsActivity;
import com.mangacollection.jblg.app.app.api.MangaAPI;
import com.mangacollection.jblg.app.app.api.MangaAPIInterface;
import com.mangacollection.jblg.app.app.models.manga.TempEnumForManga;
import com.mangacollection.jblg.app.app.models.manga.manga.MangaResponse;
import com.mangacollection.jblg.app.app.models.manga.trending.TrendingResponse;
import com.mangacollection.jblg.app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrendingMangaAdapter extends RecyclerView.Adapter <TrendingMangaAdapter.ViewHolder> {

        private List<String> posterPaths = new ArrayList<>();
        private Context myContext;
        private TrendingResponse trendingMangaResponse;
        private ProgressBar progressBar;

        public TrendingMangaAdapter(Context context, ProgressBar progressBar, TrendingResponse trendingMangaResponse){
            this.trendingMangaResponse = trendingMangaResponse;
            this.progressBar = progressBar;
            this.myContext = context;
        }

        private void getPosterPaths() {
            for (int i = 0; i < trendingMangaResponse.getTop().size(); i++) {
                String posterUrlWithoutSize = trendingMangaResponse.getTop().get(i).getImageUrl().replace("/r/100x140", "");
                posterPaths.add(posterUrlWithoutSize);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_row, parent, false);
            ButterKnife.bind(this, itemView);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            getPosterPaths();
            Picasso.with(myContext)
                    .load(posterPaths.get(position))
                    .placeholder(R.drawable.ic_cloud_download_black_24dp).resize(350, 450)
                    .error(R.drawable.ic_error_outline_black_24dp).into(holder.trendingMangaPosterIV);
        }

        @Override
        public int getItemCount() {
            return trendingMangaResponse.getTop().size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.trendingPosterIV)
            ImageView trendingMangaPosterIV;
            private Intent intent;
            private MangaAPIInterface service = MangaAPI.getRetrofit().create(MangaAPIInterface.class);

            private ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                intent = new Intent(myContext, DetailsActivity.class);
                trendingMangaPosterIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        progressBar.setVisibility(View.VISIBLE);
                        int malId = trendingMangaResponse.getTop().get(getAdapterPosition()).getMalId();
                        Call<MangaResponse> call = service.getMangaByID(malId);
                        call.enqueue(new Callback<MangaResponse>() {

                            @Override
                            public void onResponse(@NonNull Call<MangaResponse> call, @NonNull Response<MangaResponse> response) {
                                if(response.body() != null) {
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
