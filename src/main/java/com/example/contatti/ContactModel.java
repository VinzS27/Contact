package com.example.contatti;

public class ContactModel {
    private String id,logo,number,surname,name,country,birth,reg,tessera;

    // create constructor
    public ContactModel(String id, String logo, String number, String surname, String name, String country, String birth, String reg, String tessera) {
        this.id = id;
        this.logo = logo;
        this.number = number;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.birth = birth;
        this.reg = reg;
        this.tessera = tessera;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getcountry() {
        return country;
    }

    public void setcountry(String country) {
        this.country = country;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getTessera() {
        return tessera;
    }

    public void setTessera(String tessera) {
        this.tessera = tessera;
    }
}
