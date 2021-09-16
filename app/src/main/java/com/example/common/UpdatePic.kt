package com.example.common


/**
 *  Create by rain
 *  Date: 2020/11/11
 */
class UpdatePic {
    //图片地址
    var url: String? = null
    
    @Transient
    var itemType: Int = 0
    
    var process: Int = 0
    var status: Int = 0
    
    constructor(url: String?, itemType: Int) {
        this.url = url
        this.itemType = itemType
    }
    
    constructor()
    constructor(url: String?) : this(url, 0)
    
    
}