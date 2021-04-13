package br.com.android.portfolio.whatsappclone.whatsappclone.utils;

import android.util.Base64;

public class Base64Custom {

    public static String codeBase64(String text){
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }

    public static String decodeBase64(String codedText){
        return new String( Base64.decode(codedText, Base64.DEFAULT ) );
    }

}
