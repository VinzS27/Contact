package com.example.contatti;

public class Constants {

    public static final String DB_NAME = "CONTACT_DB";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "CONTACT_TABLE";

    // table fields
    public static final String C_ID = "ID";
    public static final String C_LOGO = "LOGO";
    public static final String C_NAME = "NAME";
    public static final String C_SURNAME = "SURNAME";
    public static final String C_NUMBER = "NUMBER";
    public static final String C_COUNTRY = "COUNTRY";
    public static final String C_BIRTH = "BIRTH_DATE";
    public static final String C_REG = "REG_DATE";
    public static final String C_TESSERA = "TESSERA";

    // query for create table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_LOGO + " TEXT, "
            + C_NUMBER + " TEXT, "
            + C_SURNAME + " TEXT, "
            + C_NAME + " TEXT, "
            + C_COUNTRY + " TEXT, "
            + C_BIRTH + " TEXT, "
            + C_REG + " TEXT, "
            + C_TESSERA + " TEXT"
            + " );";
}
