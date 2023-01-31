package com.ghazalpaknia_mahdinazarian.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.ghazalpaknia_mahdinazarian.ViewModels.MainActivityViewModel
import com.ghazalpaknia_mahdinazarian.app_models.FileDataModel
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.ghazalpaknia_mahdinazarian.static_values.MimeTypes
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

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
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        val downloadLinkInput : EditText = view.findViewById(R.id.DownloadLinkInput)
        val addDownloadLinkSubmitButton : MaterialButton = view.findViewById(R.id.AddDownloadLinkSubmitButton)
        val downloadLinkProgressBar : ProgressBar = view.findViewById(R.id.DownloadLinkProgressBar)
        addDownloadLinkSubmitButton.setOnClickListener {
            viewModelScope.launch {
                var fileData : FileDataModel
                addDownloadLinkSubmitButton.visibility = View.GONE
                downloadLinkProgressBar.visibility = View.VISIBLE
                try {
                    withContext(Dispatchers.IO) {
                        val url : URL = URL(downloadLinkInput.text.toString())
                        val connection : URLConnection =
                            url.openConnection() as URLConnection
                        connection.setRequestProperty("Accept-Encoding", "identity");
                        connection.connect()
                        val contentType : String = connection.contentType.split(";").first()
                        val contentLength : Long = connection.getHeaderFieldLong("Content-Length",-1)
                            fileData = FileDataModel(
                            downloadLinkInput.text.toString() ,
                            File(url.path).nameWithoutExtension ,
                            MimeTypes.getDefaultExt(contentType),
                            contentLength
                        )
                    }
                    dismiss()
                    val submitDownloadBottomSheetDialog : BottomSheetDialogFragment = SubmitDownloadBottomSheetDialog(fileData)
                    submitDownloadBottomSheetDialog.show(requireActivity().supportFragmentManager , submitDownloadBottomSheetDialog.tag)
                }catch (e : Exception){
                    addDownloadLinkSubmitButton.visibility = View.VISIBLE
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