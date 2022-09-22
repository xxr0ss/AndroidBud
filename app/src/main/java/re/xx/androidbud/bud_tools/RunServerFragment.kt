package re.xx.androidbud.bud_tools

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import re.xx.androidbud.R
import re.xx.androidbud.databinding.FragmentRunServerBinding
import re.xx.androidbud.utils.Shell
import java.io.File

// To run frida_server or IDA server
class RunServerFragment : Fragment() {
    private val TAG = "RunServerFragment"
    private var _binding: FragmentRunServerBinding? = null
    private val binding get() = _binding!!
    private lateinit var sPrefs: SharedPreferences
    private var fridaServerName: String? = null
    private var fridaServerDir: String? = null
    private val defaultFridaListeningAddress = "0.0.0.0:27042"
    private val defaultLldbListeningPort = "6666"
    private var lldbServerName: String? = null
    private var lldbServerDir: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: $savedInstanceState")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        _binding = FragmentRunServerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonStartFridaServer.setOnClickListener {
            val address = binding.tvFridaListeningAddress.text.toString().trim()
            var extraArguments = ""
            if (address != defaultFridaListeningAddress) {
                extraArguments += "-l $address"
            }
            startServer(fridaServerDir, fridaServerName, extraArguments)
        }
        binding.buttonKillFridaServer.setOnClickListener { killServer(fridaServerName) }
        binding.buttonRunLldbServer.setOnClickListener {
            val useTcp = binding.switchLldb.isChecked
            val extraArguments = if (useTcp)
                "platform --server --listen \"*:${binding.tvLldbListeningPort.text}\""
                else "platform --server --listen unix-abstract:///data/local/tmp/debug.sock"
            startServer(lldbServerDir, lldbServerName, extraArguments)
        }
        binding.buttonKillLldbServer.setOnClickListener { killServer(lldbServerName) }
    }

    override fun onResume() {
        super.onResume()
        sPrefs =
            PreferenceManager.getDefaultSharedPreferences(activity!!.applicationContext)
        fridaServerDir = sPrefs.getString("frida_server_dir", "/data/local/tmp")
        fridaServerName = sPrefs.getString("frida_server_name", "frida-server-15.2.2-android-arm64")
        lldbServerDir = sPrefs.getString("lldb_server_dir", "/data/local/tmp")
        lldbServerName = sPrefs.getString("lldb_server_name", "lldb-server")

        binding.tvFridaListeningAddress.setText(
            sPrefs.getString("frida_server_listening_address", defaultFridaListeningAddress)
        )
        binding.tvLldbListeningPort.setText(
            sPrefs.getString("lldb_server_listening_port", defaultLldbListeningPort)
        )
        Log.d(TAG, "onResume")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        Log.d(TAG, "onDestroyView")
    }

    private fun startServer(serverDir: String?, serverName: String?, extraArguments: String = "") {
        if (serverDir == null || serverName == null)
            return
        val serverFilePath = "$serverDir/$serverName"
        if (!File(serverFilePath).exists()) {
            view?.let {
                Snackbar
                    .make(
                        it,
                        "Server executable not found at $serverFilePath",
                        Snackbar.LENGTH_SHORT
                    ).show()
            }
            return
        }

        // Kill existing running server
        killServer(serverName, true)

        Shell.execRootCmd("$serverFilePath $extraArguments", false)

        val output = Shell.execRootCmd("ps -ef | grep $serverName | grep -v grep")
        if (output.contains(serverName)) view?.let {
            Snackbar.make(it, "$serverName started", Snackbar.LENGTH_SHORT).show()
        } else view?.let {
            Snackbar.make(it, R.string.server_failed, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun killServer(serverName: String?, quiet: Boolean = false) {
        if (serverName == null)
            return
        Shell.execRootCmd("killall $serverName")
        Log.i(TAG, "kill server: $serverName")
        if (!quiet) {
            view?.let {
                Snackbar.make(it, R.string.server_killed, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}