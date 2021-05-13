/*
File: ContactListViewAdapter
Assignment: InClass07
Group: B8
Group Members:
Anisha Kakwani
Hiten Changlani
 */
package com.example.inclass07;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactListViewAdapter extends RecyclerView.Adapter<ContactListViewAdapter.ListViewHolder>{
    ArrayList<Contact> Lists;
    ContactListViewAdapterInterface AppListListener;

    public ContactListViewAdapter(ArrayList<Contact> Data, ContactListViewAdapterInterface contactListListener){
        this.Lists = Data;
        this.AppListListener = contactListListener;
    }
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_layout,parent,false);
        ContactListViewAdapter.ListViewHolder viewHolder = new ContactListViewAdapter.ListViewHolder(view,AppListListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Contact SelectedContact = Lists.get(position);
        holder.contact_name.setText(SelectedContact.name);
        holder.contact_email.setText(SelectedContact.email);
        holder.contact_phone.setText(SelectedContact.phone);
        holder.contact_type.setText(SelectedContact.type);
        holder.position = position;
        holder.contact = SelectedContact;
    }


    @Override
    public int getItemCount() {
        return Lists.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{
        TextView contact_name,contact_email,contact_phone,contact_type;
        int position;
        Contact contact;
        ContactListViewAdapter.ContactListViewAdapterInterface AppListListener;
        public ListViewHolder(@NonNull View itemView, ContactListViewAdapterInterface AppListListener) {
            super(itemView);
            this.AppListListener = AppListListener;
            contact_name = itemView.findViewById(R.id.textView_name);
            contact_email = itemView.findViewById(R.id.textView_email);
            contact_phone = itemView.findViewById(R.id.textView_phone);
            contact_type = itemView.findViewById(R.id.textView_type);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppListListener.get_contact_details(contact);
                }
            });
            itemView.findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppListListener.delete_contact(contact);
                }
            });
        }
    }

    interface ContactListViewAdapterInterface {
        void get_contact_details(Contact contact);
        void delete_contact(Contact contact);
    }
}
