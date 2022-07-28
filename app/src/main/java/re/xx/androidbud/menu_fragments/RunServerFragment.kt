package re.xx.androidbud.menu_fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import re.xx.androidbud.R
import re.xx.androidbud.databinding.FragmentRunServerBinding
import re.xx.androidbud.utils.SuperUser
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
        binding.buttonStartFridaServer.setOnClickListener { startFridaServer() }
        binding.buttonKillFridaServer.setOnClickListener { killFridaServer() }
    }

    override fun onResume() {
        super.onResume()
        sPrefs =
            PreferenceManager.getDefaultSharedPreferences(activity!!.applicationContext)
        fridaServerDir = sPrefs.getString("Frida server directory", "/data/local/tmp")
        fridaServerName = sPrefs.getString("frida_server_name", "frida-server-15.2.2-android-arm64")

        binding.tvFridaListeningAddress.setText(
            sPrefs.getString("frida_server_listening_address", defaultFridaListeningAddress)
        )
        Log.d(TAG, "onResume")
    }

    private fun startFridaServer() {
        val serverFilePath = "$fridaServerDir/$fridaServerName"
        if (!File(serverFilePath).exists()) {
            view?.let {
                Snackbar
                    .make(it, R.string.plz_check_frida_server_exists, Snackbar.LENGTH_SHORT).show()
            }
            return
        }
        val address = binding.tvFridaListeningAddress.text.toString().trim()
        var extraArguments = ""
        if (address != defaultFridaListeningAddress) {
            extraArguments += "-l $address"
        }
        killFridaServer(true)
        SuperUser.execRootCmd("$serverFilePath $extraArguments", false)
        val output = SuperUser.execRootCmd("ps -ef | grep frida-server")
        if (fridaServerName?.let { output.contains(it) } == true) view?.let {
            Snackbar
                .make(it, R.string.server_started, Snackbar.LENGTH_SHORT).show()
        }
        else view?.let{
            Snackbar
                .make(it, R.string.server_failed, Snackbar.LENGTH_SHORT).show()
        }

    }

    private fun killFridaServer(quiet: Boolean=false) {
        fridaServerName?.let {
            SuperUser.execRootCmd("killall $it")
            Log.i(TAG, "kill frida server")
        }
        if (!quiet) {
            view?.let {
                Snackbar.make(it, R.string.server_killed, Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        Log.d(TAG, "onDestroyView")
    }
}