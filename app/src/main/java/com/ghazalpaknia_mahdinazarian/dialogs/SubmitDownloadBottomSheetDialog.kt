package com.ghazalpaknia_mahdinazarian.dialogs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ghazalpaknia_mahdinazarian.ViewModels.MainActivityViewModel
import com.ghazalpaknia_mahdinazarian.app_models.FileDataModel
import com.ghazalpaknia_mahdinazarian.database.DownloadManagerDatabase
import com.ghazalpaknia_mahdinazarian.database_daos.DBDownloadDao
import com.ghazalpaknia_mahdinazarian.database_models.DBDownload
import com.ghazalpaknia_mahdinazarian.database_models.DBDownloadLine
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers
import com.ghazalpaknia_mahdinazarian.downloadmanager.MainActivity
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.ghazalpaknia_mahdinazarian.services.DownloadService
import com.ghazalpaknia_mahdinazarian.static_values.Helpers
import com.ghazalpaknia_mahdinazarian.static_values.StaticValues
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*
import java.util.Calendar

class SubmitDownloadBottomSheetDialog (private var fileData: FileDataModel? ) : BottomSheetDialogFragment() , AdapterView.OnItemSelectedListener{
    private val viewModelJob = SupervisorJob()
    private val viewModelScope : CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var SignedInUser : DBUsers? = null
    private var model : MainActivityViewModel? = null
    private var arrayAdapter : ArrayAdapter<String>? = null
    private var submitDownloadLineValue : DBDownloadLine? = null
    override fun onCreateView(
        inflater : LayoutInflater ,
        container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View? = inflater.inflate(R.layout.submit_download_bottom_sheet , container , false)

    companion object {
        const val TAG = "SubmitDownloadBottomSheet"
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        model = ViewModelProvider(requireActivity() as MainActivity)[MainActivityViewModel::class.java]
        SignedInUser = model!!.singedInUser?.value
        val downloadFileLinkInput : EditText = view.findViewById(R.id.DownloadFileLinkInput)
        val downloadLinkFileNameInput : EditText = view.findViewById(R.id.DownloadLinkFileNameInput)
        val downloadLinkFileTypeInput : EditText = view.findViewById(R.id.DownloadLinkFileTypeInput)
        val downloadLinkDownloadLineSelectInput : Spinner = view.findViewById(R.id.DownloadLinkDownloadLineSelectInput)
        val downloadLinkFileSizeInput : EditText = view.findViewById(R.id.DownloadLinkFileSizeInput)
        val downloadLinkStartButton : MaterialButton = view.findViewById(R.id.DownloadLinkStartButton)
        val downloadLinkLaterStartButton : MaterialButton = view.findViewById(R.id.DownloadLinkLaterStartButton)
        downloadFileLinkInput.setText(fileData?.url)
        downloadLinkFileNameInput.setText(fileData?.name)
        downloadLinkFileTypeInput.setText(fileData?.type)
        setSubmitDownloadLinesItems(downloadLinkDownloadLineSelectInput)
        downloadLinkFileSizeInput.setText(Helpers.byteConverter(fileData?.length,false))
        downloadLinkLaterStartButton.setOnClickListener {
            if(downloadLinkFileNameInput.text.toString() != null && downloadLinkFileNameInput.text.toString() != ""){
                val db : DownloadManagerDatabase =
                    DownloadManagerDatabase.getInstance(context)
                val dbDownloadDao : DBDownloadDao = db.dbDownloadDao()
                val newDownload : DBDownload = DBDownload()
                newDownload.FileName = downloadLinkFileNameInput.text.toString().replace("%","")+"."+downloadLinkFileTypeInput.text.toString()
                newDownload.FileSize = if(fileData != null) fileData!!.length else 0
                newDownload.DownloadUrl = downloadFileLinkInput.text.toString()
                newDownload.DownloadState = StaticValues.DownloadStates.PAUSED
                newDownload.RemainingTime = 0
                newDownload.DownloadedSize = 0
                newDownload.Progress = 0
                newDownload.SpeedRestriction = false
                newDownload.DownloadSpeed = 0
                newDownload.Description = ""
                newDownload.DownloadStartDate = Calendar.getInstance().timeInMillis
                newDownload.DownloadEndData = 0
                if(submitDownloadLineValue != null)
                    newDownload.LineId = submitDownloadLineValue!!.Id
                else
                    newDownload.LineId = 0
                if(model!!.singedInUser?.value != null)
                    newDownload.UserId = model!!.singedInUser?.value!!.Id
                else
                    newDownload.UserId = 0
                viewModelScope.launch {
                    var downloads : List<DBDownload>
                    try {
                        withContext(Dispatchers.IO) {
                            dbDownloadDao.Insert(newDownload)
                            downloads = if(SignedInUser!=null) dbDownloadDao.getAll(SignedInUser!!.Id) else dbDownloadDao.getAll(0)
                        }
                        model!!.downloads?.postValue(downloads)
                        Toast.makeText(
                            context ,
                            resources.getString(R.string.AddDownloadSuccessMessage) ,
                            Toast.LENGTH_LONG
                        )
                            .show();
                    }catch (e : Exception){
                        withContext(Dispatchers.IO) {
                            downloads = if(SignedInUser!=null) dbDownloadDao.getAll(SignedInUser!!.Id) else dbDownloadDao.getAll(0)
                        }
                        model!!.downloads?.postValue(downloads)
                        Toast.makeText(
                            context ,
                            e.message ,
                            Toast.LENGTH_LONG
                        )
                            .show();
                    }
                }
            }else{
                downloadLinkFileNameInput.error = "نام فایل نمی تواند خالی باشد.";
            }
        }
        downloadLinkStartButton.setOnClickListener {
            if(downloadLinkFileNameInput.text.toString() != null && downloadLinkFileNameInput.text.toString() != ""){
                val db : DownloadManagerDatabase =
                    DownloadManagerDatabase.getInstance(context)
                val dbDownloadDao : DBDownloadDao = db.dbDownloadDao()
                val newDownload : DBDownload = DBDownload()
                newDownload.FileName = downloadLinkFileNameInput.text.toString().replace("%","")+"."+downloadLinkFileTypeInput.text.toString()
                newDownload.FileSize = if(fileData != null) fileData!!.length else 0
                newDownload.DownloadUrl = downloadFileLinkInput.text.toString()
                newDownload.DownloadState = StaticValues.DownloadStates.PAUSED
                newDownload.RemainingTime = 0
                newDownload.DownloadedSize = 0
                newDownload.Progress = 0
                newDownload.SpeedRestriction = false
                newDownload.DownloadSpeed = 0
                newDownload.Description = ""
                newDownload.DownloadStartDate = Calendar.getInstance().timeInMillis
                newDownload.DownloadEndData = 0
                if(submitDownloadLineValue != null)
                    newDownload.LineId = submitDownloadLineValue!!.Id
                else
                    newDownload.LineId = 0
                if(model!!.singedInUser?.value != null)
                    newDownload.UserId = model!!.singedInUser?.value!!.Id
                else
                    newDownload.UserId = 0
                viewModelScope.launch {
                    var downloads : List<DBDownload>
                    var submittedDownloadId : Long
                    try {
                        withContext(Dispatchers.IO) {
                            submittedDownloadId = dbDownloadDao.Insert(newDownload)
                            downloads = if(SignedInUser!=null) dbDownloadDao.getAll(SignedInUser!!.Id) else dbDownloadDao.getAll(0)
                        }
                        val startDownloadIntent : Intent = Intent(context as MainActivity,DownloadService::class.java)
                        startDownloadIntent.putExtra(StaticValues.DownloadServiceGetIdExtraKeyName,submittedDownloadId)
                        (context as MainActivity).startService(startDownloadIntent)
                        model!!.downloads?.postValue(downloads)
                        Toast.makeText(
                            context ,
                            resources.getString(R.string.AddDownloadSuccessMessage) ,
                            Toast.LENGTH_LONG
                        )
                            .show();
                    }catch (e : Exception){
                        withContext(Dispatchers.IO) {
                            downloads = if(SignedInUser!=null) dbDownloadDao.getAll(SignedInUser!!.Id) else dbDownloadDao.getAll(0)
                        }
                        model!!.downloads?.postValue(downloads)
                        Toast.makeText(
                            context ,
                            e.message ,
                            Toast.LENGTH_LONG
                        )
                            .show();
                    }
                }
            }else{
                downloadLinkFileNameInput.error = "نام فایل نمی تواند خالی باشد.";
            }
        }
        val downloadLinesObserver = Observer<List<DBTimings>> {
            setSubmitDownloadLinesItems(downloadLinkDownloadLineSelectInput)
        }
        model!!.timings?.observe(viewLifecycleOwner, downloadLinesObserver)
    }
    private fun setSubmitDownloadLinesItems(spinner : Spinner){
        val downloadLines : List<DBDownloadLine>? = model!!.downloadLines?.value
        val downloadLinesNames : MutableList<String> = mutableListOf<String>()
        downloadLines?.forEach {
            downloadLinesNames.add(it.Name)
        }
        downloadLinesNames.add("هیچکدام")
        arrayAdapter= ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            downloadLinesNames.toList()
        )
        arrayAdapter?.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = this
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        submitDownloadLineValue =
            if(model!!.downloadLines?.value != null)
                if(pos < model!!.downloadLines!!.value!!.count())
                    model!!.downloadLines?.value?.get(pos)
                else null
            else null
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}