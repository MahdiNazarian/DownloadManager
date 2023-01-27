package com.ghazalpaknia_mahdinazarian.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

class DatePickerDialogBox(private var caller : MaterialButton) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)

    }

    override fun onDateSet(view: DatePicker , year: Int , month: Int , day: Int) {
        val selectedMonth : Int = month + 1

        caller.text = resources.getString(
            R.string.DateTemplate,
            year.toString(),
            selectedMonth.toString(),
            day.toString())
    }
}