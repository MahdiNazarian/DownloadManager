package com.ghazalpaknia_mahdinazarian.database_models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DBSettingsTopics {
    @PrimaryKey
    public int Id;
    public String SettingTopicName;
}
