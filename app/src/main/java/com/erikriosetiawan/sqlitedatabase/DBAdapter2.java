package com.erikriosetiawan.sqlitedatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBAdapter2 extends SQLiteOpenHelper {

    // Path to the database folder
    public static String DB_PATH;

    // Database file name;
    public static String DATABASE_NAME;
    public SQLiteDatabase db;
    public Context context;
    private static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";

    public DBAdapter2(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        // Write a full path to the database of your application
        String packageName = context.getPackageName();
        DB_PATH = "/data/data/" + packageName + "/databases/";
        DATABASE_NAME = name;
        this.context = context;
        openDatabase();
    }

    public SQLiteDatabase openDatabase() {
        String path = DB_PATH + DATABASE_NAME;
        if (db == null) {
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        }
        return db;
    }

    // Create a database if it's not created yet
    public void createDatabase() {
        boolean dbExist = checkDatabase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                Log.e(this.getClass().toString(), "Copying error");
                throw new Error("Error copying database!");
            }
        } else {
            Log.i(this.getClass().toString(), "Database already exists!");
        }
    }

    // Performing database existence check
    private boolean checkDatabase() {
        String path = DB_PATH + DATABASE_NAME;
        File dbFile = new File(path);
        return dbFile.exists();
    }

    // Method to copy the database
    private void copyDatabase() throws IOException {
        // Open a stream for reading from the ready-made database
        // The stream source is located in the assets
        InputStream externalDBStream = context.getAssets().open(DATABASE_NAME);
        // Path to the created empty daatabase on your Android device
        String outFileName = DB_PATH + DATABASE_NAME;
        // Create a stream for writing the database byte by by byte
        OutputStream localDBStream = new FileOutputStream(outFileName);
        // Copying the database
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDBStream.read(buffer)) > 0) {
            localDBStream.write(buffer, 0, bytesRead);
        }
        localDBStream.flush();
        // Close the stream
        localDBStream.close();
        externalDBStream.close();
    }

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
        }
        super.close();
    }

    // Retrieves all contacts
    public Cursor getAllContacts() {
        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL}, null, null, null, null, null);
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}