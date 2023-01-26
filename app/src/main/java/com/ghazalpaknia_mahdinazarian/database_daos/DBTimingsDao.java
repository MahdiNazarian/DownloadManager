package com.ghazalpaknia_mahdinazarian.database_daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.ghazalpaknia_mahdinazarian.database_models.DBTimings;

import java.util.List;

@Dao
public interface DBTimingsDao extends CRUDDao<DBTimings> {
    @Query("SELECT * FROM DBTimings WHERE UserCreatorId=(:userId)")
    List<DBTimings> getAll(int userId);
}
