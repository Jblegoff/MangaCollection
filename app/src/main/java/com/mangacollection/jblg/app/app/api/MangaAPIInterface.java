package com.mangacollection.jblg.app.app.api;


import com.mangacollection.jblg.app.app.models.manga.manga.MangaResponse;
import com.mangacollection.jblg.app.app.models.manga.search.SearchResponse;
import com.mangacollection.jblg.app.app.models.manga.trending.TrendingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MangaAPIInterface {
    @GET("http://api.jikan.moe/v3/top/manga/1/")
    Call<TrendingResponse> getTrendingManga();

    @GET("http://api.jikan.moe/v3/manga/{malId}/characters")
    Call<MangaResponse> getMangaByID(@Path("malId") int id);

    @GET("http://api.jikan.moe/search/manga")
    Call<SearchResponse> searchByMangaTitle(@Query("q") String title);

}
