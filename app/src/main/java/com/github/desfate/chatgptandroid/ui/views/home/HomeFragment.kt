package com.github.desfate.chatgptandroid.ui.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.desfate.chatgptandroid.R
import com.github.desfate.chatgptandroid.databinding.FragmentHomeBinding
import com.github.desfate.chatgptandroid.ui.views.picker.PickerTools
import com.github.gzuliyujiang.wheelpicker.OptionPicker
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * 主页面 可选择或是配置对应的聊天模式
 * 1. 网络模式版本 OpenAI
 * 2. 本地版本 OpenAI GPT
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by viewModels()
    lateinit var adapter: HomeAdapter
    lateinit var gptPicker: OptionPicker
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        adapter = HomeAdapter(homeViewModel.testData)
        binding.recycleView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recycleView.adapter = adapter
        // 处理默认值
        homeViewModel.testData[0].content = homeViewModel.gptType[0].name

        bindClick()

        return root
    }

    fun bindClick(){

        val navController = findNavController(this)
        adapter.addOnItemChildClickListener(R.id.select_tv){ adapter, view, position ->
            gptPicker = PickerTools.initGPTPicker(requireActivity())
            gptPicker.setOnOptionPickedListener { position, item ->
                (view as TextView).text = item.toString()            }
            gptPicker.show()
        }

        adapter.addOnItemChildClickListener(R.id.key_btn) { adapter, view, position ->
            navController.navigate(R.id.action_nav_home_to_nav_gallery)
        }

        adapter.addOnItemChildClickListener(R.id.submit_btn) { adapter, view, position ->
            val bundle = bundleOf("chat_type" to "text")
            navController.navigate(R.id.action_nav_home_to_nav_chat, bundle)
        }

        adapter.addOnItemChildClickListener(R.id.submit_image_btn) { adapter, view, position ->
            val bundle = bundleOf("chat_type" to "image")
            navController.navigate(R.id.action_nav_home_to_nav_chat, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}