package com.ghazalpaknia_mahdinazarian.database_daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.ghazalpaknia_mahdinazarian.database_models.DBDownloadLine;

import java.util.List;

@Dao
public interface DBDownloadLineDao extends CRUDDao<DBDownloadLine> {
    @Query("SELECT * FROM DBDownloadLine WHERE UserCreatorId=(:userId)")
    List<DBDownloadLine> getAll(int userId);
}
