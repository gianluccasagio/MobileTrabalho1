package com.example.trabalhopratico1.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "computaria.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE = "chamados";
    public static final String COL_ID = "id";
    public static final String COL_TITULO = "titulo";
    public static final String COL_DATA = "data";
    public static final String COL_DESCRICAO = "descricao";
    public static final String COL_LOCAL = "local";
    public static final String COL_TIPO = "tipo";
    public static final String COL_STATUS = "status";
    public static final String COL_SOLUCAO = "solucao";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE + " (" +
                    COL_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TITULO   + " TEXT NOT NULL, " +
                    COL_DATA     + " TEXT NOT NULL, " +
                    COL_DESCRICAO+ " TEXT, " +
                    COL_LOCAL    + " TEXT, " +
                    COL_TIPO     + " TEXT, " +
                    COL_STATUS   + " TEXT, " +
                    COL_SOLUCAO  + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}