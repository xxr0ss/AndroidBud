package re.xx.androidbud.bud_tools

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import re.xx.androidbud.databinding.FragmentShellCmdBinding
import re.xx.androidbud.utils.Shell

class ShellCmdFragment : Fragment() {
    private var _binding: FragmentShellCmdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        _binding = FragmentShellCmdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRunCmd.setOnClickListener {
            val cmd = binding.shellCmdInput.text.toString()
            if (cmd == "") return@setOnClickListener
            binding.shellCmdOutput.text = Shell.execCmd(cmd)
        }

        binding.buttonRunSuCmd.setOnClickListener {
            val cmd = binding.shellCmdInput.text.toString()
            if (cmd == "") return@setOnClickListener
            binding.shellCmdOutput.text = Shell.execRootCmd(cmd)
        }
    }

    companion object {
        const val TAG = "ShellCmdFragment"
    }
}