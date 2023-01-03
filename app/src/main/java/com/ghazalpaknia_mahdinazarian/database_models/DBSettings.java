package com.ghazalpaknia_mahdinazarian.database_models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DBSettings {
    @PrimaryKey
    public int Id;
    public String SettingName;
    public String SettingValue;
    public String SettingDescription;
    public int SettingTopicId;
    public int UserCreatorId;
}
