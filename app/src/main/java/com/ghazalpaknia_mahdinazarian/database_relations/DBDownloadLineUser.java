package com.ghazalpaknia_mahdinazarian.database_relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ghazalpaknia_mahdinazarian.database_models.DBDownloadLine;
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers;

import java.util.Date;
import java.util.List;

public class DBDownloadLineUser {
    @Embedded
    public DBUsers dbUser;
    @Relation(
            parentColumn = "Id",
            entityColumn = "UserCreatorId"
    )
    public List<DBDownloadLine> dbDownloadLineList;
}
