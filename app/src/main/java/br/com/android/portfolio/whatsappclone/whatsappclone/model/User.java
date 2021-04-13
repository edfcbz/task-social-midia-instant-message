package br.com.android.portfolio.whatsappclone.whatsappclone.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.android.portfolio.whatsappclone.whatsappclone.utils.Util;

public class User {

    private String id;
    private String idNew;
    private String name;
    private String email;
    private String phone;

    public String getIdNew() {
        return idNew;
    }

    public void setIdNew(String idNew) {
        this.idNew = idNew;
    }

    private String password;

    public User(){}

    public User(String name, String email, String phone) {
        setName(name);
        setEmail(email);
        setPhone(phone);
    }

    public void save(){
        DatabaseReference databaseReference = Util.getFirebase();
        databaseReference.child("users").child(getId()).setValue(this);
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
