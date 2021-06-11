//package com.example.common.http
//
//import android.util.Log
//import androidx.paging.PagingSource
//
///**
// *  Create by rain
// *  Date: 2021/3/9
// */
//abstract class BasePagingSource<T : Any> : PagingSource<Int, T>() {
//	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
//		return try {
//			val loadIndex = loadIndex()
//			Log.d("testLaunchTag", "loadIndex:$loadIndex")
//			Log.d("testLaunchTag", "key:${params.key}")
//			LoadResult.Page(
//					//需要加载的数据
//					data = loadDataRetrofit(params.key) ?: mutableListOf(),
//					//如果可以往上加载更多就设置该参数，否则不设置
//					prevKey = null,
//					//加载下一页的key 如果传null就说明到底了
//					nextKey = if (loadIndex == null) null else loadIndex + 1
//			)
//		} catch (e: Exception) {
//			Log.d("testLaunchTag", "e:$e")
//			return LoadResult.Error(e)
//		}
//	}
//
//	abstract fun loadIndex(): Int?
//	abstract suspend fun loadDataRetrofit(index: Int?): MutableList<T>?
//}