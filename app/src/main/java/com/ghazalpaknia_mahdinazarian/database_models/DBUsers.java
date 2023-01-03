package com.ghazalpaknia_mahdinazarian.database_models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
@Entity
public class DBUsers {
    @PrimaryKey
    public int Id;
    public String Email;
    public String Password;
    public String Name;
    public String LastName;
    public Date AccountCreationDate;
}
