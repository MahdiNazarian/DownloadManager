package com.ghazalpaknia_mahdinazarian.database_models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
@Entity
public class DBTimings {
    @PrimaryKey(autoGenerate = true)
    public int Id;
    public int DownloadType;
    public boolean StartDownloadAtProgramStartup;
    public long StartTime;
    public long FinishTime;
    public boolean DailyDownload;
    public int RetryCount;
    public boolean DisconnectInternet;
    public boolean SpeedRestriction;
    public int DownloadSpeed;
    public boolean ShutdownSystem;
    public boolean CloseApps;
    public long DateCreated;
    public int UserCreatorId;
}
