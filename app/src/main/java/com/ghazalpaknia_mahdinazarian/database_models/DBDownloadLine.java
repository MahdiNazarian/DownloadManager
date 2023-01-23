package com.ghazalpaknia_mahdinazarian.database_models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class DBDownloadLine {
    @PrimaryKey(autoGenerate = true)
    public int Id;
    public String Name;
    public long DateCreated;
    public int TimingId;
    public int UserCreatorId;
}
