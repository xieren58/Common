package com.says.common.signature.listener

/**
 *  Create by rain
 *  Date: 2021/4/2
 */
interface OnSignedListener {
    fun onStartSigning()

    fun onSigned()

    fun onClear()
}