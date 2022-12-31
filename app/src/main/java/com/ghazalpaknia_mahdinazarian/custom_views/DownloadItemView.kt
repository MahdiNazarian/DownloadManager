package com.ghazalpaknia_mahdinazarian.custom_views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ghazalpaknia_mahdinazarian.app_models.DownloadsItemModel
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.ghazalpaknia_mahdinazarian.static_values.Helpers

class DownloadItemView(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context,attrs) {
    private lateinit var downloadItem : DownloadsItemModel
    private var downloadItemFrameLayout : FrameLayout
    private var downloadItemMainLayout : ConstraintLayout
    private var downloadItemCard : CardView
    private var downloadItemLayout : ConstraintLayout
    private var downloadItemIcon : ImageView
    private var downloadItemName :TextView
    private var downloadItemStatus : TextView
    private var downloadItemPercentage : TextView
    private var downloadItemProgressBar : ProgressBar
    private var downloadItemFirstDownloadAction : ImageButton
    private var downloadItemSecondDownloadAction :ImageButton
    private var downloadItemsDownloadSpeed : TextView
    private var downloadItemFileSize : TextView
    private var downloadItemDownloadedSize : TextView
    init {
        inflate(context, R.layout.downloads_list_item,this)
        downloadItemFrameLayout = findViewById(R.id.DownloadItemFrame)
        downloadItemMainLayout = findViewById(R.id.DownloadItemMainLayout)
        downloadItemCard  = findViewById(R.id.DownloadItemCard)
        downloadItemLayout = findViewById(R.id.DownloadItemLayout)
        downloadItemIcon = findViewById(R.id.DownloadItemIcon)
        downloadItemName  = findViewById(R.id.DownloadItemName)
        downloadItemStatus = findViewById(R.id.DownloadItemStatus)
        downloadItemPercentage = findViewById(R.id.DownloadItemPercentage)
        downloadItemProgressBar = findViewById(R.id.DownloadProgressBar)
        downloadItemFirstDownloadAction = findViewById(R.id.FirstDownloadAction)
        downloadItemSecondDownloadAction = findViewById(R.id.SecondDownlodAction)
        downloadItemsDownloadSpeed = findViewById(R.id.DownloadSpeed)
        downloadItemFileSize  = findViewById(R.id.DownloadSize)
        downloadItemDownloadedSize  = findViewById(R.id.DownloadedFileSize)
    }
    fun setDownloadItem(downloadItem : DownloadsItemModel){
        this.downloadItem = downloadItem
    }
    fun setDownloadItemValues(){
        this.downloadItemName.text = this.downloadItem.itemName
        this.downloadItemStatus.text = this.downloadItem.downloadState.toString()
        this.downloadItemPercentage.text = resources.getString(R.string.DownloadItemPercentageText,this.downloadItem.downloadProgress.toString())
        this.downloadItemProgressBar.progress = this.downloadItem.downloadProgress
        this.downloadItemsDownloadSpeed.text = Helpers.byteConverter(this.downloadItem.downloadSpeed,true)
        this.downloadItemFileSize.text = Helpers.byteConverter(this.downloadItem.downloadFileSize,false)
        this.downloadItemDownloadedSize.text = Helpers.byteConverter(this.downloadItem.downloadedFileSize,false)
    }
}