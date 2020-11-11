package com.example.common

import android.view.View
import android.view.ViewGroup
import com.rain.baselib.adapter.BaseRecAdapter
import com.rain.baselib.holder.BaseRecHolder
import com.says.common.ui.singleClick
import kotlinx.android.synthetic.main.item_photo_weight_view.*

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


    override fun getViewHolder(viewGroup: ViewGroup, viewType: Int): BaseRecHolder<UpdatePic> {
        when (viewType) {
            ADD_TYPE -> return PhotoAddHolder(viewGroup)
        }
        return PhotoHolder(viewGroup)
    }

    override fun getItemViewType(position: Int): Int {
        return getItemData(position)?.itemType ?: 0

    }

    override fun bindHolder(viewHolder: BaseRecHolder<UpdatePic>, i: Int) {
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

    interface PhotoItemClickListener {
        fun itemAdd()
        fun itemClick(position: Int)
        fun itemDelete(position: Int)
    }

    class PhotoAddHolder(viewGroup: ViewGroup) :
        BaseRecHolder<UpdatePic>(R.layout.item_add_photo, viewGroup,-1 ) {
        override fun setData(model: UpdatePic, position: Int) = Unit
    }

    class PhotoHolder(viewGroup: ViewGroup) :
        BaseRecHolder<UpdatePic>(R.layout.item_photo_weight_view,viewGroup, BR.updatePicId) {
        fun setDeleteListener(clickListener: View.OnClickListener) {
            iv_delete?.singleClick(clickListener)
        }
    }


}