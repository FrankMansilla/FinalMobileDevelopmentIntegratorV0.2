package com.example.finalmobiledevelopmentintegratorv02;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Constantes para la base de datos
    private static final String DATABASE_NAME = "user_credentials.db";
    private static final int DATABASE_VERSION = 2;

    // tabla y columnas para la seccion Profile
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    //Tabla y culumnas para la seccion Buy
    public static final String TABLE_FLIGHTS_PURCHASED = "FlightsPurchased";
    public static final String COLUMN_ORIGIN = "origin";
    public static final String COLUMN_DESTINATION = "destination";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla "users"
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL);");

        // Crear la tabla "FlightsPurchased"
        db.execSQL("CREATE TABLE " + TABLE_FLIGHTS_PURCHASED + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORIGIN + " TEXT NOT NULL, " +
                COLUMN_DESTINATION + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla existente y crear una nueva en caso de actualización de la base de datos
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLIGHTS_PURCHASED);
        onCreate(db);
    }
}
