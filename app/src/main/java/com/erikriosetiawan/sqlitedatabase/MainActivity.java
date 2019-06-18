package com.erikriosetiawan.sqlitedatabase;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editTextId, editTextName, editTextEmail;
    Button buttonAdd, buttonGet, buttonGetAll, buttonUpdate, buttonDelete;

    Button buttonGetAllAssets;
    String DB_NAME = "myContactsDB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextId = findViewById(R.id.edit_text_id);
        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        buttonAdd = findViewById(R.id.button_add_contact);
        buttonGet = findViewById(R.id.button_get_a_contact);
        buttonGetAll = findViewById(R.id.button_get_all_contact);
        buttonUpdate = findViewById(R.id.buton_update_a_contact);
        buttonDelete = findViewById(R.id.button_delete_a_contact);
        buttonGetAllAssets = findViewById(R.id.button_get_all_assets);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAdapter dbAdapter = new DBAdapter(getApplicationContext(), null, null, 1);

                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                Contact contact = new Contact(name, email);

                dbAdapter.onOpen();
                dbAdapter.insertContacts(contact);
                dbAdapter.close();

                editTextName.setText("");
                editTextEmail.setText("");

                displayToast("Sucessfully add a contact!");
            }
        });

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAdapter dbAdapter = new DBAdapter(getApplicationContext(), null, null, 1);

                String name = editTextName.getText().toString();
                dbAdapter.onOpen();
                Contact contact = dbAdapter.getContact(name);

                if (contact != null) {
                    editTextId.setText(String.valueOf(contact.getId()));
                    editTextEmail.setText(contact.getEmail());
                } else {
                    editTextId.setText("No match found!");
                    editTextName.setText("");
                    editTextEmail.setText("");
                }

                dbAdapter.close();
            }
        });

        buttonGetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAdapter dbAdapter = new DBAdapter(getApplicationContext(), null, null, 1);
                dbAdapter.onOpen();
                String string = "All contact:\n";
                Cursor cursor = dbAdapter.getAllContacts();
                cursor.moveToFirst();
                do {
                    string += cursor.getString(0) + " - "
                            + cursor.getString(1) + " - "
                            + cursor.getString(2) + "\n";
                } while (cursor.moveToNext());
                displayToast(string);
                dbAdapter.close();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAdapter dbAdapter = new DBAdapter(getApplicationContext(), null, null, 1);
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                dbAdapter.onOpen();
                dbAdapter.updateContact(Long.parseLong(editTextId.getText().toString()), name, email);
                dbAdapter.close();
                displayToast("Successfully updated a contact!");
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAdapter dbAdapter = new DBAdapter(getApplicationContext(), null, null, 1);
                String name = editTextName.getText().toString();
                dbAdapter.onOpen();
                boolean result = dbAdapter.deleteContact(name);
                if (result) {
                    editTextId.setText("Record deleted");
                    editTextName.setText("");
                    editTextEmail.setText("");
                } else {
                    editTextId.setText("No match found!");
                }
                dbAdapter.close();
            }
        });

        buttonGetAllAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAdapter2 dbAdapter2 = new DBAdapter2(getApplicationContext(), DB_NAME, null, 1);
                dbAdapter2.openDatabase();

                String string = "All contacts:\n";
                Cursor cursor = dbAdapter2.getAllContacts();
                cursor.moveToFirst();
                do {
                    string += cursor.getString(0) + " - "
                            + cursor.getString(1) + " - "
                            + cursor.getString(2) + "\n";
                } while (cursor.moveToNext());
                displayToast(string);
                dbAdapter2.close();
            }
        });
    }

    public void displayToast(String toast) {
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
    }
}
