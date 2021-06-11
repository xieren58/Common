//package com.example.common.adapter
//
//import android.util.Log
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.example.common.BR
//import com.example.common.R
//import com.example.common.databinding.ItemFooterViewBinding
//import com.example.common.databinding.ItemPatientFgViewBinding
//import com.example.common.model.PatientMainModel
//import com.rain.baselib.common.getBind
//import com.rain.baselib.common.singleClick
//import com.rain.baselib.holder.BaseRecHolder
//
///**
// *  Create by rain
// *  Date: 2021/4/21
// */
//class PatientLoadStateAdapter(private val adapter: PatientPagingAdapter) : LoadStateAdapter<FooterHolder>() {
//	override fun onBindViewHolder(holder: FooterHolder, loadState: LoadState) {
//		val dataBind = holder.dataBind
//		Log.d("loadStateTag","loadState:$loadState")
//		when (loadState) {
//			is LoadState.Error -> {
//				dataBind.tvLoading.visibility = View.VISIBLE
//				dataBind.tvLoading.text = "Load Failed, Tap Retry"
//				dataBind.tvLoading.singleClick { adapter.retry() }
//			}
//			is LoadState.Loading -> {
//				dataBind.tvLoading.visibility = View.VISIBLE
//				dataBind.tvLoading.text = "loading"
//			}
//			is LoadState.NotLoading -> {
//				dataBind.tvLoading.visibility = View.GONE
//			}
//		}
//	}
//
//	override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FooterHolder {
//		Log.d("loadStateTag","parent:$loadState")
//		return FooterHolder(parent.getBind(R.layout.item_footer_view))
//	}
//}
//
//class FooterHolder(val dataBind: ItemFooterViewBinding) : RecyclerView.ViewHolder(dataBind.root)
//class PatientPagingAdapter : PagingDataAdapter<PatientMainModel, PatientPagingHolder>(diffCallback) {
//	override fun onBindViewHolder(holder: PatientPagingHolder, position: Int) {
//		val patientMainModel = getItem(position) ?: return
//		holder.setData(patientMainModel, position)
//	}
//
//	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientPagingHolder {
//		return PatientPagingHolder(parent.getBind(R.layout.item_patient_fg_view), BR.patientMainModelId)
//	}
//}
//
//private val diffCallback = object : DiffUtil.ItemCallback<PatientMainModel>() {
//	override fun areItemsTheSame(oldItem: PatientMainModel, newItem: PatientMainModel): Boolean =
//			oldItem.id == newItem.id
//
//	override fun areContentsTheSame(oldItem: PatientMainModel, newItem: PatientMainModel): Boolean =
//			oldItem == newItem
//}
//
//class PatientPagingHolder(dataBind: ItemPatientFgViewBinding, variableId: Int) : BaseRecHolder<PatientMainModel, ItemPatientFgViewBinding>(dataBind, variableId = variableId)