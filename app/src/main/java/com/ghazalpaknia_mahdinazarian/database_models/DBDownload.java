package com.ghazalpaknia_mahdinazarian.database_models;

import androidx.room.Entity;

import com.ghazalpaknia_mahdinazarian.static_values.StaticValues;
@Entity
public class DBDownload {
    public int ID;
    public String fileName;
    public int lineID;
    public int fileSize;
    public String DownloadUrl;
    public StaticValues.DownloadStates downloadState;
    public int remainingTime;
    public int downloadedSize;
    public int progress;
    public boolean speedRestriction;
    public int downloadSpeed;
    public String description;
}
