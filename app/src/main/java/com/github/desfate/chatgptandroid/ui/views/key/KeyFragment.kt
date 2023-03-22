package com.github.desfate.chatgptandroid.ui.views.key

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.github.desfate.chatgptandroid.databinding.FragmentKeyBinding
import com.hjq.toast.Toaster
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KeyFragment : Fragment() {

    private var _binding: FragmentKeyBinding? = null
    private val homeViewModel: KeyViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKeyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        bindUi()
        bindIntent()
        return root
    }

    /**
     * ui显示的双向绑定
     */
    private fun bindUi(){
        homeViewModel.viewModelScope.launch {
            homeViewModel.uiState.collect{
                binding.keyEdt.setText(it.key)
            }
        }
    }

    /**
     * 绑定行为
     */
    private fun bindIntent(){
        homeViewModel.initSecretKey(requireContext()) // 获取私钥
        binding.submitBtn.setOnClickListener {
            homeViewModel.intentSubmit(requireContext(), binding.keyEdt.text.toString())
            Toaster.show("Secret Key Saved")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}