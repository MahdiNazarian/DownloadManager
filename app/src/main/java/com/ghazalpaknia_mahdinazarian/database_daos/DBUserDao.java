package com.ghazalpaknia_mahdinazarian.database_daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ghazalpaknia_mahdinazarian.database_models.DBUsers;

import java.util.List;

@Dao
public interface DBUserDao {
    @Insert
    void InsertUser(DBUsers user);
    @Query("SELECT * FROM DBUsers WHERE Email=(:email)")
    DBUsers getUserByEmail(String email);
}
