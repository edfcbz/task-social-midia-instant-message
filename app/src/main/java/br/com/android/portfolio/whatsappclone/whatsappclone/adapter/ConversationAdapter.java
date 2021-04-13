package br.com.android.portfolio.whatsappclone.whatsappclone.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.android.portfolio.whatsappclone.whatsappclone.R;
import br.com.android.portfolio.whatsappclone.whatsappclone.model.Conversation;

public class ConversationAdapter extends ArrayAdapter<Conversation>{

    private ArrayList<Conversation> conversas;
    private Context context;


    public ConversationAdapter(@NonNull Context context, @NonNull ArrayList<Conversation> conversas) {
        super(context, 0, conversas);
        this.conversas = conversas;
        this.context   = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        if ( conversas != null ){
            //Inicializa o objeto para a montagem da lista
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_conversas , parent, false);

            //Recupara elemento para a exibição
            TextView nome    = (TextView) view.findViewById(R.id.tv_titulo);
            TextView ultimaMensagem = (TextView) view.findViewById(R.id.tv_subtitulo);

            Conversation conversa = conversas.get(position);
            nome.setText(conversa.getName());
            ultimaMensagem.setText(conversa.getMessage());
        }
        return view;
    }
}
