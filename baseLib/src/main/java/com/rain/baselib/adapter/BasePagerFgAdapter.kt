package com.rain.baselib.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *  Create by rain
 *  Date: 2020/11/6
 */
class BasePagerFgAdapter : FragmentStateAdapter {

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
    constructor(fragment: Fragment) : super(fragment)

    private val lists: MutableList<Fragment> = mutableListOf()

    fun setData(list: MutableList<Fragment>) {
        lists.clear()
        lists.addAll(list)
        notifyDataSetChanged()

    }

    override fun getItemCount() = lists.size

    override fun createFragment(position: Int): Fragment {
        return lists[position]
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}