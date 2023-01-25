package com.ghazalpaknia_mahdinazarian.recylcler_view_adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ghazalpaknia_mahdinazarian.app_models.DownloadsItemModel
import com.ghazalpaknia_mahdinazarian.custom_views.DownloadItemView
import com.ghazalpaknia_mahdinazarian.custom_views.TimingItemView
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings

class TimingListItemsAdapter (private var timingItems : List<DBTimings>): RecyclerView.Adapter<TimingListItemsAdapter.ViewHolder>() {
    class ViewHolder(var timingItemView : TimingItemView) : RecyclerView.ViewHolder(timingItemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): ViewHolder {
        val item : TimingItemView = TimingItemView(parent.context)
        item.setLayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.timingItemView.setTimingItem(this.timingItems[position])
        holder.timingItemView.setTimingItemsValue()
    }

    override fun getItemCount(): Int {
        return this.timingItems.size
    }
}