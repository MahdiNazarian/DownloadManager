package com.ghazalpaknia_mahdinazarian.downloadmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.ghazalpaknia_mahdinazarian.database.DownloadManagerDatabase
import com.ghazalpaknia_mahdinazarian.database_daos.DBUserDao
import com.ghazalpaknia_mahdinazarian.database_models.DBUsers
import com.ghazalpaknia_mahdinazarian.dialogs.LoginUserBottomSheetDialog
import com.ghazalpaknia_mahdinazarian.dialogs.RegisterUserBottomSheetDialog
import com.ghazalpaknia_mahdinazarian.fragments.Downloads
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*
import androidx.lifecycle.Observer
import com.ghazalpaknia_mahdinazarian.ViewModels.MainActivityViewModel
import com.ghazalpaknia_mahdinazarian.database_daos.DBDownloadLineDao
import com.ghazalpaknia_mahdinazarian.database_daos.DBTimingsDao
import com.ghazalpaknia_mahdinazarian.database_models.DBDownloadLine
import com.ghazalpaknia_mahdinazarian.database_models.DBTimings
import com.ghazalpaknia_mahdinazarian.fragments.DownloadTimings
import com.ghazalpaknia_mahdinazarian.fragments.Lines
import com.ghazalpaknia_mahdinazarian.static_values.StaticValues


class MainActivity : AppCompatActivity() {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope : CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val model: MainActivityViewModel by viewModels()
    private var SignedInUser : DBUsers? = null
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        viewModelScope.launch {
            val db : DownloadManagerDatabase =
                DownloadManagerDatabase.getInstance(applicationContext)
            val userDao : DBUserDao = db.dbUserDao()
            withContext(Dispatchers.IO) {
                SignedInUser = userDao.loggedInUser;
            }
            model.singedInUser?.postValue(SignedInUser)
            refreshLineViewItem()
            refreshTimingViewItem()
        }
        val userObserver = Observer<DBUsers> { newUser ->
            if (newUser != null && newUser.loggedIn) {
                findViewById<TextView>(R.id.UserGreet1).text = resources.getString(
                    R.string.UserGreetWithName ,
                    newUser.Name + " " + newUser.LastName
                )
            }else{
                findViewById<TextView>(R.id.UserGreet1).text = resources.getString(R.string.UserGreetDefault)
            }
        }
        model.singedInUser?.observe(this, userObserver)
    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.NotificationChannelName)
            val descriptionText = getString(R.string.NotificationChannelDescription)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(StaticValues.NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun onOpenDrawerClick(view : View) {
        val drawer : DrawerLayout = findViewById(R.id.drawer_layout)
        drawer.open()
    }

    fun onDrawerContentRegisterClick(view : View) {
        val drawer : DrawerLayout = findViewById(R.id.drawer_layout)
        drawer.close()
        val registerBottomSheet : BottomSheetDialogFragment = RegisterUserBottomSheetDialog()
        registerBottomSheet.show(supportFragmentManager , registerBottomSheet.tag)
    }

    fun onDrawerContentLoginClick(view : View) {
        val drawer : DrawerLayout = findViewById(R.id.drawer_layout)
        drawer.close()
        val registerBottomSheet : BottomSheetDialogFragment = LoginUserBottomSheetDialog()
        registerBottomSheet.show(supportFragmentManager , registerBottomSheet.tag)
    }

    fun onDrawerContentBuySubscriptionClick(view : View) {}
    fun onDrawerContentLogoutClick(view : View) {
        val drawer : DrawerLayout = findViewById(R.id.drawer_layout)
        val db : DownloadManagerDatabase =
            DownloadManagerDatabase.getInstance(applicationContext)
        val userDao : DBUserDao = db.dbUserDao()
        var allUsers : List<DBUsers>?
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    allUsers =
                        userDao.all
                }
                if (allUsers != null && allUsers!!.isNotEmpty()) {
                    allUsers!!.forEach {
                        it.loggedIn = false
                        withContext(Dispatchers.IO) {
                            userDao.Update(it)
                        }
                    }
                }
                model.singedInUser?.postValue(null)
                drawer.close()
            } catch (e : Exception) {
                Toast.makeText(
                    applicationContext ,
                    e.message.toString() ,
                    Toast.LENGTH_LONG
                )
                    .show();
            }
        }
    }
    fun onDownloadPageNavigationClick(view : View) {
        val downloadFragment : Fragment = Downloads()
        supportFragmentManager.beginTransaction()
            .replace(R.id.FragmentsContainer,downloadFragment)
            .setReorderingAllowed(true)
            .addToBackStack(null) // name can be null
            .commit();
    }
    fun onTimingsPageNavigationClick(view : View){
        val timingFragment : Fragment = DownloadTimings()
        supportFragmentManager.beginTransaction()
            .replace(R.id.FragmentsContainer,timingFragment)
            .setReorderingAllowed(true)
            .addToBackStack(null) // name can be null
            .commit();
    }
    fun onLinesPageNavigationClick(view : View){
        val linesFragment : Fragment = Lines()
        supportFragmentManager.beginTransaction()
            .replace(R.id.FragmentsContainer,linesFragment)
            .setReorderingAllowed(true)
            .addToBackStack(null) // name can be null
            .commit();
    }
    private fun refreshLineViewItem(){
        val db : DownloadManagerDatabase =
            DownloadManagerDatabase.getInstance(this)
        val downloadLinesDao : DBDownloadLineDao = db.dbDownloadLineDao()
        var downloadLines : List<DBDownloadLine>?
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if(model.singedInUser?.value != null){
                    downloadLines = downloadLinesDao.getAll(model.singedInUser?.value!!.Id)
                }else{
                    downloadLines = downloadLinesDao.getAll(0)
                }
            }
            model.downloadLines?.postValue(downloadLines)
        }
    }
    private fun refreshTimingViewItem(){
        val db : DownloadManagerDatabase =
            DownloadManagerDatabase.getInstance(this)
        val timingDao : DBTimingsDao = db.dbTimingsDao()
        var timings : List<DBTimings>?
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if(model.singedInUser?.value != null){
                    timings = timingDao.getAll(model.singedInUser?.value!!.Id)
                }else{
                    timings = timingDao.getAll(0)
                }
            }
            model.timings?.postValue(timings)
        }
    }
}