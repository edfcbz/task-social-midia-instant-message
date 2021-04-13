package br.com.android.portfolio.whatsappclone.whatsappclone.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private Context contect;
    private SharedPreferences preferences;
    private final String FILE_NAME = "whatappclone.preference";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR  = "identificadorUsuarioLogado";
    private final String CHAVE_NOME  = "nomeUsuarioLogado";


    public Preferences(Context contextoParametro){
        this.contect = contextoParametro;
        preferences = this.contect.getSharedPreferences(FILE_NAME,MODE);
        editor = preferences.edit();
    }

    public void salvarDadosUsuario(String identificadorUsuario, String nomeUsuario){
        editor.putString(CHAVE_IDENTIFICADOR,identificadorUsuario);
        editor.putString(CHAVE_NOME,nomeUsuario);
        editor.commit();
    }

    public String getIdentificador(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome(){
        return preferences.getString(CHAVE_NOME, null);
    }

}
