package re.xx.androidbud.bud_tools

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import re.xx.androidbud.R
import re.xx.androidbud.databinding.FragmentHomeBinding


class HomeFragment : Fragment(){
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardRunShellCmd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_shellCmdFragment)
        }
        binding.cardRunServer.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_runServerFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "HomeFragment"
    }
}