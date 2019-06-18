package com.erikriosetiawan.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "myContactsDB.db";
    private static final String TABLE_CONTACTS = "contacts";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";

    SQLiteDatabase db;

    public DBAdapter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS
                + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_EMAIL + " TEXT NOT NULL"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
        Log.w("DBAdapter", "Upgrading database from version " + oldVersion
                + " to " + newVersion + ", which will destroy all old data");
    }

    public void onOpen() {
        super.onOpen(db);
        db = this.getWritableDatabase();
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    // Inserts a contacts to the database
    public void insertContacts(Contact contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, contact.getName());
        contentValues.put(COLUMN_EMAIL, contact.getEmail());

        db.insert(TABLE_CONTACTS, null, contentValues);
    }

    // Retrieves a particular contact
    public Contact getContact(String name) {
        String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE "
                + COLUMN_NAME + " = \"" + name + "\"";

        Cursor cursor = db.rawQuery(query, null);
        Contact contact = new Contact();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            contact.setId(Integer.parseInt(cursor.getString(0)));
            contact.setName(cursor.getString(1));
            contact.setName(cursor.getString(2));
            cursor.close();
        } else {
            contact = null;

        }
        return contact;
    }

    // Retrieve all contacts
    public Cursor getAllContacts() {
        Cursor cursor = db.query(TABLE_CONTACTS,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL}, null, null, null, null, null);
        return cursor;
    }

    // Updates a contact
    public boolean updateContact(long rowID, String newName, String newEmail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, newName);
        contentValues.put(COLUMN_EMAIL, newEmail);
        return db.update(TABLE_CONTACTS, contentValues, COLUMN_ID + " = "
                + rowID, null) > 0;
    }

    // Deletes a particular contact
    public boolean deleteContact(String name) {
        boolean result = false;
        String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE "
                + COLUMN_NAME + " = \"" + name + "\"";
        Cursor cursor = db.rawQuery(query, null);
        Contact contact = new Contact();
        if (cursor.moveToFirst()) {
            contact.setId(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(contact.getId())});
            cursor.close();
            result = true;
        }
        return result;
    }
}
