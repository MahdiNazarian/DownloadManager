package com.ghazalpaknia_mahdinazarian.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers

class MainActivityViewModel : ViewModel() {
    val singedInUser: MutableLiveData<DBUsers>? by lazy {
        MutableLiveData<DBUsers>()
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