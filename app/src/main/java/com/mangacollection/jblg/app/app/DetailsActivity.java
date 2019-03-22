package com.mangacollection.jblg.app.app;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mangacollection.jblg.app.app.adapter.CharactersAdapter;
import com.mangacollection.jblg.app.app.api.MangaAPI;
import com.mangacollection.jblg.app.app.api.MangaAPIInterface;
import com.mangacollection.jblg.app.app.models.manga.Favorite;
import com.mangacollection.jblg.app.app.models.manga.TempEnumForManga;
import com.mangacollection.jblg.app.app.models.manga.manga.MangaResponse;
import com.mangacollection.jblg.app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.navigationDetails)
    BottomNavigationView navigationDetails;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.posterIV)
    ImageView posterIV;
    @BindView(R.id.titleTV)
    TextView titleTV;
    @BindView(R.id.numberOfVolumesTV)
    TextView Volumes;
    @BindView(R.id.synopsisTV)
    TextView synopsis;
    @BindView(R.id.charactersRV)
    RecyclerView recyclerView;
    @BindView(R.id.favoriteIBT)
    ImageButton favoriteIBT;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    private MangaAPIInterface service = MangaAPI.getRetrofit().create(MangaAPIInterface.class);
    private TempEnumForManga enumForManga = TempEnumForManga.INSTANCE;
    private MangaResponse manga;
    private CharactersAdapter charactersAdapter;
    private Parcelable recyclerViewState;
    private DatabaseReference databaseReference;
    private List<String> favoriteTitle = new ArrayList<>();
    private int yellow= Color.YELLOW;
    private int gray = Color.GRAY;
    private String userName;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_details_prev:
                    getPrevOrNextManga(manga.getMalId() - 1);
                    return true;
                case R.id.nav_home_from_details:
                    finish();
                    return true;
                case R.id.nav_details_next:
                    getPrevOrNextManga(manga.getMalId() + 1);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login Data", MODE_PRIVATE);
        if(savedInstanceState!=null){
            recyclerViewState=savedInstanceState.getParcelable("CHARACTER_LIST_STATE");
            manga=savedInstanceState.getParcelable("MANGA");
        }

        navigationDetails.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        manga=enumForManga.getManga();
        userName= sharedPreferences.getString("userName",null);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        charactersAdapter = new CharactersAdapter(getApplicationContext(),manga);
        recyclerView.setAdapter(charactersAdapter);
        Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(recyclerViewState);

        updateUI();
        loadingIndicator.setVisibility(View.INVISIBLE);

    }



    private void queryAndUpdateUI(){
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(userName!=null){
            databaseReference.child("favoriteManga").child(userName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Favorite dbStuff = dataSnapshot.getValue(Favorite.class);
                            if(dbStuff!=null){
                                favoriteTitle=dbStuff.getFavoriteTitles();
                            } else {
                                throw new DatabaseException("DB ERROR");
                            }
                            if (favoriteTitle.contains(manga.getTitle())){
                                favoriteIBT.setColorFilter(yellow);
                                favoriteIBT.setSelected(true);
                            }else {
                                favoriteIBT.setColorFilter(gray);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("CHARACTER_LIST_STATE", Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState());
        outState.putParcelable("MANGA",manga);
    }

    private void updateUI(){
        titleTV.setText(manga.getTitle());

        synopsis.setText((manga.getSynopsis()));
        Volumes.setText(String.valueOf(manga.getVolumes()));
        Picasso.with(getApplicationContext()).load(manga.getImageUrl()).resize(350,450)
                .placeholder(R.drawable.ic_cloud_download_black_24dp)
                .error(R.drawable.common_full_open_on_phone).into(posterIV);
        charactersAdapter=new CharactersAdapter(getApplicationContext(),manga);
        recyclerView.setAdapter(charactersAdapter);
        queryAndUpdateUI();
    }

    private void getPrevOrNextManga(int malId){
        Call<MangaResponse> call=service.getMangaByID(malId);
        loadingIndicator.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaResponse> call,@NonNull Response<MangaResponse> response) {
                if(response.body()!=null){
                    manga=response.body();
                    enumForManga.setManga(manga);
                    updateUI();
                    loadingIndicator.setVisibility(View.INVISIBLE);
                }else{
                    loadingIndicator.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplication(),getString(R.string.error_on_no_result_from_server), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponse> call,@NonNull Throwable t) {
                Toast.makeText(getApplication(), getString(R.string.error_on_no_result_from_server), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.favoriteIBT)
    public void addAndRemoveFavorite(){
        if(userName != null){
            databaseReference.child("favoriteManga").child(userName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Favorite dbStuff = dataSnapshot.getValue(Favorite.class);
                            if (dbStuff != null) {
                                favoriteTitle = dbStuff.getFavoriteTitles();
                            } else {
                                throw new DatabaseException("DB Error");
                            }

                            if (!favoriteTitle.contains(manga.getTitle())) {
                                favoriteTitle.add(manga.getTitle());
                                writeData(userName, favoriteTitle);
                                favoriteIBT.setColorFilter(yellow);
                                favoriteIBT.setSelected(true);
                            } else {
                                favoriteTitle.remove(manga.getTitle());
                                writeData(userName, favoriteTitle);
                                favoriteIBT.setColorFilter(gray);
                                favoriteIBT.setSelected(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(),getString(R.string.login_to_save_favorite), Toast.LENGTH_LONG).show();
        }
    }
    private void writeData(String userName, List<String> favoriteTitle){
        Favorite input = new Favorite(favoriteTitle);
        Map<String,Object> postValues = input.toMap();
        Map<String,Object> childUpdates =new HashMap<>();

        childUpdates.put("/favoriteManga/" + userName,postValues);

        databaseReference.updateChildren(childUpdates);
    }

}

