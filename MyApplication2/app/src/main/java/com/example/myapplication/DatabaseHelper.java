package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trails.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TRAILS = "trails";
    public static final String COLUMN_TRAIL_ID = "_id";
    public static final String COLUMN_TRAIL_NAME = "name";
    public static final String COLUMN_TRAIL_DATE = "date";

    public static final String TABLE_WAYPOINTS = "waypoints";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TRAIL_FOREIGN_ID = "trail_id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_ALTITUDE = "altitude";

    // Query  tabela
    private static final String TABLE_TRAILS_CREATE =
            "CREATE TABLE " + TABLE_TRAILS + " (" +
                    COLUMN_TRAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TRAIL_NAME + " TEXT, " +
                    COLUMN_TRAIL_DATE + " TEXT);";


    private static final String TABLE_WAYPOINTS_CREATE =
            "CREATE TABLE " + TABLE_WAYPOINTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TRAIL_FOREIGN_ID + " INTEGER, " +
                    COLUMN_LATITUDE + " REAL, " +
                    COLUMN_LONGITUDE + " REAL, " +
                    COLUMN_ALTITUDE + " REAL, " +
                    "FOREIGN KEY(" + COLUMN_TRAIL_FOREIGN_ID + ") REFERENCES " + TABLE_TRAILS + "(" + COLUMN_TRAIL_ID + "));";

    // Construtor da classe
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Quando o banco de dados é criado pela primeira vez
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_TRAILS_CREATE);
        db.execSQL(TABLE_WAYPOINTS_CREATE);
    }

    // Método chamado quando há uma atualização de versão do banco de dados
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WAYPOINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAILS);
        onCreate(db);
    }
}