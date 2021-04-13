package br.com.android.portfolio.whatsappclone.whatsappclone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.android.portfolio.whatsappclone.whatsappclone.R;
import br.com.android.portfolio.whatsappclone.whatsappclone.helper.Preferences;

public class ValidatorActivity extends BaseActivity{ //AppCompatActivity {

    private EditText editTextCodeValidation;
    private Button buttonVerify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validator);

        editTextCodeValidation = (EditText) findViewById(R.id.editTextCodeValidationId);
        buttonVerify = (Button) findViewById(R.id.buttonVerifyId);

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

    }
}
