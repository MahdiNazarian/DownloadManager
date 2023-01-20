package com.ghazalpaknia_mahdinazarian.Dialoges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RegisterUserBottomSheetDialog : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.register_bottom_sheet, container, false)

    companion object {
        const val TAG = "RegisterUserBottomSheet"
    }
}