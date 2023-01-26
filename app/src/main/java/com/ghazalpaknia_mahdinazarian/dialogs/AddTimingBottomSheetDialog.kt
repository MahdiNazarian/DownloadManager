package com.ghazalpaknia_mahdinazarian.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ghazalpaknia_mahdinazarian.database.DownloadManagerDatabase
import com.ghazalpaknia_mahdinazarian.database_daos.DBTimingDatesDao
import com.ghazalpaknia_mahdinazarian.database_daos.DBTimingsDao
import com.ghazalpaknia_mahdinazarian.database_daos.DBUserDao
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.*
import java.util.*

class AddTimingBottomSheetDialog : BottomSheetDialogFragment() , AdapterView.OnItemSelectedListener {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope : CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var downloadTypeSelected : Int = 0
    var SignedInUser : DBUsers? = null
    override fun onCreateView(
        inflater : LayoutInflater ,
        container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View? = inflater.inflate(R.layout.add_timing_bottom_sheet , container , false)

    companion object {
        const val TAG = "AddTimingBottomSheet"
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        viewModelScope.launch {
            val db : DownloadManagerDatabase =
                DownloadManagerDatabase.getInstance(context)
            val userDao : DBUserDao = db.dbUserDao()
            withContext(Dispatchers.IO) {
                SignedInUser = userDao.loggedInUser;
            }
        }
        val editTimingIdInput : EditText = view.findViewById(R.id.EditTimingIdInput)
        val addTimingNameInput : EditText = view.findViewById(R.id.AddTimingNameInput)
        val addTimingTypeInput : Spinner = view.findViewById(R.id.AddTimingTypeInput)
        val addTimingStartDownloadStartup : SwitchMaterial = view.findViewById(R.id.AddTimingStartDownloadAtStartup)
        val addTimingStartDateInput : EditText = view.findViewById(R.id.AddTimingStartDateInput)
        val addTimingStartTimeInput : EditText = view.findViewById(R.id.AddTimingStartTimeInput)
        val addTimingEndTimeInput : EditText = view.findViewById(R.id.AddTimingEndTimeInput)
        val addTimingDailyDownloadCheck : SwitchMaterial = view.findViewById(R.id.AddTimingDailyDownloadCheck)
        val addTimingRetryCountInput : EditText = view.findViewById(R.id.AddTimingRetryCountInput)
        val addTimingSpeedRestrictionCheck : SwitchMaterial = view.findViewById(R.id.AddTimingSpeedRestrictionCheck)
        val addTimingDownloadSpeedInput : EditText = view.findViewById(R.id.AddTimingDownloadSpeedInput)
        val addTimingDisconnectInternetCheck : SwitchMaterial = view.findViewById(R.id.AddTimingDisconnectInternetCheck)
        val addTimingCloseIdmCheck : SwitchMaterial = view.findViewById(R.id.AddTimingCloseIdmCheck)
        val addTimingCloseAppsCheck : SwitchMaterial = view.findViewById(R.id.AddTimingCloseAppsCheck)
        val addTimingShutdownCheck : SwitchMaterial = view.findViewById(R.id.AddTimingShutdownCheck)
        val addTimingButton : MaterialButton = view.findViewById(R.id.AddTimingButton)
        val editTimingButton : MaterialButton = view.findViewById(R.id.EditTimingButton)
        var editTimingIdNull : Boolean = false
        var addTimingNameNull : Boolean = false
        var addTimingTypeNull : Boolean = false
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.AddTimingTypesArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            addTimingTypeInput.adapter = adapter
        }
        addTimingButton.setOnClickListener {
            if(addTimingNameInput.text == null || addTimingNameInput.text.toString() == ""){
                addTimingNameInput.error = resources.getString(R.string.AddTimingEmptyNameError);
                addTimingNameNull = true;
            }
            if(addTimingTypeInput.selectedItem == null || addTimingTypeInput.selectedItem == ""){
                addTimingTypeNull = true
            }
            if(!addTimingNameNull && !addTimingTypeNull){
                val db : DownloadManagerDatabase =
                    DownloadManagerDatabase.getInstance(context)
                val timingDao : DBTimingsDao = db.dbTimingsDao()
                val timingDatesDao : DBTimingDatesDao = db.dbTimingDatesDao()
                val insertTiming : DBTimings = DBTimings()
                insertTiming.TimingName = addTimingNameInput.text.toString()
                insertTiming.DownloadType = downloadTypeSelected
                insertTiming.StartDownloadAtProgramStartup = addTimingStartDownloadStartup.isChecked
                if(addTimingStartTimeInput.text == null || addTimingStartTimeInput.text.toString() == "")
                    insertTiming.StartTime = 0
                else
                    insertTiming.StartTime = addTimingStartTimeInput.text.toString()
                        .toLong()
            }
        }
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        when (parent.selectedItemPosition) {
            0 -> this.downloadTypeSelected = 0
            1 -> this.downloadTypeSelected = 1
            else -> this.downloadTypeSelected = 0
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}