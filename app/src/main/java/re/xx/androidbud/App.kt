package re.xx.androidbud

import android.app.Application
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.preference.PreferenceManager


class App : Application() {
    private val keyFirstStart = "isFirstStart"

    override fun onCreate() {
        super.onCreate()

        initializeApp()
        setupClipboardReceiver()
    }

    private fun initializeApp() {
        val sPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val isFirstStart = sPrefs.getBoolean(keyFirstStart, true)

        val fridaVersion = "16.0.10"
        if (isFirstStart) {
            val fridaServerName: String = when (Build.SUPPORTED_ABIS[0]) {
                "arm64-v8a" -> {
                    "frida-server-$fridaVersion-android-arm64"
                }
                "armeabi-v7a" -> {
                    "frida-server-$fridaVersion-android-arm"
                }
                "armeabi" -> {
                    "frida-server-$fridaVersion-android-arm"
                }
                "x86_64" -> {
                    "frida-server-$fridaVersion-android-x86_64"
                }
                "x86" -> {
                    "frida-server-$fridaVersion-android-x86"
                }
                else -> {
                    ""
                }
            }
            val editor = sPrefs.edit()
            Log.d(TAG, "initializeApp: frida server:  $fridaServerName")
            editor.putBoolean(keyFirstStart, false)
                .putString("frida_server_name", fridaServerName)
                .apply()
        }
    }

    private fun setupClipboardReceiver() {
        val cbReceiver = ClipboardReceiver()
        val filter = IntentFilter().apply {
            addAction(ClipboardReceiver.ACTION_SET)
            addAction(ClipboardReceiver.ACTION_SET_SHORT)
            addAction(ClipboardReceiver.ACTION_GET)
            addAction(ClipboardReceiver.ACTION_GET_SHORT)
        }
        registerReceiver(cbReceiver, filter)
    }

    companion object {
        const val TAG = "App"
    }
}