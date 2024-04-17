package com.example.contatti;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class AddContact extends AppCompatActivity {

    private FloatingActionButton Add_image_button, Save_button, Close_button;
    private ShapeableImageView Logo_image_view;
    private EditText number,name,surname,birthday,country,registration_data,tessera;
    private String id, numero, logo, nome, cognome, nascita, paese, data_di_reg, tess;
    private String filename = ".txt";
    private String myData = "";
    private ActionBar actionBar;
    private Uri Logo_uri;
    private DatabaseHelper databaseHelper;
    private Boolean isEditMode;
    private AlertDialog.Builder alertDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //init db
        databaseHelper = new DatabaseHelper(this);

        //bar for position info
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //data to be saved
        registration_data = findViewById(R.id.data_registrazione);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        surname = findViewById(R.id.surname);
        birthday = findViewById(R.id.birthday);
        country = findViewById(R.id.country);
        tessera = findViewById(R.id.tessera);
        Logo_image_view = findViewById(R.id.logo_image);

        //buttons
        Add_image_button = findViewById(R.id.Add_logo_pic);
        Save_button = findViewById(R.id.save_button);
        Close_button = findViewById(R.id.cancel_button);

        //current data
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        registration_data.setText(currentDateTimeString);

        //intent data
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode",false);
        if(isEditMode){
            actionBar.setTitle("Modifica Contatto");

            id = intent.getStringExtra("ID");
            numero = intent.getStringExtra("NUMBER");
            nome = intent.getStringExtra("NAME");
            cognome = intent.getStringExtra("SURNAME");
            paese = intent.getStringExtra("COUNTRY");
            nascita = intent.getStringExtra("BIRTH_DATE");
            data_di_reg = intent.getStringExtra("REG_DATE");
            tess = intent.getStringExtra("TESSERA");
            logo = intent.getStringExtra("LOGO");

            number.setText(numero);
            name.setText(nome);
            surname.setText(cognome);
            country.setText(paese);
            birthday.setText(nascita);
            registration_data.setText(data_di_reg);
            tessera.setText(tess);
            Logo_uri = Uri.parse(logo);

            if(logo.equals("")){
                Logo_image_view.setImageResource(R.drawable.baseline_account_circle_24);
            }else{
                Logo_image_view.setImageURI(Logo_uri);
            }
        }else{
            actionBar.setTitle("Nuovo Contatto");
            number.setText("1");
        }

        //add logo pic
        Add_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddContact.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        Save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData();
            }
        });

        Close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //save data
    private void SaveData() {
        nome = name.getText().toString();
        numero = number.getText().toString();
        cognome = surname.getText().toString();
        paese = country.getText().toString();
        nascita = birthday.getText().toString();
        data_di_reg = registration_data.getText().toString();
        tess = tessera.getText().toString();

        myData = Logo_uri + "\n" + numero +"\n"+ cognome +" "+ nome + "\n" + paese + "\n" + nascita + "\n" +
                data_di_reg + "\n" + tess;

        if (!nome.isEmpty()) {
            if (isEditMode){
            databaseHelper.updateContact(
                    "" + id,
                    "" + Logo_uri,
                    "" + numero,
                    "" + cognome,
                    "" + nome,
                    "" + paese,
                    "" + nascita,
                    "" + data_di_reg,
                    "" + tess
            );
            Toast.makeText(getApplicationContext(), "Modifiche salvate."+id, Toast.LENGTH_SHORT).show();
            //saveToExternal(myData,id+"_"+nome+"_"+cognome+filename);
            finish();
        }else{
                // add mode
                long id = databaseHelper.insertContact(
                        "" + Logo_uri,
                        "" + numero,
                        "" + cognome,
                        "" + nome,
                        "" + paese,
                        "" + nascita,
                        "" + data_di_reg,
                        "" + tess
                );
                Toast.makeText(getApplicationContext(), "Nuovo contatto salvato.", Toast.LENGTH_SHORT).show();
                //saveToExternal(myData,id+"_"+nome+"_"+cognome+filename);
                finish();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Aggiungi un nome.", Toast.LENGTH_SHORT).show();
        }
    }

    //go back bar
    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }

    //saveFilesToExternal
    public void saveToExternal(String myData, String filenametxt){
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            //If it isn't mounted - we can't write into it.
            return;
        }
        //Create a new file that points to the root directory, with the given name:
        File file = new File(getExternalFilesDir(null), filenametxt);

        //This point and below is responsible for the write operation
        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            //second argument of FileOutputStream constructor indicates whether
            //to append or create new file if one exists
            outputStream = new FileOutputStream(file, false);
            outputStream.write(myData.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //add logo pic function
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logo_uri = data.getData();
        Logo_image_view.setImageURI(Logo_uri);
    }
}