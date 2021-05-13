/*
File: MainActivity
Assignment: InClass07
Group: B8
Group Members:
Anisha Kakwani
Hiten Changlani
 */

package com.example.inclass07;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ContactListFragment.ContactListInterface, ContactDetailFragment.ContactDetailInterface, EditContactFragment.EditContactFragmentInterface, AddContactFragment.AddContactFragmentInterface {
    public Boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer,new ContactListFragment(),"ContactList")
                .commit();

    }

    @Override
    public void get_contact_details(Contact contact) {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,ContactDetailFragment.newInstance(contact))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void add_new_contact() {
        Log.d("Demo","Add contact button clicked");
        flag = true;
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,new AddContactFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void contactDeleted(Contact contact) {
        Log.d("Demo","Delete Button Clicked from inside detail screen" );
        flag = true;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,new ContactListFragment(),"ContactList")
                .commit();
    }

    @Override
    public void editContact(Contact contact) {
        Log.d("Demo","Edit Button Clicked" );
//        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,EditContactFragment.newInstance(contact))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void contactUpdated(Contact contact) {
        Log.d("Demo","Update button clicked from Edit contact screen");
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,ContactDetailFragment.newInstance(contact))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancelContactUpdate() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void contactCreated() {
        flag = true;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,new ContactListFragment())
                .commit();
    }

    @Override
    public void cancelCreate() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {
        ContactListFragment fragmentView = (ContactListFragment) getSupportFragmentManager().findFragmentByTag("ListContact");
        if (fragmentView != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentView).commit();
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 && flag) {
            flag = false;
            getSupportFragmentManager().popBackStack();
        }
        super.onBackPressed();
    }
}