package com.example.contatti;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton NewButton;
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private ContactAdapter contactAdapter;
    private AlertDialog.Builder alertDialog;
    private Backup backup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Backup
        backup = new Backup(this);

        //alert dialog
        alertDialog = new AlertDialog.Builder(this);

        //databasehelper
        databaseHelper = new DatabaseHelper(this);

        //card view
        recyclerView = findViewById(R.id.contact_rv);
        recyclerView.setHasFixedSize(true);

        //action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle("Contatti Soci");
        //actionBar.hide();

        //init new button
        NewButton = findViewById(R.id.NewButton);
        NewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddContact.class);
                intent.putExtra("isEditMode",false);
                startActivity(intent);
            }
        });
        loadData();
    }

    //Create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        //add search on menu
        MenuItem item = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                return true;
            }
        });
        return true;
    }

    private void search(String s) {
        contactAdapter = new ContactAdapter(this,databaseHelper.getSearchContact(s));
        recyclerView.setAdapter(contactAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        int itemId = item.getItemId();
        if(itemId == R.id.delete_all){
            alertDialog.setTitle("Cancella").setMessage("Vuoi davvero cancellare tutti i contatti?")
                    .setCancelable(true)
                    .setPositiveButton("Si", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                        databaseHelper.deleteAllContact();
                        onResume();
                    }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel()).show();
        }else if (itemId == R.id.backup){
            alertDialog.setTitle("Backup").setMessage("salvare tutti i contatti?")
                    .setCancelable(true)
                    .setPositiveButton("Si", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                        //backup
                        requestPermissionsBackup();
                        onResume();
                    }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel()).show();
        }else if (itemId == R.id.restore){
            alertDialog.setTitle("Restore").setMessage("Caricare contatti?")
                    .setCancelable(true)
                    .setPositiveButton("Si", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                        //restore
                        backup.Restore(databaseHelper);
                        onResume();
                    }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel()).show();
        }else{}
        return true;
    }

    private void requestPermissionsBackup() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },786);
            backup.DbBackup(databaseHelper);
        }else{
            backup.DbBackup(databaseHelper);
        }
    }

    private void loadData() {
        contactAdapter = new ContactAdapter(this,databaseHelper.getAllData());
        recyclerView.setAdapter(contactAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); //for refresh data
    }
}