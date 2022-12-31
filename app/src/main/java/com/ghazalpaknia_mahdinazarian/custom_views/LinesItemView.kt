package com.ghazalpaknia_mahdinazarian.custom_views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ghazalpaknia_mahdinazarian.downloadmanager.R

class LinesItemView(
    context: Context,
    attrs: AttributeSet? = null,
)  : FrameLayout(context,attrs) {
    private var lineItemFrame : FrameLayout
    private var lineItemMainLayout : ConstraintLayout
    private var lineItemMainCard : CardView
    private var lineItemLayout : ConstraintLayout
    private var lineItemName : TextView
    private var lineItemDescription : TextView
    private var lineItemActions : Spinner
    init {
        inflate(context , R.layout.lines_list_item , this)
        lineItemFrame = findViewById(R.id.LineItemFrame)
        lineItemMainLayout = findViewById(R.id.LineItemMainLayout)
        lineItemMainCard = findViewById(R.id.LineItemCard)
        lineItemLayout = findViewById(R.id.LineItemLayout)
        lineItemName = findViewById(R.id.LineItemName)
        lineItemDescription = findViewById(R.id.LineItemDescription)
        lineItemActions = findViewById(R.id.LineItemActions)
    }
}