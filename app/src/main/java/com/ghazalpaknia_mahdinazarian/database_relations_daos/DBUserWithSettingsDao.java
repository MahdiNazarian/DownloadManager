package com.ghazalpaknia_mahdinazarian.database_relations_daos;

import androidx.room.Dao;

import com.ghazalpaknia_mahdinazarian.database_daos.CRUDDao;
import com.ghazalpaknia_mahdinazarian.database_relations.DBUserWithSettings;

@Dao
public interface DBUserWithSettingsDao extends CRUDDao<DBUserWithSettings> {
}
