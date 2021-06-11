//package com.example.common.dataSourse
//
//import androidx.paging.PagingSource
//import com.example.common.http.RetrofitFac
//import com.example.common.http.scope.ResultThrowable
//import com.example.common.model.PatientMainModel
//
///**
// *  Create by rain
// *  Date: 2021/4/21
// */
//class PixDataSource : PagingSource<Int, PatientMainModel>() {
//	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PatientMainModel> {
//		return try {
//			val key = params.key ?: 1
//			val loadDemo = RetrofitFac.iData.loadPatientList(key, params.loadSize, "")
//			val data = loadDemo.data
//			if (data == null) {
//				LoadResult.Error(ResultThrowable(loadDemo.result, loadDemo.errorMsg))
//			} else LoadResult.Page(data = data, prevKey = null, nextKey = key + 1)
//		} catch (e: Exception) {
//			LoadResult.Error(e)
//		}
//	}
//}