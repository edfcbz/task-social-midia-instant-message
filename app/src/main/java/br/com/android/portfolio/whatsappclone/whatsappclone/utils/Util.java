package br.com.android.portfolio.whatsappclone.whatsappclone.utils;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Random;



public class Util {

    private static DatabaseReference firebaseReference;
    private static FirebaseAuth autentication;



    public static boolean sendSMS(String phone, String message){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone,null,message,null,null);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static String tokenGeneration(){
        Random random = new Random();
        int randomicNumber = random.nextInt(9999 - 1000) + 1000;
        return String.valueOf(randomicNumber);

    }

    public static DatabaseReference getFirebase(){

        if (firebaseReference == null){
            firebaseReference =  FirebaseDatabase.getInstance().getReference();
        }
        return firebaseReference;
    }




    public static FirebaseAuth getFirebaseAutenticacao(){

        if ( autentication == null){
            autentication =  FirebaseAuth.getInstance();
        }
        return autentication;
    }

}
