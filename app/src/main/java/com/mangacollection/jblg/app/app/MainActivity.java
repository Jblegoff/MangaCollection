package com.mangacollection.jblg.app.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mangacollection.jblg.app.R;
import com.mangacollection.jblg.app.app.adapter.TrendingMangaAdapter;
import com.mangacollection.jblg.app.app.api.MangaAPI;
import com.mangacollection.jblg.app.app.api.MangaAPIInterface;
import com.mangacollection.jblg.app.app.models.manga.TempEnumForManga;
import com.mangacollection.jblg.app.app.models.manga.manga.MangaResponse;
import com.mangacollection.jblg.app.app.models.manga.search.SearchResponse;
import com.mangacollection.jblg.app.app.models.manga.trending.TrendingResponse;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final int RESULT_CODE_SIGN_IN = 1;
    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.trendingMangaRV)
    RecyclerView recyclerView;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.searchButton)
    Button searchButton;
    @BindView(R.id.searchET)
    EditText searchET;
    @BindView(R.id.loggedInTV)
    TextView loggedInTV;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    private Menu loginMenu;
    private MangaAPIInterface service = MangaAPI.getRetrofit().create(MangaAPIInterface.class);
    private TrendingMangaAdapter trendingMangaAdapter;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private String currentUserName;
    private SharedPreferences sharedPreferences;
    private Parcelable recyclerViewState;
    private Intent intent;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_favorite:
                    intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_surprise:
                    getRandomManga();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getParcelable("TRENDING_LIST_STATE");
        }

        //setSupportActionBar(toolbar);
        invalidateOptionsMenu();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(recyclerViewState);
        sharedPreferences = getApplicationContext().getSharedPreferences("LoginData", MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(String.valueOf(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserName = sharedPreferences.getString("userName", null);
        if (currentUserName != null) {
            loggedInTV.setText(String.format(getString(R.string.logged_in_with_title), currentUserName));

        }

        MobileAds.initialize(this, "1");
        if (checkInternetConnection()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            Call<TrendingResponse> call = service.getTrendingManga();
            getTrendingResponse(call);
        } else {
            loadingIndicator.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), getString(R.string.error_on_no_connection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("TRENDING_LIST_STATE", Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        this.loginMenu = menu;

        sharedPreferences = getApplicationContext().getSharedPreferences("LoginData", MODE_PRIVATE);
        currentUserName = sharedPreferences.getString("userName", null);

        if (currentUserName != null) {
            MenuItem loginField = menu.findItem(R.id.loginField);
            loginField.setTitle(getString(R.string.logout));

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.loginField) {
            signIn(item);
        }

        return super.onOptionsItemSelected(item);
    }


    private void getRandomManga() {
        int id = ThreadLocalRandom.current().nextInt(1, 1000);
        loadingIndicator.setVisibility(View.VISIBLE);
        Call<MangaResponse> call = service.getMangaByID(id);
        call.enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaResponse> call, @NonNull Response<MangaResponse> response) {
                if (response.body() != null) {
                    loadingIndicator.setVisibility(View.INVISIBLE);
                    intent = new Intent(getApplicationContext(), DetailsActivity.class);
                    TempEnumForManga enumForManga = TempEnumForManga.INSTANCE;
                    enumForManga.setManga(response.body());


                    startActivity(intent);
                } else {
                    Toast.makeText(getApplication(), getString(R.string.error_on_no_result_from_server), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), getString(R.string.error_on_no_result_from_server), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getTrendingResponse(Call<TrendingResponse> call) {
        loadingIndicator.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<TrendingResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrendingResponse> call, @NonNull Response<TrendingResponse> response) {
                if (response.body() != null) {
                    TrendingResponse trendingManga = response.body();
                    trendingMangaAdapter = new TrendingMangaAdapter(getApplicationContext(), loadingIndicator, trendingManga);
                    recyclerView.setAdapter(trendingMangaAdapter);
                    loadingIndicator.setVisibility(View.INVISIBLE);
                } else {
                    loadingIndicator.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplication(), getString(R.string.error_on_no_result_from_server), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrendingResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), getString(R.string.error_on_no_result_from_server), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.searchButton)
    public void getSearchResponse() {
        String title = searchET.getText().toString();
        Call<SearchResponse> call = service.searchByMangaTitle(title);
        if (!title.equals("")) {
            loadingIndicator.setVisibility(View.VISIBLE);
            call.enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                    if (response.body() != null) {
                        Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                        intent.putExtra("SEARCH_RESULT", response.body());
                        loadingIndicator.setVisibility(View.INVISIBLE);
                        startActivity(intent);
                    } else {
                        loadingIndicator.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplication(), getString(R.string.error_on_no_result_from_server), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                    Toast.makeText(getApplication(), getString(R.string.error_on_no_result_from_server), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean checkInternetConnection() throws NullPointerException {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager != null) && (connectivityManager.getActiveNetworkInfo() != null);
    }

    @Optional
    @OnClick(R.id.loginField)
    public void signIn(MenuItem item) {
        currentUserName = sharedPreferences.getString("userName", null);

        if (currentUserName == null) {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RESULT_CODE_SIGN_IN);
        } else {
            firebaseAuth.signOut();
            item.setTitle(getString(R.string.login));
            sharedPreferences.edit().putString("userName", null).apply();
            loggedInTV.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CODE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                authWithGoogleThroughFirebase(account);
            } catch (ApiException e) {
                Log.e("TEST", getString(R.string.error_on_login), e);
            }
        }
    }
    private void authWithGoogleThroughFirebase(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && firebaseAuth.getCurrentUser() != null) {
                            currentUserName = firebaseAuth.getCurrentUser().getDisplayName();
                            sharedPreferences.edit().putString("userName", currentUserName).apply();
                            loggedInTV.setText(String.format(getString(R.string.logged_in_with_title), currentUserName));

                            MenuItem login = loginMenu.findItem(R.id.loginField);
                            login.setTitle(getString(R.string.logout));
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_on_authentication), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
