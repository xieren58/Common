package com.example.common.adapter

import android.view.ViewGroup
import com.example.common.BR
import com.example.common.R
import com.example.common.UpdatePic
import com.example.common.databinding.ItemAddPhotoBinding
import com.example.common.databinding.ItemPhotoWeightViewBinding
import com.rain.baselib.adapter.BaseRecAdapter
import com.rain.baselib.common.getBind
import com.rain.baselib.holder.BaseRecHolder

/**
 *  Create by rain
 *  Date: 2020/3/6
 */
class PhotoWeightAdapter : BaseRecAdapter<UpdatePic>() {
	companion object {
		const val ADD_TYPE = 1
	}
	
	override fun createHolder(parent: ViewGroup, viewType: Int, layoutResId: Int, variableId: Int): BaseRecHolder<UpdatePic, *> {
		if (viewType == ADD_TYPE) return BaseRecHolder<UpdatePic, ItemAddPhotoBinding>(parent.getBind(layoutResId), variableId)
		return PhotoHolder(parent.getBind(layoutResId), variableId)
	}
	
	override fun getItemViewType(position: Int): Int {
		val itemData = getItemData(position) ?: return 0
		return itemData.itemType
	}
	
	class PhotoHolder(dataBind: ItemPhotoWeightViewBinding, variableId: Int) : BaseRecHolder<UpdatePic, ItemPhotoWeightViewBinding>(dataBind, variableId)
	
	override fun getLayoutResId(viewType: Int): Int {
		return if (viewType == ADD_TYPE) R.layout.item_add_photo else R.layout.item_photo_weight_view
	}
	
	override fun getVariableId(viewType: Int): Int {
		return if (viewType == ADD_TYPE) -1 else BR.updatePicId
	}
	
}