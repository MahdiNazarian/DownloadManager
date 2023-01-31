package com.ghazalpaknia_mahdinazarian.database_daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.ghazalpaknia_mahdinazarian.database_models.DBDownload;

import java.util.List;

@Dao
public interface DBDownloadDao extends CRUDDao<DBDownload> {
    @Query("SELECT * FROM DBDownload WHERE UserId=(:userId)")
    List<DBDownload> getAll(int userId);
    @Query("SELECT * FROM DBDownload WHERE Id=(:Id)")
    DBDownload getById(int Id);
}
