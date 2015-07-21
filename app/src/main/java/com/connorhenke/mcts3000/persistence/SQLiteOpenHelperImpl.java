package com.connorhenke.mcts3000.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.connorhenke.mcts3000.models.Favorite;
import com.connorhenke.mcts3000.models.Route;
import com.connorhenke.mcts3000.models.Stop;

import java.util.ArrayList;
import java.util.List;

public class SQLiteOpenHelperImpl extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mcts";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FAVORITES = "favorites";
    private static final String FAVORITES_ID = "id";
    private static final String FAVORITES_STOP_ID = "stop_id";
    private static final String FAVORITES_STOP_NAME = "stop_name";
    private static final String FAVORITES_ROUTE_ID = "route_id";

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
                + FAVORITES_ID + " INTEGER PRIMARY KEY, " + FAVORITES_STOP_ID + " INTEGER, "
                + FAVORITES_STOP_NAME + " TEXT, " + FAVORITES_ROUTE_ID + " TEXT)";
        db.execSQL(createFavorites);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
            onCreate(db);
        }
    }

    public void addFavorite(Stop stop, String route) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues(3);
        values.put(FAVORITES_STOP_ID, stop.getStopId());
        values.put(FAVORITES_STOP_NAME, stop.getStopName());
        values.put(FAVORITES_ROUTE_ID, route);
        long rowId = db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    public List<Favorite> getFavorites() {
        SQLiteDatabase db = getReadableDatabase();
        String select = "SELECT * FROM " + TABLE_FAVORITES;
        Cursor cursor = db.rawQuery(select, null);
        List<Favorite> favorites = new ArrayList<>(cursor.getCount());
        while(cursor.moveToNext()) {
            int stop = cursor.getInt(cursor.getColumnIndex(FAVORITES_STOP_ID));
            String name = cursor.getString(cursor.getColumnIndex(FAVORITES_STOP_NAME));
            String route = cursor.getString(cursor.getColumnIndex(FAVORITES_ROUTE_ID));
            Favorite favorite = new Favorite(stop, name, route);
            favorites.add(favorite);
        }
        cursor.close();
        return favorites;
    }

    public void deleteFavorite(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FAVORITES, FAVORITES_ID + " = ?", new String[] { "" + id });
        db.close();
    }
}
