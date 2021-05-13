/*
File: ContactListFragment
Assignment: InClass07
Group: B8
Group Members:
Anisha Kakwani
Hiten Changlani
 */
package com.example.inclass07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ContactListFragment extends Fragment implements ContactListViewAdapter.ContactListViewAdapterInterface{
    OkHttpClient client = new OkHttpClient();
    HttpUrl url;
    Request request;
    RecyclerView recyclerView;
    ContactListViewAdapter adapter;
    LinearLayoutManager layoutManager;
    ArrayList<Contact> contacts_List = new ArrayList<>();
    ContactListInterface listener;

    public ContactListFragment(){
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        getActivity().setTitle(getResources().getString(R.string.contact_list_label));
        recyclerView = view.findViewById(R.id.recyclerView_contact_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getContactList();
        view.findViewById(R.id.button_add_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.add_new_contact();
            }
        });
        return view;
    }

    void getContactList(){
        url = HttpUrl.parse("https://www.theappsdr.com/contacts").newBuilder().build();

        request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    String[] contactList_string  = body.split("\n");

                    if(body!=""){
                        contacts_List.removeAll(contacts_List);
                        Log.d("list",contactList_string.length+"");
                        for (String value:contactList_string){
                            String[] contact = value.split(",");
                            contacts_List.add(new Contact(contact[0],contact[1],contact[2],contact[3],contact[4]));
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new ContactListViewAdapter(contacts_List,ContactListFragment.this);
                                recyclerView.setAdapter(adapter);
                            }
                        });
                    }
                    else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new ContactListViewAdapter(new ArrayList<>(),ContactListFragment.this);
                                recyclerView.setAdapter(adapter);
                                Toast.makeText(getActivity(), getResources().getString(R.string.zero_contacts_label) , Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), getResources().getString(R.string.error_message) , Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), getResources().getString(R.string.error_label) + e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ContactListFragment.ContactListInterface) {
            listener = (ContactListFragment.ContactListInterface)context;
        } else {
            throw new RuntimeException(context.toString() + "must implement IListener");
        }
    }


    @Override
    public void get_contact_details(Contact contact) {
        listener.get_contact_details(contact);
    }

    @Override
    public void delete_contact(Contact contact) {
        // delete contact and update adapter
        url = HttpUrl.parse("https://www.theappsdr.com/contact/delete").newBuilder().build();
        FormBody formBody = new FormBody.Builder()
                .add("id",contact.getId())
                .build();
        request = new Request.Builder().url(url).post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    contacts_List.removeAll(contacts_List);
                    Log.d("list",contacts_List.size()+"delete");
                    getContactList();

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), getResources().getString(R.string.error_message) , Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), getResources().getString(R.string.error_label) + e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    interface ContactListInterface {
        void get_contact_details(Contact contact);
        void add_new_contact();
    }

}