package com.ghazalpaknia_mahdinazarian.database_daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.ghazalpaknia_mahdinazarian.database_models.DBTimingDates;

@Dao
public interface DBTimingDatesDao extends CRUDDao<DBTimingDates> {
    @Query("SELECT * FROM DBTimingDates WHERE TimingId=(:timingId)")
    DBTimingDates getTimingDateByTimingId(int timingId);
}
