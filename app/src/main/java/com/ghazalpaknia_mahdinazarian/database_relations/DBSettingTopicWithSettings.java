package com.ghazalpaknia_mahdinazarian.database_relations;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import com.ghazalpaknia_mahdinazarian.database_models.DBSettings;
import com.ghazalpaknia_mahdinazarian.database_models.DBSettingsTopics;

import java.util.List;


public class DBSettingTopicWithSettings {
    @Embedded
    public DBSettingsTopics dbSettingsTopics;
    @Relation(
            parentColumn = "Id",
            entityColumn = "SettingTopicId"
    )
    public List<DBSettings> dbSettingsList;
}
