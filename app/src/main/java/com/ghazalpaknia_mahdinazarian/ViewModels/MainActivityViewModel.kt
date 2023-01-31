package com.ghazalpaknia_mahdinazarian.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ghazalpaknia_mahdinazarian.database_models.DBDownload
import com.ghazalpaknia_mahdinazarian.database_models.DBDownloadLine
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers

class MainActivityViewModel : ViewModel() {
    val singedInUser: MutableLiveData<DBUsers>? by lazy {
        MutableLiveData<DBUsers>()
    }
    val timings : MutableLiveData<List<DBTimings>>? by lazy {
        MutableLiveData<List<DBTimings>>()
    }
    val downloadLines : MutableLiveData<List<DBDownloadLine>>? by lazy {
        MutableLiveData<List<DBDownloadLine>>()
    }
    val downloads : MutableLiveData<List<DBDownload>>? by lazy {
        MutableLiveData<List<DBDownload>>()
    }
    @Volatile
    private var INSTANCE : MainActivityViewModel? = null

    fun getInstance() : MainActivityViewModel? {
        if (INSTANCE == null) {
            synchronized(this) {
                if (INSTANCE == null)
                    INSTANCE = MainActivityViewModel()
            }
        }
        return INSTANCE
    }
}