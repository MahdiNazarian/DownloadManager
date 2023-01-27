package com.ghazalpaknia_mahdinazarian.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings

class TimingsViewModel : ViewModel() {
    val timings: MutableLiveData<List<DBTimings>> by lazy {
        MutableLiveData<List<DBTimings>>()
    }
}