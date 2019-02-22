package com.mangacollection.jblg.app.app;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mangacollection.jblg.app.app.adapter.FavoriteAdapter;
import com.mangacollection.jblg.app.app.models.manga.Favorite;
import com.mangacollection.jblg.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity {

    @BindView(R.id.favoriteRV)
    RecyclerView recyclerView;
    @BindView(R.id.navigationSearch)
    BottomNavigationView navigationSearch;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    private FavoriteWidget widget = new FavoriteWidget();
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private Parcelable recyclerViewState;
    private FavoriteAdapter favoriteAdapter;
    private List<String> favoriteTitle=new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener(){
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
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);
        navigationSearch.setOnNavigationItemSelectedListener(mOnNavigationSelectedListener);
        sharedPreferences=getApplicationContext().getSharedPreferences("LoginData",MODE_PRIVATE);
        final String userName = sharedPreferences.getString("userName",null);

        if(savedInstanceState!=null){
            recyclerViewState=savedInstanceState.getParcelable("FAVORITE_LIST_STATE");
        }
        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        loadingIndicator.setVisibility(View.VISIBLE);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        queryDBFavorite(userName);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback= new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                favoriteAdapter.removeFavorite(viewHolder.getAdapterPosition());
                favoriteTitle=favoriteAdapter.getAllItemsFromList();
                updateDB(userName,favoriteTitle);
                widgetContentUpdate();
            }
            @Override
            public void onChildDraw(Canvas canvas, RecyclerView recyclerView , RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    private void queryDBFavorite(String userName) {

        if(userName!=null){
            databaseReference.child("favoriteManga").child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Favorite dbStuff=dataSnapshot.getValue(Favorite.class);
                    try{
                        favoriteTitle=dbStuff.getFavoriteTitles();
                    }catch (Exception e){
                        e.getLocalizedMessage();
                    }
                    recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    favoriteAdapter = new FavoriteAdapter(favoriteTitle);
                    recyclerView.setAdapter(favoriteAdapter);
                    widgetContentUpdate();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {

            favoriteTitle=null;
            loadingIndicator.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),getString(R.string.please_log_in), Toast.LENGTH_LONG).show();
        }
    }
    private void widgetContentUpdate(){
        sharedPreferences.edit().putString("stringToWidget",formattedFavorite(favoriteTitle)).commit();
        int[] ids= AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(),FavoriteWidget.class));
        widget.onUpdate(getApplicationContext(), AppWidgetManager.getInstance(getApplicationContext()),ids);
        loadingIndicator.setVisibility(View.INVISIBLE);
    }

    private String formattedFavorite(List<String> list){
        String formattedFavorite=getString(R.string.yourFavorite)+ TextUtils.join(", \n-",list);
        return formattedFavorite;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("FAVORITE_LIST_STATE",recyclerView.getLayoutManager().onSaveInstanceState());
    }
    void updateDB(String userName, List<String> favoriteTitle){
        Favorite input = new Favorite(favoriteTitle);
        Map<String,Object> postValue=input.toMap();
        Map<String,Object> childUpdate=new HashMap<>();

        childUpdate.put("/favoriteManga/"+userName,postValue);
        databaseReference.updateChildren(childUpdate);
    }
}
