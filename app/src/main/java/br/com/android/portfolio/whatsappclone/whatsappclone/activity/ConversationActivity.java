package br.com.android.portfolio.whatsappclone.whatsappclone.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.android.portfolio.whatsappclone.whatsappclone.R;
import br.com.android.portfolio.whatsappclone.whatsappclone.adapter.MessageAdapter;
import br.com.android.portfolio.whatsappclone.whatsappclone.helper.Preferences;
import br.com.android.portfolio.whatsappclone.whatsappclone.model.Conversation;
import br.com.android.portfolio.whatsappclone.whatsappclone.model.Message;
import br.com.android.portfolio.whatsappclone.whatsappclone.utils.Base64Custom;
import br.com.android.portfolio.whatsappclone.whatsappclone.utils.Util;

public class ConversationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editTextMessage;
    private ImageButton buttonSend;
    private ListView listView;
    private ArrayList<Message> mensagens;
    private ArrayAdapter<Message> adapter;
    private ValueEventListener valueEventListenerMessage;
    private DatabaseReference firebase;

    //Dados do destinatario
    private String userNameDestination;
    private String userIdDestination;

    //dados do Remetente
    private String userIdSender;
    private String userNameSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);
        init();

        //Dados do usuario logado
        Preferences preferencias = new Preferences(ConversationActivity.this);
        userIdSender = preferencias.getIdentificador();
        userNameSender = preferencias.getNome();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensagem = editTextMessage.getText().toString();
                if ( ! mensagem.isEmpty() ){
                    Message message =  new Message();
                    message.setIdUser(userIdSender);
                    message.setMessage( mensagem );

                    if  ( saveMessage(userIdSender, userIdDestination, message )){
                          saveMessage(userIdDestination, userIdSender, message );

                            Conversation conversa = new Conversation();

                            conversa.setIdUser(userIdDestination);
                            conversa.setName(userNameDestination);
                            conversa.setMessage(mensagem);
                            saveConversation(userIdSender, userIdDestination,conversa);

                            conversa.setIdUser(userIdSender);
                            conversa.setName(userNameSender);
                            conversa.setMessage(mensagem);
                            saveConversation(userIdDestination, userIdSender,conversa);

                            editTextMessage.setText("");
                    }

                }else{
                    Toast.makeText(ConversationActivity.this, R.string.informMessage, Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    private boolean saveConversation(String userIdSender, String userIdDestination, Conversation conversation){
        firebase = Util.getFirebase().child("conversations");
        firebase.child(userIdSender)
                .child(userIdDestination)
                .setValue(conversation);

        return true;
    }

    public boolean saveMessage(String userIdSender, String userIdDestination, Message _message){
            try {
                DatabaseReference firebase = Util.getFirebase().child("messages");
                firebase.child(userIdSender)
                        .child(userIdDestination)
                        .push()
                        .setValue(_message);
                return true;
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void init(){
        toolbar          = (Toolbar) findViewById(R.id.toolbarConversa);
        editTextMessage = (EditText) findViewById(R.id.editTextMensagem);
        buttonSend = (ImageButton) findViewById(R.id.buttonEnviar);
        listView         = (ListView)findViewById(R.id.listViewConversas);


        Bundle extra = getIntent().getExtras();
        if ( extra != null ){
            userNameDestination = extra.getString("nome");
            String emailDestinatario = extra.getString("email");
            userIdDestination = Base64Custom.codeBase64(emailDestinatario);
            userIdSender = new Preferences(ConversationActivity.this).getIdentificador();
        }

        //Setting toolbar
        toolbar.setTitle(userNameDestination);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Build listview e adapter
        mensagens = new ArrayList<>();
        adapter = new MessageAdapter(ConversationActivity.this, mensagens );

        listView.setAdapter(adapter);

        //Recovering the messages from firebase
        loadMessages();


    }

    public void loadMessages(){
        firebase = Util.getFirebase()
                                        .child("messages")
                                        .child(userIdSender)
                                        .child(userIdDestination);

        valueEventListenerMessage = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mensagens.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Message message = dados.getValue(Message.class);
                    mensagens.add(message);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener(valueEventListenerMessage);

    }


    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMessage);
    }
}
