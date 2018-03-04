package io.androidedu.servicesbroadcastreceiver.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import io.androidedu.servicesbroadcastreceiver.R


/******************************
 * Created by Gökhan ÖZTÜRK   |
 * 4.03.2018                 |
 * GokhanOzturk@AndroidEdu.IO |
 *****************************/

class ChatWithMe : Service(), View.OnTouchListener {

    private val windowManager: WindowManager by lazy { getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    private val chatImage: ImageView by lazy { ImageView(this) }

    private val width by lazy { WindowManager.LayoutParams.WRAP_CONTENT }
    private val height by lazy { WindowManager.LayoutParams.WRAP_CONTENT }
    private val type by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
    }
    private val flag by lazy { WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE }
    private val format by lazy { PixelFormat.TRANSLUCENT }

    private val params by lazy { WindowManager.LayoutParams(width, height, type, flag, format) }

    private var initialX: Int = 0
    private var initialY: Int = 0
    private var initialTouchX: Float = 0.toFloat()
    private var initialTouchY: Float = 0.toFloat()

    override fun onBind(intent: Intent): IBinder? {

        Toast.makeText(this, "onBind works", Toast.LENGTH_SHORT).show()

        return null
    }

    override fun onUnbind(intent: Intent): Boolean {

        Toast.makeText(this, "onUnbind works", Toast.LENGTH_SHORT).show()

        return super.onUnbind(intent)
    }

    override fun onLowMemory() {
        super.onLowMemory()

        Toast.makeText(this, "onLowMemory works", Toast.LENGTH_SHORT).show()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Toast.makeText(this, "ChatWithMeService started", Toast.LENGTH_SHORT).show()

        return Service.START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        chatImage.setImageResource(R.mipmap.ic_launcher_round)
        chatImage.setOnTouchListener(this)

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 0
        params.y = 100

        windowManager.addView(chatImage, params)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = params.x
                initialY = params.y
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                return true
            }
            MotionEvent.ACTION_UP -> return true
            MotionEvent.ACTION_MOVE -> {
                params.x = initialX + (event.rawX - initialTouchX).toInt()
                params.y = initialY + (event.rawY - initialTouchY).toInt()
                windowManager.updateViewLayout(chatImage, params)
                return true
            }
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()

        windowManager.removeView(chatImage)

        //servisin içindeysek..
        //stopSelf();
        stopService(Intent(this, ChatWithMe::class.java))
    }
}