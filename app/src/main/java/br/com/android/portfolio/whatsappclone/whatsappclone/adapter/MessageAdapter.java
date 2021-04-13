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
import br.com.android.portfolio.whatsappclone.whatsappclone.helper.Preferences;
import br.com.android.portfolio.whatsappclone.whatsappclone.model.Message;

public class MessageAdapter extends ArrayAdapter<Message> {
    private Context context;
    private ArrayList<Message> mensagens;

    public MessageAdapter(@NonNull Context context, @NonNull ArrayList<Message> mensagens) {
        super(context, 0, mensagens);
        this.context   = context;
        this.mensagens = mensagens;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        //Verificar se as mensagens estão diferente de nulas
        if( mensagens != null ){

            //Inicializar os objetos ara a montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Recuperando mensagem reccebida da lista de mensagens
            Message mensagem = mensagens.get(position);

            //Recupenrando o id do usuario que esta enviando mensagens
            String idUsuarioRemetente = new Preferences(context).getIdentificador();

            if ( mensagem.getIdUser().equals(idUsuarioRemetente) ){
                view = inflater.inflate(R.layout.item_mensagem_enviada, parent, false);
            } else{
                view = inflater.inflate(R.layout.item_mensagem_recebida, parent, false);
            }



            //Recuperar elemento para exibição
            TextView textoMensagem =   (TextView) view.findViewById(R.id.textViewMensagemRecebida);
            textoMensagem.setText(mensagem.getMessage());
        }

        return view;
    }
}
