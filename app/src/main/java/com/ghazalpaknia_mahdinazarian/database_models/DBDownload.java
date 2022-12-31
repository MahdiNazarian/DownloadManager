package com.ghazalpaknia_mahdinazarian.database_models;

import com.ghazalpaknia_mahdinazarian.static_values.StaticValues;

public class DBDownload {
    public DBDownload(){

    }
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
