package com.ghazalpaknia_mahdinazarian.database_models;

import java.util.Date;

public class DBTimings {
    public int ID;
    public int downloadType;
    public boolean startDownloadAtProgramStartup;
    public Date startTime;
    public Date finishTime;
    public boolean dailyDownload;
    public int retryCount;
    public boolean disconnectInternet;
    public boolean speedRestriction;
    public int downloadSpeed;
    public boolean shutdownSystem;
    public boolean closeApps;
}
