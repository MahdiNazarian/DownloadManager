package com.ghazalpaknia_mahdinazarian.database_daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ghazalpaknia_mahdinazarian.database_models.DBUsers;

import java.util.List;

@Dao
public interface DBUserDao extends CRUDDao<DBUsers> {
    @Query("SELECT * FROM DBUsers WHERE Email=(:email)")
    DBUsers getUserByEmail(String email);
    @Query("SELECT * FROM DBUsers WHERE Id=(:Id)")
    DBUsers getUserById(int Id);
    @Query("SELECT * FROM DBUsers WHERE loggedIn=1")
    DBUsers getLoggedInUser();
    @Query("SELECT * FROM DBUsers WHERE Email=(:email) AND Password=(:password)")
    DBUsers getUserByEmailAndPassword(String email, String password);
    @Query("SELECT * FROM DBUsers")
    List<DBUsers> getAll();
}
