package com.mangacollection.jblg.app.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.mangacollection.jblg.app.app.adapter.SearchResultAdapter;
import com.mangacollection.jblg.app.app.models.manga.search.SearchResponse;
import com.mangacollection.jblg.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultActivity extends AppCompatActivity {
    @BindView(R.id.searchResultRV)
    RecyclerView recyclerView;
    @BindView(R.id.navigationSearch)
    BottomNavigationView navigationSearch;
    @BindView(R.id.loading_indicator)
    ProgressBar loading_indicator;

    private Parcelable recyclerViewState;
    private SearchResultAdapter searchResultAdapter;
    private SearchResponse manga;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_home_from_search_and_favorite:
                            finish();
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        navigationSearch.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        if(savedInstanceState!=null){
            recyclerViewState=savedInstanceState.getParcelable("SEARCH_RESULT_LIST_STATE");
        }
        manga=getIntent().getExtras().getParcelable("SEARCH_RESULT");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        searchResultAdapter=new SearchResultAdapter(getApplicationContext(),loading_indicator,manga);
        recyclerView.setAdapter(searchResultAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("SEARCH_RESULT_LIST_STATE",recyclerView.getLayoutManager().onSaveInstanceState());
    }
}
