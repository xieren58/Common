package com.example.common.adapter

import android.view.View
import com.example.common.BR
import com.example.common.R
import com.example.common.UpdatePic
import com.example.common.databinding.ItemAddPhotoBinding
import com.example.common.databinding.ItemPhotoWeightViewBinding
import com.rain.baselib.adapter.BaseRecAdapter
import com.rain.baselib.holder.BaseRecHolder

/**
 *  Create by rain
 *  Date: 2020/3/6
 */
class PhotoWeightAdapter : BaseRecAdapter<UpdatePic>() {
	companion object {
		const val ADD_TYPE = 1
	}
	
	override fun createHolder(view: View, viewType: Int, variableId: Int): BaseRecHolder<UpdatePic, *> {
		if (viewType == ADD_TYPE) return BaseRecHolder<UpdatePic, ItemAddPhotoBinding>(view, variableId)
		return PhotoHolder(view, variableId)
	}
	
	override fun getItemViewType(position: Int): Int {
		val itemData = getItemData(position) ?: return 0
		return itemData.itemType
	}
	
	class PhotoHolder(view: View, variableId: Int) : BaseRecHolder<UpdatePic, ItemPhotoWeightViewBinding>(view, variableId)
	
	override fun getLayoutResId(viewType: Int): Int {
		if (viewType == ADD_TYPE) {
			return R.layout.item_add_photo
		}
		return R.layout.item_photo_weight_view
	}
	
	override fun getVariableId(viewType: Int): Int {
		if (viewType == ADD_TYPE) {
			return -1
		}
		return BR.updatePicId
	}
	
}