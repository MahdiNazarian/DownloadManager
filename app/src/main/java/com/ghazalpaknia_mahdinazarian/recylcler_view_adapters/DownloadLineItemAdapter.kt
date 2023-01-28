package com.ghazalpaknia_mahdinazarian.recylcler_view_adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ghazalpaknia_mahdinazarian.custom_views.LinesItemView
import com.ghazalpaknia_mahdinazarian.database_models.DBDownloadLine

class DownloadLineItemAdapter (private var lineItems : List<DBDownloadLine>) : RecyclerView.Adapter<DownloadLineItemAdapter.ViewHolder>() {
    class ViewHolder(var linesItemView : LinesItemView) : RecyclerView.ViewHolder(linesItemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): ViewHolder {
        val item : LinesItemView = LinesItemView(parent.context)
        item.setLayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.linesItemView.setLineItem(this.lineItems[position])
        holder.linesItemView.setLineItemsValue()
    }

    override fun getItemCount(): Int {
        return this.lineItems.size
    }
}