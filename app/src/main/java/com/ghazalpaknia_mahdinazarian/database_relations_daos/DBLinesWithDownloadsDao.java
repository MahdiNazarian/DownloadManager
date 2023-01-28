package com.ghazalpaknia_mahdinazarian.database_relations_daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.ghazalpaknia_mahdinazarian.database_daos.CRUDDao;
import com.ghazalpaknia_mahdinazarian.database_relations.DBLinesWithDownloads;

@Dao
public interface DBLinesWithDownloadsDao extends CRUDDao<DBLinesWithDownloads> {
    @Query("SELECT * FROM DBDownloadLine WHERE Id=(:Id)")
    DBLinesWithDownloads getLineWithDownloads(int Id);
}
