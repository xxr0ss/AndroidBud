package re.xx.androidbud

import android.app.Activity
import android.content.*
import android.widget.Toast

class ClipboardReceiver : BroadcastReceiver() {
    companion object {
        val ACTION_GET = "clip.get"
        val ACTION_GET_SHORT = "get"
        val ACTION_SET = "clip.set"
        val ACTION_SET_SHORT = "set"
        val TAG = "ClipboardReceiver"
        val EXTRA_TEXT = "text"
    }

    fun isActionGet(action: String?) : Boolean {
        return (action == ACTION_GET) or (action == ACTION_GET_SHORT)
    }

    fun isActionSet(action: String?) : Boolean {
        return (action == ACTION_SET) or (action == ACTION_SET_SHORT)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val clipboardMgr = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (isActionSet(intent.action)) {
            val text = intent.getStringExtra(EXTRA_TEXT)
            if (text != "") {
                val clipData = ClipData.newPlainText(TAG, text)
                clipboardMgr.setPrimaryClip(clipData)
                resultCode = Activity.RESULT_OK
                resultData = "Text copied to clipboard"
                Toast.makeText(context, resultData, Toast.LENGTH_SHORT).show()
            } else {
                resultCode = Activity.RESULT_CANCELED
                resultData = "No text is provided. Use -e text \"text to be pasted\""
            }
        }else if (isActionGet(intent.action)) {
            if (clipboardMgr.primaryClip != null) {
                val item = clipboardMgr.primaryClip!!.getItemAt(0)
                resultData = String.format("Clipboard text: %s", item.text)
                resultCode = Activity.RESULT_OK
            } else {
                resultCode = Activity.RESULT_CANCELED
                resultData = ""
            }
        }
    }
}