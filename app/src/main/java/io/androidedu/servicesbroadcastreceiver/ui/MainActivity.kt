package io.androidedu.servicesbroadcastreceiver.ui

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import io.androidedu.servicesbroadcastreceiver.R
import io.androidedu.servicesbroadcastreceiver.enums.DownloadServiceEnum
import io.androidedu.servicesbroadcastreceiver.receiver.DownloadReceiver
import io.androidedu.servicesbroadcastreceiver.receiver.InternetConnectionReceiver
import io.androidedu.servicesbroadcastreceiver.services.ChatWithMe
import io.androidedu.servicesbroadcastreceiver.services.DownloadDataService

class MainActivity : AppCompatActivity() {

    private val downloadReceiver by lazy { DownloadReceiver() }
    private val internetConnectionReceiver by lazy { InternetConnectionReceiver() }
    private val txtServiceStatus by lazy { findViewById<TextView>(R.id.activity_main_txtServiceStatus) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        downloadReceiver.setTextView(txtServiceStatus)
    }

    fun btnStartAChat(view: View) {

        //promt izni icin
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName))
            startActivityForResult(intent, 140)
        } else {
            startService(Intent(this, ChatWithMe::class.java))
        }
    }

    fun btnDownloadWholePage(view: View) {

        val intent = Intent(this, DownloadDataService::class.java)

        intent.putExtra(DownloadServiceEnum.FILENAME.toString(), "index.html")
        intent.putExtra(DownloadServiceEnum.URL.toString(), "https://github.com/AndroidEduIO/Android-Kotlin/blob/master/README.md")

        startService(intent)

        txtServiceStatus.text = "Donwload Service Started"
    }

    override fun onResume() {

        super.onResume()

        registerReceiver(downloadReceiver, IntentFilter(DownloadServiceEnum.NOTIFICATION.toString()))

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(internetConnectionReceiver, filter)
    }

    override fun onPause() {

        super.onPause()

        unregisterReceiver(downloadReceiver)
        unregisterReceiver(internetConnectionReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(downloadReceiver)
        unregisterReceiver(internetConnectionReceiver)
    }
}
