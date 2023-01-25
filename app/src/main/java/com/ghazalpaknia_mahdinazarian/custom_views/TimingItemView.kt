package com.ghazalpaknia_mahdinazarian.custom_views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ghazalpaknia_mahdinazarian.app_models.DownloadsItemModel
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.ghazalpaknia_mahdinazarian.static_values.Helpers

class TimingItemView (
    context: Context,
    attrs: AttributeSet? = null,
)  : FrameLayout(context,attrs) {
    private lateinit var timingItemData : DBTimings
    private var timingItemFrame : FrameLayout
    private var timingItemMainLayout : ConstraintLayout
    private var timingItemCard : CardView
    private var timingItemLayout : ConstraintLayout
    private var timingItemName : TextView
    private var timingItemDateCreated : TextView
    private var timingItemActions : Spinner
    init {
        inflate(context , R.layout.lines_list_item , this)
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
    }
    fun setTimingItemsValue(){
        timingItemName.text = this.timingItemData.TimingName.toString()
        timingItemDateCreated.text = Helpers.getDate(this.timingItemData.DateCreated,"\"dd/MM/yyyy hh:mm:ss.SSS\"")
    }
}