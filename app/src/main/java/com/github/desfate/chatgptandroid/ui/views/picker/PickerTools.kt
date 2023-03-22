package com.github.desfate.chatgptandroid.ui.views.picker

import android.app.Activity
import com.github.desfate.gptcore.config.getGptTypeArray
import com.github.gzuliyujiang.wheelpicker.OptionPicker

class PickerTools {


    companion object{
        var GPT_TYPE = 0

        /**
         * 返回选择器数据
         */
        fun initGPTPicker(activity: Activity): OptionPicker{
            val adapterData = getGptTypeArray().map { it.name }
            val picker = OptionPicker(activity)
            picker.setTitle("GPT VERSION");
            picker.setDefaultPosition(GPT_TYPE);
            picker.setData(adapterData)
            return picker
        }




    }

}