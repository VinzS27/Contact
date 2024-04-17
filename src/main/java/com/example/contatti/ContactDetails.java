package com.example.contatti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

public class ContactDetails extends AppCompatActivity {

    private TextView number_cd, surname_cd, name_cd, country_cd,birth_cd,reg_cd,tessera_cd;
    private ShapeableImageView logo_cd;
    private String id;
    private DatabaseHelper dbHelper;
    private ActionBar actionBar;
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        //alert dialog
        alertDialog = new AlertDialog.Builder(this);

        //init db
        dbHelper = new DatabaseHelper(this);

        //get data from intent
        Intent intent = getIntent();
        id = intent.getStringExtra("contactId");

        //init view
        number_cd = findViewById(R.id.number_cd);
        name_cd = findViewById(R.id.name_cd);
        surname_cd = findViewById(R.id.surname_cd);
        country_cd = findViewById(R.id.country_cd);
        birth_cd = findViewById(R.id.birthday_cd);
        reg_cd = findViewById(R.id.data_registrazione_cd);
        tessera_cd = findViewById(R.id.tessera_cd);
        logo_cd = findViewById(R.id.logo_image_cd);

        //action bar
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        loadDataById();

    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        int itemId = item.getItemId();
        if(itemId == R.id.delete_menu){
            alertDialog.setTitle("Conferma").setMessage("Vuoi davvero cancellare questo contatto?")
                    .setCancelable(true)
                    .setPositiveButton("Si", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                        dbHelper.deleteContact(id);
                        onResume();
                        finish();
                        Toast.makeText(getApplicationContext(), "Contatto Cancellato.", Toast.LENGTH_SHORT).show();
                    }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel()).show();
        }else{
            finish();
        }
        return true;
    }

    //go back bar
    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }

    //load file from db
    private void loadDataById() {
        String selectQuery =  "SELECT * FROM "+Constants.TABLE_NAME + " WHERE " + Constants.C_ID + " =\"" + id + "\"";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do {
                String logo =  ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_LOGO));
                String name = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME));
                String surname = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_SURNAME));
                String number = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NUMBER));
                String birth = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_BIRTH));
                String country = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_COUNTRY));
                String reg = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_REG));
                String tessera = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_TESSERA));

                //set data

                surname_cd.setText(surname);
                name_cd.setText(name);
                number_cd.setText(number);
                tessera_cd.setText(tessera);
                birth_cd.setText(birth);
                country_cd.setText(country);
                reg_cd.setText(reg);
                actionBar.setTitle(name);

                if (logo.equals("null")){
                    logo_cd.setImageResource(R.drawable.baseline_account_circle_24);
                }else {
                    logo_cd.setImageURI(Uri.parse(logo));
                }

            }while (cursor.moveToNext());
        }
        db.close();
    }
}
