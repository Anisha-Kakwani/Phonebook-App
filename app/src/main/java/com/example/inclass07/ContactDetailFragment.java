/*
File: ContactDetailFragment
Assignment: InClass07
Group: B8
Group Members:
Anisha Kakwani
Hiten Changlani
 */
package com.example.inclass07;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ContactDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private Contact mParam1;
    TextView name,email,phone,type;
    ContactDetailInterface listener;
    OkHttpClient client = new OkHttpClient();
    HttpUrl url;
    Request request;

    // TODO: Rename and change types of parameters


    public ContactDetailFragment() {
        // Required empty public constructor
    }

    public static ContactDetailFragment newInstance(Contact selectedContact) {
        ContactDetailFragment fragment = new ContactDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, selectedContact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Contact)getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_detail, container, false);
        getActivity().setTitle(getResources().getString(R.string.contact_detail_label));
        name = view.findViewById(R.id.textView_name_detail);
        email = view.findViewById(R.id.textView_email_detail);
        phone = view.findViewById(R.id.textView_phone_detail);
        type = view.findViewById(R.id.textView_type_detail);
        name.setText(mParam1.name);
        email.setText(mParam1.email);
        phone.setText(mParam1.phone);
        type.setText(mParam1.type);
        view.findViewById(R.id.button_delete_contact_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.delete_dialog_confirmation)
                        .setPositiveButton(R.string.yes_label, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                url = HttpUrl.parse("https://www.theappsdr.com/contact/delete").newBuilder().build();
                                FormBody formBody = new FormBody.Builder()
                                        .add("id",mParam1.getId())
                                        .build();
                                request = new Request.Builder().url(url).post(formBody).build();
                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                        if (response.isSuccessful()) {
                                            listener.contactDeleted(mParam1);
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getContext(), getResources().getString(R.string.toast_delete_successful), Toast.LENGTH_SHORT).show();
                                                }
                                            });
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
                        })
                        .setNegativeButton(R.string.no_label, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        view.findViewById(R.id.button_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.editContact(mParam1);
            }
        });
        return view;
    }

    interface ContactDetailInterface {
        void contactDeleted(Contact contact);
        void editContact(Contact contact);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ContactDetailFragment.ContactDetailInterface) {
            listener = (ContactDetailFragment.ContactDetailInterface)context;
        } else {
            throw new RuntimeException(context.toString() + "must implement IListener");
        }
    }


}