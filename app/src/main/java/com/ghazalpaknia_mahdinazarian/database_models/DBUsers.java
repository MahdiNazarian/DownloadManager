package com.ghazalpaknia_mahdinazarian.database_models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;
@Entity
public class DBUsers {
    @PrimaryKey(autoGenerate = true)
    public int Id;
    public String Email;
    public String Password;
    public String Name;
    public String LastName;
    public long AccountCreationDate;
    public boolean loggedIn;
}
