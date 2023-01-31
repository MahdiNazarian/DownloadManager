package com.ghazalpaknia_mahdinazarian.services

import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.os.*
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.ghazalpaknia_mahdinazarian.ViewModels.MainActivityViewModel
import com.ghazalpaknia_mahdinazarian.database.DownloadManagerDatabase
import com.ghazalpaknia_mahdinazarian.database_daos.DBDownloadDao
import com.ghazalpaknia_mahdinazarian.database_models.DBDownload
import com.ghazalpaknia_mahdinazarian.downloadmanager.R
import com.ghazalpaknia_mahdinazarian.static_values.StaticValues
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit


class DownloadService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    var dbDownloadObject : DBDownload? = null
    private val viewModelJob = SupervisorJob()
    private val viewModelScope : CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var model : MainActivityViewModel? = null
    private var notificationId : Int = 0
    var downloadNotification : NotificationCompat.Builder? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }


            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent , flags: Int , startId: Int): Int {
        //model = ViewModelProvider(requireActivity() as MainActivity)[MainActivityViewModel::class.java]
        serviceHandler?.post {
            kotlin.run {
                val db : DownloadManagerDatabase =
                    DownloadManagerDatabase.getInstance(this)
                val dbDownloadDao : DBDownloadDao = db.dbDownloadDao()
                val downloadId = intent.getLongExtra(StaticValues.DownloadServiceGetIdExtraKeyName,0).toInt()
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        dbDownloadObject = dbDownloadDao.getById(downloadId)
                    }

                    downloadNotification = NotificationCompat.Builder(applicationContext, StaticValues.NOTIFICATION_CHANNEL_ID).apply {
                        setContentTitle(dbDownloadObject?.FileName)
                        setContentText(StaticValues.DownloadStates.DOWNLOADING.toString())
                        setSmallIcon(R.drawable.download_icon)
                        priority = NotificationCompat.PRIORITY_LOW
                    }
                    val progressMax = 100
                    var progressCurrent = 0
                    // For each start request, send a message to start a job and deliver the
                    // start ID so we know which request we're stopping when we finish the job
                    serviceHandler?.obtainMessage()?.also { msg ->
                        msg.arg1 = startId
                        serviceHandler?.sendMessage(msg)
                    }
                    if (dbDownloadObject != null){
                        withContext(Dispatchers.IO){
                        val okHttpBuilder = OkHttpClient.Builder()
                            .connectTimeout(20 , TimeUnit.SECONDS)
                            .readTimeout(20 , TimeUnit.SECONDS)
                        val okHttpClient = okHttpBuilder.build()
                        val request = Request.Builder().url(dbDownloadObject!!.DownloadUrl).build()
                        val response = okHttpClient.newCall(request).execute()
                        val body = response.body
                        val responseCode = response.code
                        if (responseCode >= HttpURLConnection.HTTP_OK &&
                            responseCode < HttpURLConnection.HTTP_MULT_CHOICE &&
                            body != null
                        ) {
                            NotificationManagerCompat.from(applicationContext).apply {
                                downloadNotification!!.setProgress(progressMax, progressCurrent, false)
                                notify(notificationId, downloadNotification!!.build())
                            }
                            val length = body.contentLength()
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                                val contentValues = ContentValues().apply {
                                    put(MediaStore.MediaColumns.DISPLAY_NAME, dbDownloadObject?.FileName?.split(".")?.first())
                                    put(MediaStore.MediaColumns.MIME_TYPE,  dbDownloadObject?.FileName?.split(".")?.last())
                                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                                }
                                val resolver = contentResolver
                                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                                body.byteStream().apply {
                                    resolver.openOutputStream(uri!!).use { fileOut ->
                                        var bytesCopied = 0
                                        val buffer = ByteArray(1024)
                                        var bytes = read(buffer)
                                        while (bytes >= 0) {
                                            fileOut!!.write(buffer , 0 , bytes)
                                            bytesCopied += bytes
                                            bytes = read(buffer)
                                            progressCurrent = ((bytesCopied * 100) / length).toInt()
                                            NotificationManagerCompat.from(applicationContext).apply {
                                                downloadNotification!!.setProgress(
                                                    progressMax ,
                                                    progressCurrent ,
                                                    false
                                                )
                                                notify(notificationId , downloadNotification!!.build())
                                            }
                                        }
                                    }
                                }
                            }else{
                                val rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                                val root = File(rootPath)
                                if (!root.exists()) {
                                    root.mkdirs()
                                }
                                val file : File = File(rootPath,dbDownloadObject?.FileName!!)
                                if(!file.exists())
                                    file.createNewFile()
                                body.byteStream().apply {
                                    FileOutputStream(file).use { fileOut ->
                                        var bytesCopied = 0
                                        val buffer = ByteArray(1024)
                                        var bytes = read(buffer)
                                        while (bytes >= 0) {
                                            fileOut.write(buffer , 0 , bytes)
                                            bytesCopied += bytes
                                            bytes = read(buffer)
                                            progressCurrent = ((bytesCopied * 100) / length).toInt()
                                            NotificationManagerCompat.from(applicationContext).apply {
                                                downloadNotification!!.setProgress(
                                                    progressMax ,
                                                    progressCurrent ,
                                                    false
                                                )
                                                notify(notificationId , downloadNotification!!.build())
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // Report the error
                        }

                    }
                    }
                }
            }
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
    }
}