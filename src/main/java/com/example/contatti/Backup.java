package com.example.contatti;

import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Backup {
    private MainActivity mainActivity;
    public boolean success = false;

    public Backup(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void DbBackup (final DatabaseHelper databaseHelper){
        Date today = new Date();
        SimpleDateFormat format_day = new SimpleDateFormat("ddMMyy");
        SimpleDateFormat format_minute = new SimpleDateFormat("hhmmss");
        String day = format_day.format(today);
        String minute = format_minute.format(today);

        Permissions.verifyStoragePerm(mainActivity);
        //file non trovato
        File folder = new File(Environment.getExternalStorageDirectory()
                +"/Documents/"+mainActivity.getResources()
                .getString(R.string.app_name));
        try{
            success = true;
            if(!folder.exists())
                success = folder.mkdir();
            if(success){
                databaseHelper.Backup(folder+"/"+"backup_"+day+minute+".db");
                Toast.makeText(mainActivity,"Salvato in: "+folder+"/"+"backup_"+day+minute+".db", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(mainActivity,"Impossibile creare cartella backup", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            success = false;
        }
    }

    public void Restore(DatabaseHelper databaseHelper) {
        Permissions.verifyStoragePerm(mainActivity);
        File folder = new File(Environment.getExternalStorageDirectory()
                +"/Documents/"+mainActivity.getResources()
                .getString(R.string.app_name));
        if(folder.exists()){
            final File[] files = folder.listFiles();
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mainActivity, android.R.layout.select_dialog_item);

            for(File file:files)
                arrayAdapter.add(file.getName());

            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle("Restore DB: ");
            builder.setNegativeButton("Annulla", (dialogInterface, i) -> dialogInterface.cancel());
            builder.setAdapter(arrayAdapter,(dialogInterface, i) -> {
                try{
                    databaseHelper.RestoreDB(files[i].getPath());
                    mainActivity.onResume();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(mainActivity,"Impossibile recuperare il backup", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        }else{
            Toast.makeText(mainActivity,"Cartella backup non trovata", Toast.LENGTH_SHORT).show();
        }
    }
}
