package io.androidedu.servicesbroadcastreceiver.services

import android.app.Activity
import android.app.IntentService
import android.content.Intent
import android.os.Environment
import io.androidedu.servicesbroadcastreceiver.enums.DownloadServiceEnum
import java.io.*
import java.net.URL


/******************************
 * Created by Gökhan ÖZTÜRK   |
 * 4.03.2018                 |
 * GokhanOzturk@AndroidEdu.IO |
 *****************************/

//servisler temelde 3 e ayrılıyor.

//onyüz servisleri (foreground service) radyo uygulamaları, spotify, messenger
//arka plan servisleri (background service) - bizim yapacağımız.
//bağıl servisler (bound service)

//servisleri temelde 2 farklı noktadan extends edebiliyoruz.

//Service ve IntentService
//Service'ler ui olmayan islerde kullanılmalı ve gorev süreleri uzun olmamalidir.
//Eger uzun surecekse Thread'lerle beraber kullanmamız gerekir.

//IntentService Main Thread (Ana Akış) ile iletişim kurmadan yapılan uzun islerde daha çok kullanılır.
//illa iletişim kurulacaksa boradcast receiever kullanılabilir.

//Service, startService() ile başlatılır.
//IntentService ise intent ile baslatilir. Ayrı bir thread üzerinde koşmaya baslar.

//Service'ler arka planda ancak yine de Main Thread (Ana Akış) üzerinde çalışır
//IntentService'ler ayrı bir worker thread (işçi akış?)

//Limitations / Drawbacks
//Service'ler ana akış (Main Thread) dediğimiz yapılar üzerinde çalıştığı için ana akışı blocklayabilir.
//IntentService'ler ayrı bir akış üzerinde çalıştıkları için işleri hızlandırabilirler. Ama maliyetlidir.


//intentService bizden bir constructor ve isim bekliyor.
class DownloadDataService : IntentService("DownloadDataService") {

    private var result = Activity.RESULT_CANCELED
    private var output: File? = null
    private var stream: InputStream? = null
    private var fos: FileOutputStream? = null

    //async şekilde dosya acıp yazdıracak.
    override fun onHandleIntent(intent: Intent?) {

        try {

            val url = URL(openFile(intent))

            stream = url.openConnection().getInputStream()

            val reader = InputStreamReader(stream!!)
            fos = FileOutputStream(output!!.path)

            var next = reader.read()

            while (next != -1) {
                fos!!.write(next)

                next = reader.read()
            }

            //tüm yazma islemi bittiginde result'a basarili kodu don.
            result = Activity.RESULT_OK

        } catch (e: Exception) {

            e.printStackTrace()

        } finally {

            //InputStream ve FileOutputStream'i kapat.
            if (stream != null) {
                try {
                    stream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            if (fos != null) {
                try {
                    fos!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

        publishResults(output!!.absolutePath, result)
    }

    private fun openFile(intent: Intent?): String {

        val urlPath = intent!!.getStringExtra(DownloadServiceEnum.URL.toString())
        val fileName = intent.getStringExtra(DownloadServiceEnum.FILENAME.toString())

        output = File(Environment.getExternalStorageDirectory(), fileName)

        if (output!!.exists()) {
            output!!.delete()
        }

        return urlPath
    }

    private fun publishResults(outputPath: String, result: Int) {

        //broadcast receiever baslat.
        val intent = Intent(DownloadServiceEnum.NOTIFICATION.toString())
        intent.putExtra(DownloadServiceEnum.FILEPATH.toString(), outputPath)
        intent.putExtra(DownloadServiceEnum.RESULT.toString(), result)
        sendBroadcast(intent)
    }
}