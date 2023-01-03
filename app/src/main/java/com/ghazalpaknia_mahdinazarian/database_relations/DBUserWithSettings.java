package com.ghazalpaknia_mahdinazarian.database_relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ghazalpaknia_mahdinazarian.database_models.DBSettings;
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers;

import java.util.List;

public class DBUserWithSettings {
    @Embedded
    public DBUsers dbUsers;
    @Relation(
            parentColumn = "Id",
            entityColumn = "UserCreatorId"
    )
    public List<DBSettings> dbSettingsList;
}
