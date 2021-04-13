package br.com.android.portfolio.whatsappclone.whatsappclone.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import br.com.android.portfolio.whatsappclone.whatsappclone.R;

public abstract class BaseActivity extends AppCompatActivity {

    //private  List<BaseActivity.OnValidateListener> mOnValidateListeners = new ArrayList<>();
    public  List<BaseActivity.OnValidateListener> mOnValidateListeners = new ArrayList<>();

    //Permissões
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult ){

        super.onRequestPermissionsResult(requestCode,permissions,grantResult);
        for ( int result : grantResult ){
            if ( result == PackageManager.PERMISSION_DENIED ){
                alertValidationoPermission();
            }
        }
    }

    public void alertValidationoPermission(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle( getString(R.string.permissionDenied) );
        builder.setMessage( getString(R.string.messagePermissionDenied) );

        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onConfirmActionPermission(dialog,which);
                //finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public boolean checkPermission(int requestCode, Activity activity, String[] permissionList){

        if (Build.VERSION.SDK_INT >= 23 ){
            List<String> permissions = new ArrayList<>();

            //Verifing permissions
            for(String permission : permissionList){
                Boolean checkPermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;

                if( !checkPermission ) permissions.add(permission);
            }

            //If the list is empty, don't need permissions
            if ( permissions.isEmpty() ) return true;

            String[] newPermissions = new String[permissions.size()];
            permissions.toArray(newPermissions);

            //Permissions Request
            ActivityCompat.requestPermissions(activity, newPermissions, requestCode);

        }

        return true;
    }

    public void onConfirmActionPermission(DialogInterface dialog, int which){};


    /**
     * Adds a validation listener.
     *
     * @param onValidateListener On validate listener.
     */
    protected void addValidationListener(final BaseActivity.OnValidateListener onValidateListener) {
        mOnValidateListeners.add(onValidateListener);
    }

    /**
     * Checks if the form is valid.
     *
     * @return Returns true if every field is valid, false otherwise.
     */
    public boolean isFormValid() {
        boolean result = true;

        for (BaseActivity.OnValidateListener onValidateListener : mOnValidateListeners) {
            if (onValidateListener != null) {
                result = result && onValidateListener.validate();
            }
        }

        return result;
    }

    /**
     * On validate listener.
     */
    public interface OnValidateListener {

        /**
         * Validates a given data.
         *
         * @return Returns true if the data is valid, false otherwise.
         */
        boolean validate();

    }



}

