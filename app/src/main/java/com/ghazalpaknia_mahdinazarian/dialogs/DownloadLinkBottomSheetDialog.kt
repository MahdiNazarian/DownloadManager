package com.ghazalpaknia_mahdinazarian.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ghazalpaknia_mahdinazarian.ViewModels.MainActivityViewModel
import com.ghazalpaknia_mahdinazarian.app_models.FileDataModel
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.ghazalpaknia_mahdinazarian.static_values.MimeTypes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

class DownloadLinkBottomSheetDialog : BottomSheetDialogFragment() {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope : CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    override fun onCreateView(
        inflater : LayoutInflater ,
        container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View? = inflater.inflate(R.layout.download_link_bottom_sheet , container , false)

    companion object {
        const val TAG = "DownloadLineBottomSheet"
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        val downloadLinkInput : EditText = view.findViewById(R.id.DownloadLinkInput)
        val addDownloadLinkSubmitButton : MaterialButton = view.findViewById(R.id.AddDownloadLinkSubmitButton)
        val downloadLinkProgressBar : ProgressBar = view.findViewById(R.id.DownloadLinkProgressBar)
        addDownloadLinkSubmitButton.setOnClickListener {
            viewModelScope.launch {
                downloadLinkInput.visibility = View.GONE
                downloadLinkProgressBar.visibility = View.VISIBLE
                var fileData : FileDataModel
                    try {
                        withContext(Dispatchers.IO) {
                            val url : URL = URL(downloadLinkInput.text.toString())
                            val connection : HttpURLConnection =
                                url.openConnection() as HttpURLConnection
                            fileData = FileDataModel(
                                downloadLinkInput.text.toString() ,
                                downloadLinkInput.text.toString().split("/").last() ,
                                MimeTypes.getMimeType(connection.contentType)
                            )
                        }
                        dismiss()
                        val submitDownloadBottomSheetDialog : BottomSheetDialogFragment = SubmitDownloadBottomSheetDialog(fileData)
                        submitDownloadBottomSheetDialog.show(requireActivity().supportFragmentManager , submitDownloadBottomSheetDialog.tag)
                    }catch (e : Exception){
                        downloadLinkInput.visibility = View.VISIBLE
                        downloadLinkProgressBar.visibility = View.GONE
                        Toast.makeText(
                            context ,
                            e.message ,
                            Toast.LENGTH_LONG
                        )
                            .show();
                    }
            }
        }
    }
}