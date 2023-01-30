package com.ghazalpaknia_mahdinazarian.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ghazalpaknia_mahdinazarian.ViewModels.MainActivityViewModel
import com.ghazalpaknia_mahdinazarian.app_models.FileDataModel
import com.ghazalpaknia_mahdinazarian.database_models.DBDownload
import com.ghazalpaknia_mahdinazarian.database_models.DBDownloadLine
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers
import com.ghazalpaknia_mahdinazarian.downloadmanager.MainActivity
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

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
        val downloadLinkStartButton : MaterialButton = view.findViewById(R.id.DownloadLinkStartButton)
        val downloadLinkLaterStartButton : MaterialButton = view.findViewById(R.id.DownloadLinkLaterStartButton)
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