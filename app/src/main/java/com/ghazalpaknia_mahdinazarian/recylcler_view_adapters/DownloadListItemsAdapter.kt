package com.ghazalpaknia_mahdinazarian.recylcler_view_adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ghazalpaknia_mahdinazarian.app_models.DownloadsItemModel
import com.ghazalpaknia_mahdinazarian.custom_views.DownloadItemView
import com.ghazalpaknia_mahdinazarian.database_models.DBDownload


class DownloadListItemsAdapter(private var downloadItems : List<DBDownload>) : RecyclerView.Adapter<DownloadListItemsAdapter.ViewHolder>() {
    class ViewHolder(var downloadItemView : DownloadItemView) : RecyclerView.ViewHolder(downloadItemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item : DownloadItemView = DownloadItemView(parent.context)
        item.setLayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.downloadItemView.setDownloadItem(this.downloadItems[position])
        holder.downloadItemView.setDownloadItemValues()
    }

    override fun getItemCount(): Int {
        return this.downloadItems.size
    }
}