package com.ghazalpaknia_mahdinazarian.database_models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
@Entity
public class DBTimingDates {
    @PrimaryKey
    public int Id;
    public int TimingId;
    public Date DownloadDate;
}
