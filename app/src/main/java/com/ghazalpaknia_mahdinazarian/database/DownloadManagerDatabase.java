package com.ghazalpaknia_mahdinazarian.database;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ghazalpaknia_mahdinazarian.database_daos.DBDownloadDao;
import com.ghazalpaknia_mahdinazarian.database_daos.DBDownloadLineDao;
import com.ghazalpaknia_mahdinazarian.database_daos.DBSettingsDao;
import com.ghazalpaknia_mahdinazarian.database_daos.DBSettingsTopicsDao;
import com.ghazalpaknia_mahdinazarian.database_daos.DBSubscriptionDao;
import com.ghazalpaknia_mahdinazarian.database_daos.DBTimingDatesDao;
import com.ghazalpaknia_mahdinazarian.database_daos.DBTimingsDao;
import com.ghazalpaknia_mahdinazarian.database_daos.DBUserDao;
import com.ghazalpaknia_mahdinazarian.database_models.DBDownload;
import com.ghazalpaknia_mahdinazarian.database_models.DBDownloadLine;
import com.ghazalpaknia_mahdinazarian.database_models.DBSettings;
import com.ghazalpaknia_mahdinazarian.database_models.DBSettingsTopics;
import com.ghazalpaknia_mahdinazarian.database_models.DBSubscription;
import com.ghazalpaknia_mahdinazarian.database_models.DBTimingDates;
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings;
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers;
import com.ghazalpaknia_mahdinazarian.database_relations.DBDownloadLineUser;
import com.ghazalpaknia_mahdinazarian.database_relations.DBDownloadUser;
import com.ghazalpaknia_mahdinazarian.database_relations.DBLinesWithDownloads;
import com.ghazalpaknia_mahdinazarian.database_relations.DBSettingTopicWithSettings;
import com.ghazalpaknia_mahdinazarian.database_relations.DBSubscriptionUser;
import com.ghazalpaknia_mahdinazarian.database_relations.DBTimingUser;
import com.ghazalpaknia_mahdinazarian.database_relations.DBTimingWithDownloadLine;
import com.ghazalpaknia_mahdinazarian.database_relations.DBTimingsWithDates;
import com.ghazalpaknia_mahdinazarian.database_relations.DBUserWithSettings;
import com.ghazalpaknia_mahdinazarian.database_relations_daos.DBDownloadLineUserDao;
import com.ghazalpaknia_mahdinazarian.database_relations_daos.DBDownloadUserDao;
import com.ghazalpaknia_mahdinazarian.database_relations_daos.DBLinesWithDownloadsDao;
import com.ghazalpaknia_mahdinazarian.database_relations_daos.DBSettingTopicWithSettingsDao;
import com.ghazalpaknia_mahdinazarian.database_relations_daos.DBSubscriptionUserDao;
import com.ghazalpaknia_mahdinazarian.database_relations_daos.DBTimingUserDao;
import com.ghazalpaknia_mahdinazarian.database_relations_daos.DBTimingWithDownloadLineDao;
import com.ghazalpaknia_mahdinazarian.database_relations_daos.DBTimingsWithDatesDao;
import com.ghazalpaknia_mahdinazarian.database_relations_daos.DBUserWithSettingsDao;

@Database(entities = {DBUsers.class, DBTimings.class, DBTimingDates.class , DBSubscription.class,
        DBSettingsTopics.class , DBSettings.class, DBDownloadLine.class ,
        DBDownload.class}, version = 1)
public abstract class DownloadManagerDatabase extends RoomDatabase {
    public abstract DBUserDao dbUserDao();
    public abstract DBTimingsDao dbTimingsDao();
    public abstract DBTimingDatesDao dbTimingDatesDao();
    public abstract DBSubscriptionDao dbSubscriptionDao();
    public abstract DBSettingsTopicsDao dbSettingsTopicsDao();
    public abstract DBSettingsDao dbSettingsDao();
    public abstract DBDownloadLineDao dbDownloadLineDao();
    public abstract DBDownloadDao dbDownloadDao();
    public abstract DBUserWithSettingsDao dbUserWithSettingsDao();
    public abstract DBTimingWithDownloadLineDao dbTimingWithDownloadLineDao();
    public abstract DBTimingUserDao dbTimingUserDao();
    public abstract DBTimingsWithDatesDao dbTimingsWithDatesDao();
    public abstract DBSubscriptionUserDao dbSubscriptionUserDao();
    public abstract DBSettingTopicWithSettingsDao dbSettingTopicWithSettingsDao();
    public abstract DBLinesWithDownloadsDao dbLinesWithDownloadsDao();
    public abstract DBDownloadUserDao dbDownloadUserDao();
    public abstract DBDownloadLineUserDao dbDownloadLineUserDao();
    private static volatile DownloadManagerDatabase INSTANCE = null;

    public static DownloadManagerDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (DownloadManagerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            DownloadManagerDatabase.class, "DownloadManagerDatabase")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
