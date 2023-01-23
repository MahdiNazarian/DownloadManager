package com.ghazalpaknia_mahdinazarian.database_models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class DBSubscription {
    @PrimaryKey(autoGenerate = true)
    public int Id;
    public int SubscriptionType;
    public int SubscriptionStatus;
    public long SubscriptionStartDate;
    public long SubscriptionEndDate;
    public int UserId;
}
