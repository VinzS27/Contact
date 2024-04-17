package com.example.contatti;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;


//Adapter used for show data in recyclerView
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private ArrayList<ContactModel> contactList;
    private DatabaseHelper dbHelper;

    //constructor
    public ContactAdapter(Context context, ArrayList<ContactModel> contactList) {
        this.context = context;
        this.contactList = contactList;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact,parent,false);
        ContactViewHolder vh = new ContactViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        ContactModel modelContact = contactList.get(position);

        String id = modelContact.getId();
        String logo = modelContact.getLogo();
        String number = modelContact.getNumber();
        String name = modelContact.getName();
        String surname = modelContact.getSurname();
        String country = modelContact.getcountry();
        String birth = modelContact.getBirth();
        String reg = modelContact.getReg();
        String tessera = modelContact.getTessera();

        //set data in view
        holder.contactName.setText(surname+" "+name);
        if (logo.equals("")){
            holder.contactLogo.setImageResource(R.drawable.baseline_account_circle_24);
        }else {
            holder.contactLogo.setImageURI(Uri.parse(logo));
        }

        //handle item click and show contact details
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create intent to move to contactsDetails Activity with contact id as reference
                Intent intent = new Intent(context,ContactDetails.class);
                intent.putExtra("contactId",id);
                context.startActivity(intent); // now get data from details Activity
                Toast.makeText(context, surname+" "+name, Toast.LENGTH_SHORT).show();
            }
        });

       // handle editBtn click
        holder.contactEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create intent to move AddContact to update data
                Intent intent = new Intent(context,AddContact.class);
                //pass the value of current position
                intent.putExtra("ID",id);
                intent.putExtra("NUMBER",number);
                intent.putExtra("SURNAME",surname);
                intent.putExtra("NAME",name);
                intent.putExtra("PHONE",country);
                intent.putExtra("BIRTH_DATE",birth);
                intent.putExtra("REG_DATE",reg);
                intent.putExtra("TESSERA",tessera);
                intent.putExtra("LOGO",logo);

                // pass a boolean data to define it is for edit purpose
                intent.putExtra("isEditMode",true);

                //start intent
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView contactLogo;
        TextView contactName;
        ImageButton contactEdit;
        RelativeLayout relativeLayout;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            contactLogo = itemView.findViewById(R.id.contact_image);
            contactName = itemView.findViewById(R.id.contact_name);
            contactEdit = itemView.findViewById(R.id.edit_c);
            relativeLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}