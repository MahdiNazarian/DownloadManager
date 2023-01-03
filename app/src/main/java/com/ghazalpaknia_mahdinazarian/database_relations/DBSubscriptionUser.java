package com.ghazalpaknia_mahdinazarian.database_relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ghazalpaknia_mahdinazarian.database_models.DBSubscription;
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers;

import java.util.List;

public class DBSubscriptionUser {
    @Embedded
    public DBUsers dbUser;
    @Relation(
            parentColumn = "Id",
            entityColumn = "UserId"
    )
    public List<DBSubscription> dbSubscriptionList;
}
