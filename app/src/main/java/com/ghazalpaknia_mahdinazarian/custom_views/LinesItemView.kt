package com.ghazalpaknia_mahdinazarian.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.ghazalpaknia_mahdinazarian.ViewModels.MainActivityViewModel
import com.ghazalpaknia_mahdinazarian.database.DownloadManagerDatabase
import com.ghazalpaknia_mahdinazarian.database_daos.DBDownloadDao
import com.ghazalpaknia_mahdinazarian.database_daos.DBDownloadLineDao
import com.ghazalpaknia_mahdinazarian.database_daos.DBTimingDatesDao
import com.ghazalpaknia_mahdinazarian.database_daos.DBTimingsDao
import com.ghazalpaknia_mahdinazarian.database_models.DBDownload
import com.ghazalpaknia_mahdinazarian.database_models.DBDownloadLine
import com.ghazalpaknia_mahdinazarian.database_models.DBTimingDates
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.database_relations.DBLinesWithDownloads
import com.ghazalpaknia_mahdinazarian.database_relations_daos.DBLinesWithDownloadsDao
import com.ghazalpaknia_mahdinazarian.dialogs.AddLineBottomSheetDialog
import com.ghazalpaknia_mahdinazarian.downloadmanager.MainActivity
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.ghazalpaknia_mahdinazarian.static_values.Helpers
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*

class LinesItemView(
    context: Context,
    attrs: AttributeSet? = null,
)  : FrameLayout(context,attrs) , AdapterView.OnItemSelectedListener{
    private lateinit var lineItemData : DBDownloadLine
    private var lineItemFrame : FrameLayout
    private var lineItemMainLayout : ConstraintLayout
    private var lineItemMainCard : CardView
    private var lineItemLayout : ConstraintLayout
    private var lineItemName : TextView
    private var lineItemDescription : TextView
    private var lineItemActions : Spinner
    private val viewModelJob = SupervisorJob()
    private val viewModelScope : CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var model: MainActivityViewModel?
    init {
        inflate(context , R.layout.lines_list_item , this)
        lineItemFrame = findViewById(R.id.LineItemFrame)
        lineItemMainLayout = findViewById(R.id.LineItemMainLayout)
        lineItemMainCard = findViewById(R.id.LineItemCard)
        lineItemLayout = findViewById(R.id.LineItemLayout)
        lineItemName = findViewById(R.id.LineItemName)
        lineItemDescription = findViewById(R.id.LineItemDescription)
        lineItemActions = findViewById(R.id.LineItemActions)
        model = ViewModelProvider(context  as MainActivity)[MainActivityViewModel::class.java]
    }
    fun setLineItem(lineItemData : DBDownloadLine){
        this.lineItemData = lineItemData
        ArrayAdapter.createFromResource(
            context ,
            R.array.DownloadLinesSpinnerItemsArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            lineItemActions.adapter = adapter
        }
        lineItemActions.onItemSelectedListener = this
    }
    fun setLineItemsValue(){
        lineItemName.text = this.lineItemData.Name.toString()
        viewModelScope.launch {
            val db : DownloadManagerDatabase =
                DownloadManagerDatabase.getInstance(context)
            val dbLineWithDownloadDao = db.dbLinesWithDownloadsDao()
            var dbLineWithDownload : DBLinesWithDownloads
            withContext(Dispatchers.IO){
                dbLineWithDownload = dbLineWithDownloadDao.getLineWithDownloads(lineItemData.Id)
            }
            lineItemDescription.text = dbLineWithDownload.dbDownloadList.count().toString()
        }
    }
    override fun onItemSelected(parent: AdapterView<*> , view: View? , pos: Int , id: Long) {
        when (parent.selectedItemPosition) {
            0 ->{}
            1 ->{
                val lineBottomSheet : BottomSheetDialogFragment = AddLineBottomSheetDialog(this.lineItemData)
                lineBottomSheet.show((context as MainActivity).supportFragmentManager , lineBottomSheet.tag)
            }
            2 -> {
                deleteLineItem(this.lineItemData)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
    fun deleteLineItem(dbDownloadLine : DBDownloadLine){
        val db : DownloadManagerDatabase =
            DownloadManagerDatabase.getInstance(context)
        val downloadLineDao : DBDownloadLineDao = db.dbDownloadLineDao()
        val downloadsDao : DBDownloadDao = db.dbDownloadDao()
        val linesWithDownloadsDao : DBLinesWithDownloadsDao = db.dbLinesWithDownloadsDao()
        var downloads : DBLinesWithDownloads?
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    downloads = linesWithDownloadsDao.getLineWithDownloads(dbDownloadLine.Id)
                    if(downloads != null){
                        downloads?.dbDownloadList?.forEach {
                            it.LineId = 0
                        }
                    }
                    downloadsDao.UpdateAll(downloads!!.dbDownloadList)
                    downloadLineDao.Delete(dbDownloadLine)
                }
                refreshLineViewItem()
                Toast.makeText(
                    context ,
                    resources.getString(R.string.DeleteDownloadLineSuccessMessage) ,
                    Toast.LENGTH_LONG
                )
                    .show();
            }catch (e : Exception){
                refreshLineViewItem()
                Toast.makeText(
                    context ,
                    e.message.toString() ,
                    Toast.LENGTH_LONG
                )
                    .show();
            }
        }
    }
    private fun refreshLineViewItem(){
        val db : DownloadManagerDatabase =
            DownloadManagerDatabase.getInstance(context)
        val downloadLinesDao : DBDownloadLineDao = db.dbDownloadLineDao()
        var downloadLines : List<DBDownloadLine>?
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if(model!!.singedInUser?.value != null){
                    downloadLines = downloadLinesDao.getAll(model!!.singedInUser?.value!!.Id)
                }else{
                    downloadLines = downloadLinesDao.getAll(0)
                }
            }
            model!!.downloadLines?.postValue(downloadLines)
        }
    }
}