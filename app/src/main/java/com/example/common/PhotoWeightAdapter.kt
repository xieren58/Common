package com.example.common

import android.view.View
import com.example.common.databinding.ItemPhotoWeightViewBinding
import com.rain.baselib.adapter.BaseRecAdapter
import com.rain.baselib.holder.BaseRecHolder
import com.says.common.ui.singleClick

/**
 *  Create by rain
 *  Date: 2020/3/6
 */
class PhotoWeightAdapter : BaseRecAdapter<UpdatePic>() {
	companion object {
		const val ADD_TYPE = 1
	}
	
	override fun setData(list: MutableList<UpdatePic>?) {
		val lists = getLists()
		lists.clear()
		lists.add(0, UpdatePic(null, ADD_TYPE))
		if (!list.isNullOrEmpty()) lists.addAll(list)
		notifyDataSetChanged()
	}
	
	override fun getItemViewType(position: Int): Int {
		return getItemData(position)?.itemType ?: 0
		
	}
	
	override fun bindHolder(viewHolder: BaseRecHolder<UpdatePic, *>, i: Int) {
		if (viewHolder is PhotoHolder) {
			viewHolder.setDeleteListener(View.OnClickListener { itemAddClickListener?.itemDelete(i) })
		}
	}
	
	private var itemAddClickListener: PhotoItemClickListener? = null
	
	fun setPhotoItemClickListener(itemClickListener: PhotoItemClickListener) {
		this.itemAddClickListener = itemClickListener
		setOnItemClickListener(object : OnItemClickListener {
			override fun itemClick(position: Int) {
				val itemData = getItemData(position) ?: return
				if (itemData.itemType == ADD_TYPE) {
					itemAddClickListener?.itemAdd()
					return
				}
				itemAddClickListener?.itemClick(position)
			}
		})
	}
	
	override fun createHolder(view: View, viewType: Int, variableId: Int): BaseRecHolder<UpdatePic, *> {
		if (viewType == ADD_TYPE) return super.createHolder(view, viewType, variableId)
		return  PhotoHolder(view,variableId)
	}
	interface PhotoItemClickListener {
		fun itemAdd()
		fun itemClick(position: Int)
		fun itemDelete(position: Int)
	}
	
	class PhotoHolder(view: View, variableId: Int) : BaseRecHolder<UpdatePic, ItemPhotoWeightViewBinding>(view, variableId) {
		fun setDeleteListener(clickListener: View.OnClickListener) {
			dataBind?.ivDelete?.singleClick(clickListener)
		}
	}
	
	override fun getLayoutResId(viewType: Int) :Int{
		if (viewType == ADD_TYPE)return  R.layout.item_add_photo
		return R.layout.item_photo_weight_view
	}
	
	override fun getVariableId(viewType: Int): Int {
		if (viewType == ADD_TYPE)	return -1
		return BR.updatePicId
	}
	
	
}