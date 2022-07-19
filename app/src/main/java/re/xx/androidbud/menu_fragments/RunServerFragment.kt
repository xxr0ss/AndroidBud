package re.xx.androidbud.menu_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import re.xx.androidbud.databinding.FragmentRunServerBinding

// To run frida_server or IDA server
class RunServerFragment : Fragment() {
    private val TAG = "RunServerFragment"
    private var _binding: FragmentRunServerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        _binding = FragmentRunServerBinding.inflate(layoutInflater, container, false)
        binding.buttonRunFridaServer.setOnClickListener { runFridaServer(it) }
        return binding.root
    }

    private fun runFridaServer(view: View?) {
        Log.d(TAG, "runFridaServer: $view")
    }
}