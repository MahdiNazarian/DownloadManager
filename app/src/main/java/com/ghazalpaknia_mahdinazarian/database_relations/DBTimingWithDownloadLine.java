package com.ghazalpaknia_mahdinazarian.database_relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ghazalpaknia_mahdinazarian.database_models.DBDownloadLine;
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings;

import java.util.List;

public class DBTimingWithDownloadLine {
    @Embedded
    public DBTimings dbTimings;
    @Relation(
            parentColumn = "Id",
            entityColumn = "TimingId"
    )
    public List<DBDownloadLine> dbDownloadLineList;
}
