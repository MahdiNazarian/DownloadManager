package com.ghazalpaknia_mahdinazarian.downloadmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.drawerlayout.widget.DrawerLayout
import com.ghazalpaknia_mahdinazarian.dialogs.RegisterUserBottomSheetDialog
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
    fun onDrawerContentRegisterClick(view: View){
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        drawer.close()
        val registerBottomSheet : BottomSheetDialogFragment = RegisterUserBottomSheetDialog()
        registerBottomSheet.show(supportFragmentManager,registerBottomSheet.tag)
    }
    fun onDrawerContentLoginClick(view: View){}
    fun onDrawerContentBuySubscriptionClick(view: View){}
    fun onDrawerContentLogoutClick(view: View){}
}