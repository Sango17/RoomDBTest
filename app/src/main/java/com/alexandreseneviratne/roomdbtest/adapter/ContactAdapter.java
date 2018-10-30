package com.alexandreseneviratne.roomdbtest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexandreseneviratne.roomdbtest.MainActivity;
import com.alexandreseneviratne.roomdbtest.R;
import com.alexandreseneviratne.roomdbtest.database.entity.Contact;

import java.util.ArrayList;

/**
 * Created by Alexandre SENEVIRATNE on 30/10/2018.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Contact> contactArrayList;
    private MainActivity mainActivity;

    public ContactAdapter(Context context, ArrayList<Contact> contactArrayList, MainActivity mainActivity) {
        this.context = context;
        this.contactArrayList = contactArrayList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.contact_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Contact contact = contactArrayList.get(i);

        viewHolder.name.setText(contact.getName());
        viewHolder.email.setText(contact.getEmail());
    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.tvName);
            email = (TextView) itemView.findViewById(R.id.tvEmail);
        }
    }
}
