package com.ghazalpaknia_mahdinazarian.database_daos;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


public interface CRUDDao<T> {
    @Insert
    void Insert(T obj);
    @Update
    void Update(T obj);
    @Delete
    void Delete(T obj);
}
