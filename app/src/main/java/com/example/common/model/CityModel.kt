package com.example.common.model

/**
 *  Create by rain
 *  Date: 2020/12/14
 */
class CityModel {
  var name:String?=null//":"Gatundu South",
  var value:String?=null //":"1003002",
  var level:Int = 0 //":2,
  var parent:String?=null // ":"1003"
  
  
  var isOpen = false
  var childCityList: MutableList<CityModel>? = null
  
}