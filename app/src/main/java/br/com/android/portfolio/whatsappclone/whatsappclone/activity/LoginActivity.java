package br.com.android.portfolio.whatsappclone.whatsappclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.android.portfolio.whatsappclone.whatsappclone.R;
import br.com.android.portfolio.whatsappclone.whatsappclone.helper.Preferences;
import br.com.android.portfolio.whatsappclone.whatsappclone.model.User;
import br.com.android.portfolio.whatsappclone.whatsappclone.utils.Base64Custom;
import br.com.android.portfolio.whatsappclone.whatsappclone.utils.Util;

public class LoginActivity extends BaseActivity{//} AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private User user;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUser;
    private String loggedUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        userIsLoged();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (isFormValid()) {
                        user = new User();
                        user.setEmail( editTextEmail.getText().toString() );
                        user.setPassword( editTextPassword.getText().toString() );
                        validarLogin(user);
                    }
            }
        });


    }

    private void userIsLoged(){
        if (Util.getFirebaseAutenticacao().getCurrentUser() != null){
            openMainScreen();
        }
    }

    public void validarLogin(final User user) {

            FirebaseAuth firebaseAuth = Util.getFirebaseAutenticacao();
            firebaseAuth.signInWithEmailAndPassword(
                    user.getEmail(),
                    user.getPassword()
            ).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    String error = "";
                    if ( task.isSuccessful() ){


                        loggedUserId = Base64Custom.codeBase64(user.getEmail());

                        firebase = Util.getFirebase()
                                .child("users")
                                .child(loggedUserId);

                        valueEventListenerUser = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                User user = dataSnapshot.getValue(User.class);

                                Preferences preferencias = new Preferences(LoginActivity.this);
                                preferencias.salvarDadosUsuario(loggedUserId, user.getName());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        firebase.addListenerForSingleValueEvent(valueEventListenerUser);
                        openMainScreen();
                        Toast.makeText(LoginActivity.this, R.string.userLogedWithSuccess, Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            throw task.getException();
                        }catch( FirebaseAuthInvalidUserException e){
                            error = R.string.invalidEmail+"";
                        }catch( FirebaseAuthInvalidCredentialsException e){
                            error = R.string.invalidPassword+"";
                        }catch( Exception e ){
                            error = R.string.loginGeneralError+"";
                        }
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }

    public void openMainScreen(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class );
        startActivity(intent);
        finish();
    }


    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
        startActivity(intent);
    }

    public void init() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmailId);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswordId);
        buttonLogin = (Button) findViewById(R.id.buttonLoginId);

        mOnValidateListeners.add(new OnValidateListener() {
            @Override
            public boolean validate() {

                if(editTextEmail.getText().length()    == 0 &&  !editTextEmail.getText().toString().contains("@")) return false;
                if(editTextPassword.getText().length()  < 6 ) return false;

                return true;
            }
        });
    }

}
