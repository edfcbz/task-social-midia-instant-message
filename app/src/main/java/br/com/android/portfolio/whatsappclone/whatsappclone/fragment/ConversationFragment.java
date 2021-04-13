package br.com.android.portfolio.whatsappclone.whatsappclone.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.android.portfolio.whatsappclone.whatsappclone.R;
import br.com.android.portfolio.whatsappclone.whatsappclone.activity.ConversationActivity;
import br.com.android.portfolio.whatsappclone.whatsappclone.adapter.ConversationAdapter;
import br.com.android.portfolio.whatsappclone.whatsappclone.helper.Preferences;
import br.com.android.portfolio.whatsappclone.whatsappclone.model.Conversation;
import br.com.android.portfolio.whatsappclone.whatsappclone.utils.Base64Custom;
import br.com.android.portfolio.whatsappclone.whatsappclone.utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<Conversation> adapter;
    private ArrayList<Conversation> conversas;
    private ValueEventListener valueEventListenerConversas;
    private DatabaseReference firebase;

    public ConversationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerConversas);
    };

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerConversas);
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        conversas = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        listView = (ListView) view.findViewById(R.id.lv_conversas);

        adapter = new ConversationAdapter(getContext(), conversas);
        loadConversas();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ConversationActivity.class);

                //Recuperando os dados do usuario para serem passados
                Conversation conversation = conversas.get(i);

                //Passando dados para a activity ConversasActivity
                intent.putExtra("nome", conversation.getName());
                intent.putExtra("email", Base64Custom.decodeBase64(conversation.getIdUser())  );

                startActivity(intent);
            }
        });

        return view;
    }

    public void loadConversas() {

        Preferences preferencias = new Preferences(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();
        firebase =    Util.getFirebase()
                .child("conversations")
                .child(identificadorUsuarioLogado);

        if ( firebase != null ){

            valueEventListenerConversas = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    conversas.clear();

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        Conversation conversa = dados.getValue(Conversation.class);
                        if (conversa != null) {
                            conversas.add(conversa);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }else{
            Toast.makeText(getContext(), R.string.errorConnection, Toast.LENGTH_SHORT).show();
        }
    }

}
