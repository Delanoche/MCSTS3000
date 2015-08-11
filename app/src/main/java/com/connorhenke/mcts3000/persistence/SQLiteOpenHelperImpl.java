package com.connorhenke.mcts3000.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.connorhenke.mcts3000.models.Favorite;

import java.util.ArrayList;
import java.util.List;

public class SQLiteOpenHelperImpl extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mcts";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FAVORITES = "favorites";
    private static final String FAVORITES_STOP_ID = "stop_id";
    private static final String FAVORITES_STOP_NAME = "stop_name";
    private static final String FAVORITES_ROUTE_ID = "route_id";
    private static final String FAVORITES_DIRECTION = "direction";

    private static SQLiteOpenHelperImpl sqLiteOpenHelper;

    public SQLiteOpenHelperImpl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized SQLiteOpenHelperImpl getInstance(Context context) {
        if(sqLiteOpenHelper == null) {
            sqLiteOpenHelper = new SQLiteOpenHelperImpl(context);
        }
        return sqLiteOpenHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createFavorites = "CREATE TABLE " + TABLE_FAVORITES + "("
                + FAVORITES_STOP_ID + " TEXT, "
                + FAVORITES_STOP_NAME + " TEXT, " + FAVORITES_ROUTE_ID + " TEXT, "
                + FAVORITES_DIRECTION + " TEXT, "
                + "PRIMARY KEY (" + FAVORITES_STOP_ID + ", " + FAVORITES_ROUTE_ID + "))";
        db.execSQL(createFavorites);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
            onCreate(db);
        }
    }

    public void addFavorite(Favorite favorite) {
        addFavorite(favorite.getStopId(), favorite.getStopName(), favorite.getRouteId(), favorite.getDirection());
    }

    public void addFavorite(String stop, String name, String route, String direction) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues(3);
        values.put(FAVORITES_STOP_ID, stop);
        values.put(FAVORITES_STOP_NAME, name);
        values.put(FAVORITES_ROUTE_ID, route);
        values.put(FAVORITES_DIRECTION, direction);
        long rowId = db.insert(TABLE_FAVORITES, null, values);
        db.close();
        Log.d("SQL", "FAVORITE ADDED");
    }

    public boolean isFavorite(Favorite favorite) {
        SQLiteDatabase db = getReadableDatabase();
        String select = "SELECT " + FAVORITES_STOP_ID + " FROM " + TABLE_FAVORITES;
        Cursor cursor = db.rawQuery(select, null);
        int count = cursor.getCount();
        cursor.close();
        if (count > 0) {
            return true;
        }
        return false;
    }

    public List<Favorite> getFavorites() {
        SQLiteDatabase db = getReadableDatabase();
        String select = "SELECT * FROM " + TABLE_FAVORITES;
        Cursor cursor = db.rawQuery(select, null);
        List<Favorite> favorites = new ArrayList<>(cursor.getCount());
        while(cursor.moveToNext()) {
            String stop = cursor.getString(cursor.getColumnIndex(FAVORITES_STOP_ID));
            String name = cursor.getString(cursor.getColumnIndex(FAVORITES_STOP_NAME));
            String route = cursor.getString(cursor.getColumnIndex(FAVORITES_ROUTE_ID));
            String direction = cursor.getString(cursor.getColumnIndex(FAVORITES_DIRECTION));
            Favorite favorite = new Favorite(stop, name, route, direction);
            favorites.add(favorite);
        }
        cursor.close();
        return favorites;
    }

    public void deleteFavorite(Favorite favorite) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FAVORITES, FAVORITES_STOP_ID + "=? AND " + FAVORITES_ROUTE_ID + "=?", new String[] { favorite.getStopId(), favorite.getRouteId() });
        db.close();
    }
}
