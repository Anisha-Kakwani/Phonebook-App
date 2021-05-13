/*
File: Contact
Assignment: InClass07
Group: B8
Group Members:
Anisha Kakwani
Hiten Changlani
 */
package com.example.inclass07;

import java.io.Serializable;

public class Contact implements Serializable {
    String id,name,email,phone,type;

    public Contact(String id, String name, String email, String phone, String type)  {
        this.id=id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }
}
