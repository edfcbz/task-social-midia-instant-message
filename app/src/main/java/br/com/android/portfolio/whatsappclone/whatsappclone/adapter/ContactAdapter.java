package br.com.android.portfolio.whatsappclone.whatsappclone.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.android.portfolio.whatsappclone.whatsappclone.R;
import br.com.android.portfolio.whatsappclone.whatsappclone.model.Contact;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> contatos;
    private Context context;

    public ContactAdapter(@NonNull Context context, @NonNull ArrayList<Contact> contatos) {
        super(context, 0, contatos);
        this.contatos = contatos;
        this.context  = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View view = null;

        //Verifica se a lista de contatos está vazia
        if (contatos != null) {
            //Inicializa o objeto para a montagem da lista
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_contato, parent, false);

            //Recupara elemento para a exibição
            TextView contactName  = (TextView) view.findViewById(R.id.textViewNome);
            TextView emailContact = (TextView) view.findViewById(R.id.textViewEmail);

            Contact contact = contatos.get(position);
            contactName.setText(contact.getName());
            emailContact.setText(contact.getEmail());

        }

        return view;
    }

}
