package io.androidedu.servicesbroadcastreceiver.receiver

import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.TextView
import android.widget.Toast
import io.androidedu.servicesbroadcastreceiver.enums.DownloadServiceEnum


/******************************
 * Created by Gökhan ÖZTÜRK   |
 * 4.03.2018                 |
 * GokhanOzturk@AndroidEdu.IO |
 *****************************/

class DownloadReceiver : BroadcastReceiver() {

    private var txtDurumu: TextView? = null

    override fun onReceive(context: Context, intent: Intent) {

        val bundle = intent.extras
        if (bundle != null) {
            val string = bundle.getString(DownloadServiceEnum.FILEPATH.toString())
            val resultCode = bundle.getInt(DownloadServiceEnum.RESULT.toString())
            if (resultCode == RESULT_OK) {
                Toast.makeText(context, "Download complete. Download URI: " + string!!,
                        Toast.LENGTH_LONG).show()
                txtDurumu!!.text = "Download done"
            } else {
                Toast.makeText(context, "Download failed", Toast.LENGTH_LONG).show()
                txtDurumu!!.text = "Download failed"
            }
        }
    }

    fun setTextView(txtDurumu: TextView) {

        this.txtDurumu = txtDurumu
    }
}