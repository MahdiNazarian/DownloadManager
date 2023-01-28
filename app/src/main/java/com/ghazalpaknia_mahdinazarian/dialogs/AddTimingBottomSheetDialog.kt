package com.ghazalpaknia_mahdinazarian.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.ghazalpaknia_mahdinazarian.ViewModels.MainActivityViewModel
import com.ghazalpaknia_mahdinazarian.database.DownloadManagerDatabase
import com.ghazalpaknia_mahdinazarian.database_daos.DBTimingDatesDao
import com.ghazalpaknia_mahdinazarian.database_daos.DBTimingsDao
import com.ghazalpaknia_mahdinazarian.database_daos.DBUserDao
import com.ghazalpaknia_mahdinazarian.database_models.DBTimingDates
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers
import com.ghazalpaknia_mahdinazarian.downloadmanager.MainActivity
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
    private var model : MainActivityViewModel? = null
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
        model = ViewModelProvider(requireActivity() as MainActivity)[MainActivityViewModel::class.java]
        SignedInUser = model!!.singedInUser?.value
        val editTimingIdInput : EditText = view.findViewById(R.id.EditTimingIdInput)
        val addTimingNameInput : EditText = view.findViewById(R.id.AddTimingNameInput)
        val addTimingTypeInput : Spinner = view.findViewById(R.id.AddTimingTypeInput)
        val addTimingStartDownloadStartup : SwitchMaterial = view.findViewById(R.id.AddTimingStartDownloadAtStartup)
        val addTimingStartDateInput : MaterialButton = view.findViewById(R.id.AddTimingStartDateInput)
        val addTimingStartTimeInput : MaterialButton = view.findViewById(R.id.AddTimingStartTimeInput)
        val addTimingEndTimeInput : MaterialButton = view.findViewById(R.id.AddTimingEndTimeInput)
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
        addTimingStartTimeInput.setOnClickListener {
            val timePicker : TimePickerDialogBox = TimePickerDialogBox(it as MaterialButton)
            timePicker.show(requireActivity().supportFragmentManager,"timepicker")
        }
        addTimingEndTimeInput.setOnClickListener {
            val timePicker : TimePickerDialogBox = TimePickerDialogBox(it as MaterialButton)
            timePicker.show(requireActivity().supportFragmentManager,"timepicker")
        }
        addTimingStartDateInput.setOnClickListener {
            val datePicker : DatePickerDialogBox = DatePickerDialogBox(it as MaterialButton)
            datePicker.show(requireActivity().supportFragmentManager,"DatePicker")
        }
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
        addTimingTypeInput.onItemSelectedListener = this
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
                val insertTimingDate : DBTimingDates = DBTimingDates()
                insertTiming.TimingName = addTimingNameInput.text.toString()
                insertTiming.DownloadType = downloadTypeSelected
                insertTiming.StartDownloadAtProgramStartup = addTimingStartDownloadStartup.isChecked
                if(addTimingStartTimeInput.text == null || addTimingStartTimeInput.text.toString() == "" || addTimingStartTimeInput.text == getString(R.string.AddTimingStartTimeInputHint))
                    insertTiming.StartTime = ""
                else
                    insertTiming.StartTime = addTimingStartTimeInput.text.toString()
                if(addTimingEndTimeInput.text == null || addTimingEndTimeInput.text.toString() == "" || addTimingEndTimeInput.text == getString(R.string.AddTimingEndTimeInputHint))
                    insertTiming.FinishTime = ""
                else
                    insertTiming.FinishTime = addTimingEndTimeInput.text.toString()
                insertTiming.DailyDownload = addTimingDailyDownloadCheck.isChecked
                if(addTimingRetryCountInput.text == null || addTimingRetryCountInput.text.toString() == "")
                    insertTiming.RetryCount = 0
                else
                    insertTiming.RetryCount = addTimingRetryCountInput.text.toString().toInt()
                insertTiming.SpeedRestriction = addTimingSpeedRestrictionCheck.isChecked
                if(addTimingDownloadSpeedInput.text == null || addTimingDownloadSpeedInput.text.toString() == "")
                    insertTiming.DownloadSpeed = 0
                else
                    insertTiming.DownloadSpeed = addTimingDownloadSpeedInput.text.toString().toInt()
                insertTiming.DisconnectInternet = addTimingDisconnectInternetCheck.isChecked
                insertTiming.CloseIDM = addTimingCloseIdmCheck.isChecked
                insertTiming.CloseApps = addTimingCloseAppsCheck.isChecked
                insertTiming.ShutdownSystem = addTimingShutdownCheck.isChecked
                insertTiming.DateCreated = Calendar.getInstance().timeInMillis
                if(SignedInUser != null)
                    insertTiming.UserCreatorId = SignedInUser!!.Id
                else
                    insertTiming.UserCreatorId = 0
                viewModelScope.launch {
                    var timings : List<DBTimings>
                    try {
                        withContext(Dispatchers.IO) {
                            val timingId : Long = timingDao.Insert(insertTiming)
                            insertTimingDate.TimingId = timingId.toInt()
                            if (addTimingStartDateInput.text == null || addTimingStartDateInput.text == "" || addTimingStartDateInput.text == getString(
                                    R.string.AddTimingStartDateInputHint
                                )
                            )
                                insertTimingDate.DownloadDate = 0
                            else {
                                val downloadDate : List<String> =
                                    addTimingStartDateInput.text.split("/")
                                val downloadDateYear : Int = downloadDate.first().trim().toInt()
                                val downloadDateMonth : Int =
                                    downloadDate.elementAt(1).trim().toInt() - 1
                                val downloadDateDay : Int = downloadDate.last().trim().toInt()
                                val calendar : Calendar = Calendar.getInstance()
                                calendar.set(
                                    downloadDateYear ,
                                    downloadDateMonth ,
                                    downloadDateDay
                                )
                                insertTimingDate.DownloadDate = calendar.timeInMillis
                            }
                            timingDatesDao.Insert(insertTimingDate)
                            timings = if(SignedInUser!=null) timingDao.getAll(SignedInUser!!.Id) else timingDao.getAll(0)
                        }
                        model!!.timings?.postValue(timings)
                        Toast.makeText(
                            context ,
                            resources.getString(R.string.InsertTimingSuccessMessage) ,
                            Toast.LENGTH_LONG
                        )
                            .show();
                    }catch (e : Exception){
                        timings = if(SignedInUser!=null) timingDao.getAll(SignedInUser!!.Id) else timingDao.getAll(0)
                        model!!.timings?.postValue(timings)
                        Toast.makeText(
                            context ,
                            e.message.toString() ,
                            Toast.LENGTH_LONG
                        )
                            .show();
                    }
                }
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