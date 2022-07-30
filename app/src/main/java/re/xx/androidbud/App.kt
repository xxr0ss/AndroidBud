package re.xx.androidbud

import android.app.Application
import android.content.IntentFilter

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setupClipboardReceiver()
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
}