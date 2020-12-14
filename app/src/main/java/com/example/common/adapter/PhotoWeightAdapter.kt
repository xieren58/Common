package com.example.common.adapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.BR
import com.example.common.R
import com.example.common.databinding.ItemPhotoWeightViewBinding
import com.example.common.model.CityModel
import com.example.common.model.DictModel
import com.rain.baselib.adapter.BaseRecAdapter
import com.rain.baselib.holder.BaseRecHolder

/**
 *  Create by rain
 *  Date: 2020/3/6
 */
class PhotoWeightAdapter : BaseRecAdapter<CityModel>() {
	fun setPhotoItemClickListener() {
		setOnItemClickListener(object : OnItemClickListener {
			override fun itemClick(position: Int) {
				val itemData = getItemData(position) ?: return
				itemData.isOpen = !itemData.isOpen
				notifyItemChanged(position)
			}
		})
	}
	
	override fun createHolder(view: View, viewType: Int, variableId: Int): BaseRecHolder<CityModel, *> {
		return PhotoHolder(view, variableId)
	}
	
	class PhotoHolder(view: View, variableId: Int) : BaseRecHolder<CityModel, ItemPhotoWeightViewBinding>(view, variableId) {
		private var childAdapter: ChildShowAdapter? = null
		
		init {
			dataBind?.recChild?.layoutManager = LinearLayoutManager(mContext)
			childAdapter = ChildShowAdapter()
			dataBind?.recChild?.adapter = childAdapter
		}
		
		override fun setData(model: CityModel, position: Int) {
			super.setData(model, position)
			Log.d("dataRecTag","childAdapter:$childAdapter,childCityList:${model.childCityList.isNullOrEmpty()}")
			childAdapter?.setData(model.childCityList)
		}
	}
	
	override fun getLayoutResId(viewType: Int): Int {
		return R.layout.item_photo_weight_view
	}
	
	override fun getVariableId(viewType: Int): Int {
		return BR.updatePicId
	}
	
}