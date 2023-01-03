package com.ghazalpaknia_mahdinazarian.database_models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class DBSubscription {
    @PrimaryKey
    public int Id;
    public int SubscriptionType;
    public int SubscriptionStatus;
    public Date SubscriptionStartDate;
    public Date SubscriptionEndDate;
    public int UserId;
}
