/*
File: AddContactFragment
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


public class AddContactFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private Contact mParam1;
    EditText name, email, phone;
    TextView cancel;
    RadioButton rb;
    String type = "";
    RadioGroup rg;
    OkHttpClient client = new OkHttpClient();
    HttpUrl url;
    Request request;
    AddContactFragmentInterface listener;
    String regEx = "^[0-9]*$";

    public AddContactFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        getActivity().setTitle(getResources().getString(R.string.create_contact_label));
        name = view.findViewById(R.id.editText_name_create);
        email = view.findViewById(R.id.editText_email_create);
        phone = view.findViewById(R.id.editText_phone_create);
        rg = view.findViewById(R.id.radioGroup_create);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_Home_create:
                        type = "HOME";
                        break;
                    case R.id.radioButton_cell_create:
                        type = "CELL";
                        break;
                    case R.id.radioButton_office_create:
                        type = "OFFICE";
                        break;
                }
            }
        });
        view.findViewById(R.id.button_update_contact_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || phone.getText().toString().isEmpty() || type.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.missing_field_dialog)
                            .setPositiveButton(R.string.okay_label, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if(!phone.getText().toString().matches(regEx)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.phone_numeric_dialog)
                            .setPositiveButton(R.string.okay_label, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    url = HttpUrl.parse("https://www.theappsdr.com/contact/create").newBuilder().build();
                    FormBody formBody = new FormBody.Builder()
                            .add("name", name.getText().toString())
                            .add("email", email.getText().toString())
                            .add("phone", phone.getText().toString())
                            .add("type", type)
                            .build();
                    request = new Request.Builder().url(url).post(formBody).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                listener.contactCreated();
                                //editContactFragmentListener.contactUpdated(new Contact(mParam1.getId(),name.getText().toString(),email.getText().toString(),phone.getText().toString(),type));
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getContext(), getResources().getString(R.string.error_label) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    });
                }


            }
        });
        cancel = view.findViewById(R.id.textView_cancel_create);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancelCreate();
            }
        });
        return view;
    }
    interface AddContactFragmentInterface {
        void contactCreated();
        void cancelCreate();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddContactFragment.AddContactFragmentInterface) {
            listener = (AddContactFragment.AddContactFragmentInterface)context;
        } else {
            throw new RuntimeException(context.toString() + "must implement IListener");
        }
    }
}
