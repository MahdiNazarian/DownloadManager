package com.ghazalpaknia_mahdinazarian.database_relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ghazalpaknia_mahdinazarian.database_models.DBDownload;
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers;

import java.util.Date;
import java.util.List;

public class DBDownloadUser {
    @Embedded
    public DBUsers dbUser;
    @Relation(
            parentColumn = "Id",
            entityColumn = "UserId"
    )
    public List<DBDownload> dbDownloads;
}
