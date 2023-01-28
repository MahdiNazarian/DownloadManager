package com.ghazalpaknia_mahdinazarian.database_daos;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


public interface CRUDDao<T> {
    @Insert
    long Insert(T obj);
    @Update
    int Update(T obj);
    @Delete
    int Delete(T obj);
    @Insert
    void InsertAll(List<T> objects);
    @Delete
    int DeleteAll(List<T> objects);
    @Update
    int UpdateAll(List<T> objects);
}
