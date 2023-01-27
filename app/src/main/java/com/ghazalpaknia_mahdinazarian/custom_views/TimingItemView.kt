package com.ghazalpaknia_mahdinazarian.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ghazalpaknia_mahdinazarian.ViewModels.TimingsViewModel
import com.ghazalpaknia_mahdinazarian.database.DownloadManagerDatabase
import com.ghazalpaknia_mahdinazarian.database_daos.DBTimingDatesDao
import com.ghazalpaknia_mahdinazarian.database_daos.DBTimingsDao
import com.ghazalpaknia_mahdinazarian.database_models.DBTimingDates
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.ghazalpaknia_mahdinazarian.static_values.Helpers
import kotlinx.coroutines.*

class TimingItemView (
    context: Context,
    attrs: AttributeSet? = null,
)  : FrameLayout(context,attrs) , AdapterView.OnItemSelectedListener {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope : CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private lateinit var timingItemData : DBTimings
    private var timingItemFrame : FrameLayout
    private var timingItemMainLayout : ConstraintLayout
    private var timingItemCard : CardView
    private var timingItemLayout : ConstraintLayout
    private var timingItemName : TextView
    private var timingItemDateCreated : TextView
    private var timingItemActions : Spinner
    private val model: TimingsViewModel = TimingsViewModel()
    init {
        inflate(context , R.layout.timing_list_item , this)
        timingItemFrame = findViewById(R.id.TimingItemFrame)
        timingItemMainLayout = findViewById(R.id.TimingItemMainLayout)
        timingItemCard = findViewById(R.id.TimingItemCard)
        timingItemLayout = findViewById(R.id.TimingItemLayout)
        timingItemName = findViewById(R.id.TimingItemName)
        timingItemDateCreated = findViewById(R.id.TimingItemDateCreated)
        timingItemActions = findViewById(R.id.TimingItemActions)
    }
    fun setTimingItem(timingItemData : DBTimings){
        this.timingItemData = timingItemData
        ArrayAdapter.createFromResource(
            context ,
            R.array.CustomViewsSpinnerItemsArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            timingItemActions.adapter = adapter
        }
    }
    fun setTimingItemsValue(){
        timingItemName.text = this.timingItemData.TimingName.toString()
        timingItemDateCreated.text = resources.getString(R.string.TimingItemDateCreated ,
            Helpers.getDate(timingItemData.DateCreated,"YYYY/MM/dd"),
            Helpers.getDate(timingItemData.DateCreated,"HH:mm"),
            )
    }
    override fun onItemSelected(parent: AdapterView<*> , view: View? , pos: Int , id: Long) {
        when (parent.selectedItemPosition) {
            0 ->{}
            1 -> {

            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
    fun deleteTimingItem(dbTiming : DBTimings){
        val db : DownloadManagerDatabase =
            DownloadManagerDatabase.getInstance(context)
        val timingDao : DBTimingsDao = db.dbTimingsDao()
        val timingDatesDao : DBTimingDatesDao = db.dbTimingDatesDao()
        var deleteTimingDate : DBTimingDates = DBTimingDates()
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    deleteTimingDate = timingDatesDao.getTimingDateByTimingId(dbTiming.Id)
                    timingDatesDao.Delete(deleteTimingDate)
                    timingDao.Delete(dbTiming)
                }
                Toast.makeText(
                    context ,
                    resources.getString(R.string.DeleteTimingSuccessMessage) ,
                    Toast.LENGTH_LONG
                )
                    .show();
            }catch (e : Exception){
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