package com.ghazalpaknia_mahdinazarian.downloadmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ghazalpaknia_mahdinazarian.Dialoges.RegisterUserBottomSheetDialog
import com.ghazalpaknia_mahdinazarian.app_models.DownloadsItemModel
import com.ghazalpaknia_mahdinazarian.recylcler_view_adapters.DownloadListItemsAdapter
import com.ghazalpaknia_mahdinazarian.static_values.StaticValues
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun onListsClick(){

    }

    fun onOpenDrawerClick(view: View) {
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        drawer.open()
    }
    fun onRegisterClickMain(){
        val registerBottomSheet : BottomSheetDialogFragment = RegisterUserBottomSheetDialog()
        registerBottomSheet.show(supportFragmentManager,registerBottomSheet.tag)
    }
}