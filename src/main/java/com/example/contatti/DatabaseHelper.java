package com.example.contatti;

import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.SimpleTimeZone;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table on database
        db.execSQL(Constants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //upgrade table if any structure change in db
        // drop table if exists
        db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME);
        //create table again
        onCreate(db);
    }

    //insert data in database
    public long insertContact(String logo,String number, String surname,String name,String country,
                              String birth_date,String reg_date,String tessera){
        //get writable database to write data on db
        SQLiteDatabase db = this.getWritableDatabase();
        //create ContentValue class object to save data
        ContentValues contentValues = new ContentValues();

        //id will save automatically as we write query
        contentValues.put(Constants.C_LOGO,logo);
        contentValues.put(Constants.C_NUMBER,number);
        contentValues.put(Constants.C_SURNAME,surname);
        contentValues.put(Constants.C_NAME,name);
        contentValues.put(Constants.C_COUNTRY,country);
        contentValues.put(Constants.C_BIRTH,birth_date);
        contentValues.put(Constants.C_REG,reg_date);
        contentValues.put(Constants.C_TESSERA,tessera);

        //insert data in row and return id of record
        long id = db.insert(Constants.TABLE_NAME,null,contentValues);
        db.close();
        return id;
    }

    //update data in database
    public void updateContact(String id,String logo, String number, String surname,String name,String country,
                              String birth_date,String reg_date,String tessera){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.C_ID,id);
        contentValues.put(Constants.C_LOGO,logo);
        contentValues.put(Constants.C_NUMBER,number);
        contentValues.put(Constants.C_SURNAME,surname);
        contentValues.put(Constants.C_NAME,name);
        contentValues.put(Constants.C_COUNTRY,country);
        contentValues.put(Constants.C_BIRTH,birth_date);
        contentValues.put(Constants.C_REG,reg_date);
        contentValues.put(Constants.C_TESSERA,tessera);

        db.update(Constants.TABLE_NAME,contentValues,Constants.C_ID+" =? ",new String[]{id} );
        db.close();
    }

    // delete data by id
    public void deleteContact(String id){
        SQLiteDatabase db =  getWritableDatabase();
        db.delete(Constants.TABLE_NAME,"ID"+" =? ",new String[]{id});
        db.close();
    }

    // delete all data
    public void deleteAllContact(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+Constants.TABLE_NAME);
        db.close();
    }

    //backup contact
    public void Backup(String FileOut){
        final String FileIn = context.getDatabasePath(Constants.DB_NAME).toString();

        try{
            File file = new File(FileIn);
            FileInputStream fileInputStream = new FileInputStream(file);
            OutputStream outputStream = new FileOutputStream(FileOut);

            byte[] buffer = new byte[1024];
            int length;
            while((length = fileInputStream.read(buffer))>0){
                outputStream.write(buffer,0,length);
            }
            outputStream.flush();
            outputStream.close();
            fileInputStream.close();
            Toast.makeText(context,"backup effettuato",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(context,"Impossibile effettuare backup",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void RestoreDB(String path) {
        final String FileOut = context.getDatabasePath(Constants.DB_NAME).toString();
        try{
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(path);
            OutputStream outputStream = new FileOutputStream(FileOut);
            byte[] buffer = new byte[1024];
            int length;
            while((length = fileInputStream.read(buffer))>0){
                outputStream.write(buffer,0,length);
            }
            outputStream.flush();
            outputStream.close();
            fileInputStream.close();
            Toast.makeText(context,"Restore effettuato",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context,"Errore importazione backup",Toast.LENGTH_SHORT).show();
        }
    }

    // get data
    public ArrayList<ContactModel> getAllData(){
        ArrayList<ContactModel> arrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+Constants.TABLE_NAME;

        //get readable db
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        // looping through all record and add to list
        if (cursor.moveToFirst()){
            do {
                ContactModel ContactModel = new ContactModel(
                        // only id is integer type
                        ""+cursor.getInt(cursor.getColumnIndexOrThrow(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_LOGO)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NUMBER)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_SURNAME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_COUNTRY)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_BIRTH)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_REG)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_TESSERA))
                );
                arrayList.add(ContactModel);
            }while (cursor.moveToNext());
        }
        db.close();
        return arrayList;
    }

    // search data in sql Database
    public ArrayList<ContactModel> getSearchContact(String query){

        // it will return arraylist of ContactModel class
        ArrayList<ContactModel> contactList = new ArrayList<>();

        // get readable database
        SQLiteDatabase db = getReadableDatabase();

        //query for search
        String queryToSearch = "SELECT * FROM "+Constants.TABLE_NAME+" WHERE "+ Constants.C_SURNAME +"||"+ Constants.C_NAME + " LIKE '%" +query+"%'";

        Cursor cursor = db.rawQuery(queryToSearch,null);

        // looping through all record and add to list
        if (cursor.moveToFirst()){
            do {
                ContactModel ContactModel = new ContactModel(
                        // only id is integer type
                        ""+cursor.getInt(cursor.getColumnIndexOrThrow(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_LOGO)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NUMBER)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_SURNAME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_COUNTRY)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_BIRTH)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_REG)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_TESSERA))
                );
                contactList.add(ContactModel);
            }while (cursor.moveToNext());
        }
        db.close();
        return contactList;
    }

}