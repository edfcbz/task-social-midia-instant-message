package br.com.android.portfolio.whatsappclone.whatsappclone.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.android.portfolio.whatsappclone.whatsappclone.R;
import br.com.android.portfolio.whatsappclone.whatsappclone.adapter.TabAdapter;
import br.com.android.portfolio.whatsappclone.whatsappclone.externalclasses.SlidingTabLayout;
import br.com.android.portfolio.whatsappclone.whatsappclone.helper.Preferences;
import br.com.android.portfolio.whatsappclone.whatsappclone.model.Contact;
import br.com.android.portfolio.whatsappclone.whatsappclone.model.User;
import br.com.android.portfolio.whatsappclone.whatsappclone.utils.Base64Custom;
import br.com.android.portfolio.whatsappclone.whatsappclone.utils.Util;

public class MainActivity extends BaseActivity{//AppCompatActivity {

    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String contactId;
    private DatabaseReference firebase;
    private FirebaseAuth usuarioFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolBarTitle);
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorAccent));

        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //Setup adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);

        usuarioFirebase = Util.getFirebaseAutenticacao();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.exitItem:
                logoutUser();
                return true;

            case R.id.researchItem:
                return true;

            case R.id.addItem:
                openContactRegister();
            return true;

            case R.id.configurationItem:
            return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void openContactRegister(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        //Setting dialog
        alertDialog.setTitle(R.string.addNewContact);
        alertDialog.setMessage(R.string.userEmail);
        alertDialog.setCancelable(false);

        //EditText
        final EditText editTextEmail = new EditText(MainActivity.this);
        alertDialog.setView(editTextEmail);

        //Button configuration
        alertDialog.setPositiveButton(R.string.registerButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String emailContato = editTextEmail.getText().toString();
                if ( emailContato.isEmpty() ){
                    Toast.makeText(MainActivity.this, R.string.type_your_email, Toast.LENGTH_SHORT).show();
                }else{
                    //Verifing is contact is app's contact
                    contactId = Base64Custom.codeBase64(emailContato);

                    //Firebase instance
                    firebase = Util.getFirebase().child("users").child(contactId);

                    //Recovering user's data
                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){

                                //Recovering user's data
                                User userContact = dataSnapshot.getValue( User.class );

                                //Recovering user's data -  base64
                                Preferences preferencias = new Preferences(MainActivity.this);
                                String identificadorUsuarioLogado = preferencias.getIdentificador();

                                firebase =  Util.getFirebase();
                                firebase =  firebase.child("contact")
                                                    .child(identificadorUsuarioLogado)
                                                    .child(contactId);

                                Contact contact = new Contact();
                                contact.setContactIdentifier(contactId);
                                contact.setEmail(userContact.getEmail());
                                contact.setName(userContact.getName());

                                firebase.setValue(contact);

                            }else{
                                Toast.makeText(MainActivity.this, R.string.userDoesntExist, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                

            }
        });

        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.create();
        alertDialog.show();

    }

    private void logoutUser(){

        try {
            Util.getFirebaseAutenticacao().signOut();
            Toast.makeText(MainActivity.this, R.string.userLogoutWithSucess, Toast.LENGTH_SHORT);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }catch(Exception e){
            e.printStackTrace();
        }



    }

}
