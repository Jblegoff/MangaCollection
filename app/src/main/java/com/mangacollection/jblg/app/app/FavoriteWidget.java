package com.mangacollection.jblg.app.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.mangacollection.jblg.app.R;


public class FavoriteWidget extends AppWidgetProvider {

        static SharedPreferences sharedPreferences;
        private String favoriteList =null;

        static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String favoriteList, int appWidgetId){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_list_widget);
            sharedPreferences=context.getSharedPreferences("LoginData", Context.MODE_PRIVATE);
            favoriteList=sharedPreferences.getString("stringToWidget",null);

            if(favoriteList!=null){
                views.setTextViewText(R.id.favoriteOfTheReaderTV,favoriteList);
                Log.i("TESZT","Favorite at the widget:" + favoriteList);
            }
            Intent intent=new Intent(context, MainActivity.class);
            PendingIntent pendingIntent= PendingIntent.getActivity(context,0,intent,0);
            views.setOnClickPendingIntent(R.id.favoriteOfTheReaderTV,pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId,views);
        }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId:appWidgetIds){
            updateAppWidget(context,appWidgetManager,favoriteList,appWidgetId);
        }
    }
}
