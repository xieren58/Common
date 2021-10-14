package com.example.common.weight

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.example.common.R
import com.example.common.databinding.LayoutTimeSelectViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.says.common.ui.singleClick
import com.says.common.utils.TimeUtils
import java.util.*

/**
 *  Create by rain
 *  Date: 2021/9/9
 */
class TimeSelectDialog : BottomSheetDialogFragment() {
    class Builder {
        private var format: String? = null
        private var currentDate: String? = null
        private var maxDate: String? = null
        private var minDate: String? = null
        private var titleStr: String? = null
        private var mode: String? = null
        private var resultListener: OnTimeBottomSelectListener? = null
        private var cancelListener: OnRegionCancelListener? = null
        fun show(fragment: Fragment) {
            show(fragment.childFragmentManager)
        }
        
        fun initListener(block: OnTimeBottomSelectListener?): Builder {
            this.resultListener = block
            return this
        }
        
        fun initCancelListener(block: OnRegionCancelListener?): Builder {
            this.cancelListener = block
            return this
        }
        
        fun show(activity: FragmentActivity) {
            show(activity.supportFragmentManager)
        }
        
        fun initData(title: String?, mode: String?, format: String?, currentDate: String?, maxDate: String?, minDate: String?): Builder {
            this.titleStr = title
            this.format = format
            this.currentDate = currentDate
            this.maxDate = maxDate
            this.minDate = minDate
            this.mode = mode
            return this
        }
        
        private fun show(manager: FragmentManager) {
            var timeFragment = manager.findFragmentByTag("timeSelectDialog")
            if (timeFragment == null || timeFragment !is TimeSelectDialog) {
                timeFragment = TimeSelectDialog()
            }
            
            val bundle = Bundle().apply {
                putString("maxDate", maxDate)
                putString("minDate", minDate)
                putString("currentDate", currentDate)
                putString("format", format)
                putString("mode", mode)
                putString("titleStr", titleStr)
            }
            timeFragment.run {
                arguments = bundle
                setTimeListener(resultListener)
                setTimeCancelListener(cancelListener)
            }
            timeFragment.show(manager, "timeSelectDialog")
        }
    }
    
    companion object {
        @JvmStatic
        fun initBuilder(): Builder {
            return Builder()
        }
    }
    
    
    private lateinit var dataBind: LayoutTimeSelectViewBinding
    private var isResultConfirm = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return DataBindingUtil.inflate<LayoutTimeSelectViewBinding>(inflater, R.layout.layout_time_select_view, container, false).apply {
            root.setBackgroundResource(R.drawable.radius_topleft_topright)
            dataBind = this
        }.root
    }
    
    @SuppressLint("PrivateApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.isHideable = false
        return dialog
    }
    
    private var formatStr = "yyyy-MM-dd"
    private var timeBuilder: TimePickerView? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        formatStr = arguments?.getString("format") ?: "yyyy-MM-dd"
        
        dataBind.tvTitle.text = arguments?.getString("titleStr") ?: ""
        initEvent()
        initTimePicker()
    }
    
    private fun initEvent() {
        dataBind.tvCancel.singleClick { dismissAllowingStateLoss() }
        dataBind.tvConfirm.singleClick { timeBuilder?.returnData() }
    }
    
    private fun initTimePicker() {
        val maxDate = TimeUtils.strToDate(arguments?.getString("maxDate"), formatStr)
        val minDate = TimeUtils.strToDate(arguments?.getString("minDate"), formatStr)
        val currentDate = TimeUtils.strToDate(arguments?.getString("currentDate"), formatStr)
        val type = when (arguments?.getString("mode")) {
            "year" -> booleanArrayOf(true, false, false, false, false, false) //只选择年
            "month" -> booleanArrayOf(true, true, false, false, false, false) //年 + 月
            "time" -> booleanArrayOf(false, false, false, true, true, true) //时分秒
            "date" -> booleanArrayOf(true, true, true, false, false, false) //年月日
            "datetime" -> booleanArrayOf(true, true, true, true, true, true) //年月日时分秒
            else -> booleanArrayOf(true, true, true, false, false, false) //默认年月日
        }
        
        val currentCalender = Calendar.getInstance()
        
        val maxCalender = Calendar.getInstance()
        if (maxDate != null) {
            maxCalender.time = maxDate
        } else {
            maxCalender.set(currentCalender.get(Calendar.YEAR) + 20, 11, 31)
        }
        val minCalender = Calendar.getInstance()
        if (minDate != null) {
            minCalender.time = minDate
        } else {
            minCalender.set(1800, 0, 1)
        }
        currentCalender.time = currentDate ?: Date()
        timeBuilder = TimePickerBuilder(requireContext(), object : OnTimeSelectListener {
            override fun onTimeSelect(date: Date?, v: View?) {
                val dataToStr = TimeUtils.dataToStr(date, formatStr)
                if (dataToStr.isNullOrEmpty()) return
                timeSelectListener?.resultTime(dataToStr)
                isResultConfirm = true
                dismissAllowingStateLoss()
            }
        }).setType(type)
                .setLabel(getString(com.bigkoo.pickerview.R.string.pickerview_year),
                        getString(com.bigkoo.pickerview.R.string.pickerview_month),
                        getString(com.bigkoo.pickerview.R.string.pickerview_day),
                        getString(com.bigkoo.pickerview.R.string.pickerview_hours),
                        getString(com.bigkoo.pickerview.R.string.pickerview_minutes),
                        getString(com.bigkoo.pickerview.R.string.pickerview_seconds))
                .setLayoutRes(R.layout.layout_time_select_custom) {}
                .setContentTextSize(14)
                .setDecorView(dataBind.flTime)
                .setDate(currentCalender)
                .setOutSideCancelable(false)
                .setLineSpacingMultiplier(2.3f)
                .setDividerColor(Color.parseColor("#F6F8FB"))
                .setTextXOffset(5, 5, 5, 5, 5, 5)
                .setTextColorCenter(Color.parseColor("#30322C"))
                .setTextColorOut(Color.parseColor("#86949E"))
                .setRangDate(minCalender, maxCalender)
                .build()
        timeBuilder?.show(false)
    }
    
    private var timeSelectListener: OnTimeBottomSelectListener? = null
    fun setTimeListener(timeSelectListener: OnTimeBottomSelectListener?) {
        this.timeSelectListener = timeSelectListener
    }
    
    private var timeCancelListener: OnRegionCancelListener? = null
    fun setTimeCancelListener(timeCancelListener: OnRegionCancelListener?) {
        this.timeCancelListener = timeCancelListener
    }
    
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d("regionTag", "onDismiss:$isResultConfirm")
        if (!isResultConfirm) timeCancelListener?.cancel()
    }
}

interface OnTimeBottomSelectListener {
    fun resultTime(time: String)
}

interface OnRegionCancelListener {
    fun cancel()
}