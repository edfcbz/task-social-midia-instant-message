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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import java.util.Date;

import br.com.android.portfolio.whatsappclone.whatsappclone.R;
import br.com.android.portfolio.whatsappclone.whatsappclone.helper.Preferences;
import br.com.android.portfolio.whatsappclone.whatsappclone.model.User;
import br.com.android.portfolio.whatsappclone.whatsappclone.utils.Base64Custom;
import br.com.android.portfolio.whatsappclone.whatsappclone.utils.Util;
import br.com.portfolio.common.TestandoModulos;

public class RegisterUserActivity extends BaseActivity{//} AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_register_activity);
        init();
    }

    public void init(){
        editTextName      = (EditText)findViewById(R.id.editTextNameId);
        editTextEmail     = (EditText)findViewById(R.id.editTextEmailId);
        editTextPassword  = (EditText)findViewById(R.id.editTextPasswordId);
        buttonRegister    = (Button) findViewById(R.id.buttonRegisterId);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setName(editTextName.getText().toString());
                user.setEmail(editTextEmail.getText().toString());
                user.setPassword(editTextPassword.getText().toString());
                registerUser(user);
            }
        });
    }

    private void registerUser(final User user) {

        firebaseAuth = Util.getFirebaseAutenticacao();
        firebaseAuth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(RegisterUserActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ){
                    Toast.makeText(RegisterUserActivity.this, R.string.UserRegistedWithSucess, Toast.LENGTH_SHORT).show();
                    String identificadorUsuario = Base64Custom.codeBase64(user.getEmail());
                    user.setId( identificadorUsuario );
                    user.save();

                    Preferences preferences = new Preferences(RegisterUserActivity.this);
                    preferences.salvarDadosUsuario(identificadorUsuario, user.getName());

                    sendToLogin();
                } else{
                    String error = "";

                    try {
                        throw task.getException();
                    }catch( FirebaseAuthWeakPasswordException e){
                            error = R.string.weakPassword+"";
                    }catch( FirebaseAuthInvalidCredentialsException e){
                        error = R.string.invalidEmail+"";
                    }catch( FirebaseAuthUserCollisionException e){
                        error = R.string.usedEmail+"";
                    }catch( Exception e ){
                        error = R.string.registerUserError+"";
                    }
                    Toast.makeText(RegisterUserActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendToLogin(){
        Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
















