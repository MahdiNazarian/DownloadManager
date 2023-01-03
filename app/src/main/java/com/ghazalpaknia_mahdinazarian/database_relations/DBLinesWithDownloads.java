package com.ghazalpaknia_mahdinazarian.database_relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ghazalpaknia_mahdinazarian.database_models.DBDownload;
import com.ghazalpaknia_mahdinazarian.database_models.DBDownloadLine;

import java.util.List;

public class DBLinesWithDownloads {
    @Embedded
    public DBDownloadLine dbDownloadLine;
    @Relation(
            parentColumn = "Id",
            entityColumn = "LineId"
    )
    public List<DBDownload> dbDownloadList;
}
