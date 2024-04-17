package com.example.contatti;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.Manifest;
import androidx.core.app.ActivityCompat;

public class Permissions {

    public static String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePerm(Activity activity){
        int writePerm = ActivityCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPerm = ActivityCompat.checkSelfPermission(activity,Manifest.permission.READ_EXTERNAL_STORAGE);

        if(writePerm != PackageManager.PERMISSION_DENIED || readPerm != PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(activity,PERMISSION_STORAGE,2);
        }
    }
}
