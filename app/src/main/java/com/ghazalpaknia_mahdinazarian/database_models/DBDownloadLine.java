package com.ghazalpaknia_mahdinazarian.database_models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class DBDownloadLine {
    @PrimaryKey
    public int Id;
    public String Name;
    public Date DateCreated;
    public int TimingId;
    public int UserCreatorId;
}
