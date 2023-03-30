package com.github.desfate.chatgptandroid.ui.views.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.github.desfate.chatgptandroid.beans.HomeEntity
import com.github.desfate.chatgptandroid.databinding.ItemOpenAiNetBinding

const val OPENAI_NET_TYPE = 1
const val OPENAI_LOCAL_TYPE = 2
class HomeAdapter(data: List<HomeEntity>) : BaseMultiItemAdapter<HomeEntity>(data) {

    class OpenAINetVH(var itemOpenAiNetBinding: ItemOpenAiNetBinding) :
        RecyclerView.ViewHolder(itemOpenAiNetBinding.root)


    init {
        addItemType(OPENAI_NET_TYPE, object : OnMultiItemAdapterListener<HomeEntity, OpenAINetVH> {
            override fun onBind(holder: OpenAINetVH, position: Int, item: HomeEntity?) {
                if (holder?.itemOpenAiNetBinding == null) return
                val binding: ItemOpenAiNetBinding = holder?.itemOpenAiNetBinding!!
                binding.selectTv.text = item?.content
            }

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): OpenAINetVH {
                val viewBinding =
                    ItemOpenAiNetBinding.inflate(LayoutInflater.from(context), parent, false)
                return OpenAINetVH(viewBinding)
            }

        }).onItemViewType { position, list -> // 根据数据，返回对应的 ItemViewType
            OPENAI_NET_TYPE
        }
    }

}