package re.xx.androidbud.menu_fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import re.xx.androidbud.databinding.FragmentRunServerBinding
import re.xx.androidbud.utils.SuperUser

// To run frida_server or IDA server
class RunServerFragment : Fragment() {
    private val TAG = "RunServerFragment"
    private var _binding: FragmentRunServerBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    var fridaServerName: String? = null
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
        binding.buttonStartFridaServer.setOnClickListener { startFrdiaServer() }
        binding.buttonKillFridaServer.setOnClickListener { killFridaServer() }
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(activity!!.applicationContext)
        fridaServerName = sharedPreferences.getString("frida_server_name", null)

        binding.tvFridaListeningAddress.setText(
            sharedPreferences.getString(
                "frida_server_listening_address",
                defaultFridaListeningAddress
            )
        )
        Log.d(TAG, "onResume")
    }

    fun startFrdiaServer() {
        fridaServerName?.let {
            val address = binding.tvFridaListeningAddress.text.toString().trim()
            var extraArguments = ""
            if (address != defaultFridaListeningAddress) {
                extraArguments += "-l $address"
            }
            SuperUser.execRootCmd("killall $it")
            SuperUser.execRootCmd("/data/local/tmp/$it $extraArguments", false)
            Log.i(TAG, "start frida server")
        }
    }

    fun killFridaServer() {
        fridaServerName?.let {
            SuperUser.execRootCmd("killall $it")
            Log.i(TAG, "kill frida server")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        Log.d(TAG, "onDestroyView")
    }
}