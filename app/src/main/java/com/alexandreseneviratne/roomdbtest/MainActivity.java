package com.alexandreseneviratne.roomdbtest;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandreseneviratne.roomdbtest.adapter.ContactAdapter;
import com.alexandreseneviratne.roomdbtest.database.ContactAppDatabase;
import com.alexandreseneviratne.roomdbtest.database.entity.Contact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private ContactAppDatabase contactAppDatabase;

    private ContactAdapter contactAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactAppDatabase = Room
                .databaseBuilder(getApplicationContext(), ContactAppDatabase.class, "ContactDB")
                .allowMainThreadQueries()
                .build();

        contactArrayList.addAll(contactAppDatabase.getContactDao().getContacts());

        contactAdapter = new ContactAdapter(this, contactArrayList, MainActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.rvContact);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditContact(false, null, -1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createContact(String name, String email) {
        long id = contactAppDatabase.getContactDao().addContact(new Contact(0, name, email));

        Contact contact = contactAppDatabase.getContactDao().getContact(id);

        if (contact != null) {
            contactArrayList.add(0, contact);
            contactAdapter.notifyDataSetChanged();
        }
    }

    public void updateContact(String name, String email, int position) {
        Contact contact = contactArrayList.get(position);

        contact.setName(name);
        contact.setEmail(email);

        if (contact != null) {
            contactArrayList.set(position, contact);
            contactAppDatabase.getContactDao().updateContact(contact);
        }

        contactAdapter.notifyDataSetChanged();
    }

    public void deleteContact(Contact contact, int position) {
        contactAppDatabase.getContactDao().deleteContact(contact);
        contactArrayList.remove(position);
        contactAdapter.notifyDataSetChanged();
    }

    public void addAndEditContact(final boolean isUpdate, final Contact contact, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View view = layoutInflater.inflate(R.layout.add_contact_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);

        TextView contactTitle = (TextView) view.findViewById(R.id.tvContactTitle);
        final EditText contactName = (EditText) view.findViewById(R.id.etName);
        final EditText contactEmail = (EditText) view.findViewById(R.id.etEmail);

        contactTitle.setText(!isUpdate ? "Add contact" : "Edit Contact");

        if (isUpdate & contact != null) {
            contactName.setText(contact.getName());
            contactEmail.setText(contact.getEmail());
        }

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(!isUpdate ? "Save" : "Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isUpdate) {
                            deleteContact(contact, position);
                        } else {
                            dialogInterface.cancel();
                        }
                    }
                });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(contactName.getText())) {
                    Toast.makeText(MainActivity.this, "Enter contact name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(contactEmail.getText())) {
                    Toast.makeText(MainActivity.this, "Enter contact email", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (isUpdate && contact != null) {
                    updateContact(contactName.getText().toString(), contactEmail.getText().toString(), position);
                } else {
                    createContact(contactName.getText().toString(), contactEmail.getText().toString());
                }
            }
        });
    }
}
