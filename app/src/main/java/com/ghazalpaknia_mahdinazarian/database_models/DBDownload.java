package com.ghazalpaknia_mahdinazarian.database_models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ghazalpaknia_mahdinazarian.static_values.StaticValues;

import java.util.Date;

@Entity
public class DBDownload {
    @PrimaryKey(autoGenerate = true)
    public int Id;
    public String FileName;
    public int FileSize;
    public String DownloadUrl;
    public StaticValues.DownloadStates DownloadState;
    public int RemainingTime;
    public int DownloadedSize;
    public int Progress;
    public boolean SpeedRestriction;
    public int DownloadSpeed;
    public String Description;
    public long DownloadStartDate;
    public long DownloadEndData;
    public int UserId;
    public int LineId;
}
