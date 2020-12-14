package com.example.common

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.common.adapter.PhotoWeightAdapter
import com.example.common.http.RetrofitFac
import com.example.common.http.scope.launchUI
import com.example.common.model.CityModel
import com.example.common.model.ConfigResultModel
import com.example.common.model.DictModel
import com.rain.baselib.viewModel.BaseRecViewModel

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class DemoListViewModel : BaseRecViewModel<CityModel>() {
	override val adapter by lazy { PhotoWeightAdapter() }
	
	
	override fun loadData() {
		viewModelScope.launchUI({ RetrofitFac.iData.loadConfig() }, {
			configResult(it)
		}, {
			loadFail()
		})
	}
	
	private fun configResult(configModel: ConfigResultModel?) {
		if (configModel == null) {
			loadFail()
			return
		}
		
		loadSuccess(configModel.cities?.filter {
			!it.parent.isNullOrEmpty() && it.parent == "0"
		}?.apply {
			forEach {
				val value = it.value
				if (value.isNullOrEmpty()) return@forEach
				it.childCityList =	configModel.cities?.filter { cityIt ->
					!cityIt.parent.isNullOrEmpty() && cityIt.parent != "0" && cityIt.parent == value
				}?.toMutableList()
			}
		}?.toMutableList())
	}
}