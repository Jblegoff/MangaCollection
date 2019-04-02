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
import com.mangacollection.jblg.app.R;
import com.mangacollection.jblg.app.app.adapter.CharactersAdapter;
import com.mangacollection.jblg.app.app.api.MangaAPI;
import com.mangacollection.jblg.app.app.api.MangaAPIInterface;
import com.mangacollection.jblg.app.app.models.manga.Favorite;
import com.mangacollection.jblg.app.app.models.manga.TempEnumForManga;
import com.mangacollection.jblg.app.app.models.manga.manga.MangaResponse;
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
    BottomNavigationView navigation;
    @BindView(R.id.authorTV)
    TextView authorTV;
    @BindView(R.id.posterIV)
    ImageView posterIV;
    @BindView(R.id.titleTV)
    TextView titleTV;
    @BindView(R.id.numberOfVolumesTV)
    TextView numberOfVolumesTV;
    @BindView(R.id.synopsisTV)
    TextView synopsisTV;
    @BindView(R.id.charactersRV)
    RecyclerView recyclerView;
    @BindView(R.id.favoriteIBT)
    ImageButton favorite;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    private MangaAPIInterface service = MangaAPI.getRetrofit().create(MangaAPIInterface.class);
    private TempEnumForManga enumForManga = TempEnumForManga.INSTANCE;
    private MangaResponse manga;
    private CharactersAdapter charactersAdapter;
    private Parcelable recyclerViewState;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private List<String> favoriteTitles = new ArrayList<>();
    private int yellow = Color.YELLOW;
    private int gray = Color.GRAY;
    private String userName;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    {
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        sharedPreferences = getApplicationContext().getSharedPreferences("LoginData", MODE_PRIVATE);
        if (savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getParcelable("CHARACTER_LIST_STATE");
            manga = savedInstanceState.getParcelable("manga");
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        manga = enumForManga.getManga();
        System.out.println(manga);
        userName = sharedPreferences.getString("userName", null);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        charactersAdapter = new CharactersAdapter(getApplicationContext(), manga);
       recyclerView.setAdapter(charactersAdapter);
        Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(recyclerViewState);

        updateUI();
        loadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void queryAndUpdateUI() {
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (userName != null) {
            databaseReference.child("favoriteManga").child(userName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Favorite dbStuff = dataSnapshot.getValue(Favorite.class);
                            if (dbStuff != null) {
                                favoriteTitles = dbStuff.getFavoriteTitles();
                            } else {
                                throw new DatabaseException("DB Error");
                            }

                            if (favoriteTitles.contains(manga.getTitle())) {
                                favorite.setColorFilter(yellow);
                                favorite.setSelected(true);
                            } else {
                                favorite.setColorFilter(gray);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("CHARACTER_LIST_STATE", Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState());
        outState.putParcelable("MANGA", manga);
    }

    private void updateUI() {
        StringBuilder builder= new StringBuilder();
        for(int i=0;i<manga.getAuthor().size();i++){
            builder.append(manga.getAuthor().get(i).getName()).append("\n");
        }
        titleTV.setText(manga.getTitle());
        synopsisTV.setText(manga.getSynopsis());
        if(manga.getVolumes()==null){
            numberOfVolumesTV.setText(R.string.unknown);
        }else {
            numberOfVolumesTV.setText(String.valueOf(manga.getVolumes()));
        }
        authorTV.setText(builder.toString());


        Picasso.with(getApplicationContext()).load(manga.getImageUrl())
                .resize(350,450)
                .placeholder(R.drawable.ic_cloud_download_black_24dp)
                .error(R.drawable.common_full_open_on_phone).into(posterIV);

        charactersAdapter = new CharactersAdapter(getApplicationContext(), manga);
        recyclerView.setAdapter(charactersAdapter);
        queryAndUpdateUI();
    }

    private void getPrevOrNextManga(int malId) {
        Call<MangaResponse> call = service.getMangaByID(malId);
        loadingIndicator.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaResponse> call, @NonNull Response<MangaResponse> response) {
                if (response.body() != null) {
                    manga = response.body();
                    enumForManga.setManga(manga);
                    updateUI();
                    loadingIndicator.setVisibility(View.INVISIBLE);
                } else {
                    loadingIndicator.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplication(), getString(R.string.error_on_no_result_from_server), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), getString(R.string.error_on_no_result_from_server), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.favoriteIBT)
    public void addAndRemoveFavorite() {
        if (userName != null) {
            databaseReference.child("favoriteManga").child(userName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Favorite dbStuff = dataSnapshot.getValue(Favorite.class);
                            if (dbStuff != null) {
                                favoriteTitles = dbStuff.getFavoriteTitles();
                            } else {
                                throw new DatabaseException("DB Error");
                            }

                            if (!favoriteTitles.contains(manga.getTitle())) {
                                favoriteTitles.add(manga.getTitle());
                                writeData(userName, favoriteTitles);
                                favorite.setColorFilter(yellow);
                                favorite.setSelected(true);
                            } else {
                                favoriteTitles.remove(manga.getTitle());
                                writeData(userName, favoriteTitles);
                                favorite.setColorFilter(gray);
                                favorite.setSelected(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.login_to_save_favorite), Toast.LENGTH_LONG).show();
        }
    }

    private void writeData(String userName, List<String> favoriteTitles) {
        Favorite input = new Favorite(favoriteTitles);
        Map<String, Object> postValues = input.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/favoriteManga/" + userName, postValues);

        databaseReference.updateChildren(childUpdates);
    }
}