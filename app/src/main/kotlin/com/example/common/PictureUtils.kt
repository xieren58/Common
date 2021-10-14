package com.example.common

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.style.PictureParameterStyle

object PictureUtils {
    private const val COMPRESS_SIZE = 90 //压缩率
    private const val CUT_SIZE = 90 //裁剪输出质量
    
    
    private var mPictureParameterStyle: PictureParameterStyle? = null
    
    fun getTakeImages(data: Intent): MutableList<String> {
        // 图片选择结果回调
        val obtainMultipleResult = PictureSelector.obtainMultipleResult(data)
        val paths: MutableList<String> = mutableListOf()
        obtainMultipleResult.forEach {
            Log.d("pictureTag", "isCut:${it.isCut},isCompressed:${it.isCompressed}")
            Log.d("pictureTag", "duration:${it.duration},width${it.width},height:${it.height}")
            Log.d("pictureTag", "cutPath:${it.cutPath},compressPath:${it.compressPath},path:${it.path},realPath:${it.realPath},originalPath:${it.originalPath},androidQToPath:${it.androidQToPath}")
            val path: String = if (it.isCut && !it.isCompressed) it.cutPath //裁剪后的路径
            else if (it.isCompressed) it.compressPath //压缩后的路径
            else if (!it.realPath.isNullOrEmpty()) it.realPath //真实路径
            else if (!it.androidQToPath.isNullOrEmpty()) it.androidQToPath //androidQ复制路径
            else it.path //原图路径
            
            paths.add(path)
        }
        return paths
    }
    
    /**
     * 拍照-fragment
     */
    fun onPickFromCapture(fragment: Fragment, isCut: Boolean = false, isGridCut: Boolean = true, isCompress: Boolean = false, blockResultBlock: (MutableList<String>?) -> Unit) {
        PictureSelector.create(fragment)
                .openCamera(PictureMimeType.ofImage()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .isWeChatStyle(true) // 是否开启微信图片选择风格
                .isUseCustomCamera(false) // 是否使用自定义相机
                .selectionMode(PictureConfig.SINGLE) // 多选 or 单选
                .isPreviewImage(true) // 是否可预览图片
                .isCompress(isCompress) // 是否压缩
                .compressQuality(COMPRESS_SIZE) // 图片压缩后输出质量 0~ 100
                .synOrAsy(true) //同步false或异步true 压缩 默认同步
                .cutOutQuality(CUT_SIZE) // 裁剪输出质量 默认100
                .isEnableCrop(isCut)
                .isAndroidQTransform(false)
                .circleDimmedLayer(!isGridCut)
                .showCropFrame(isGridCut)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(isGridCut)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {
                        val paths: MutableList<String> = mutableListOf()
                        result?.forEach {
                            val path: String = if (it.isCut && !it.isCompressed) it.cutPath //裁剪后的路径
                            else if (it.isCompressed) it.compressPath //压缩后的路径
                            else if (!it.realPath.isNullOrEmpty()) it.realPath //真实路径
                            else if (!it.androidQToPath.isNullOrEmpty()) it.androidQToPath //androidQ复制路径
                            else it.path //原图路径
                            
                            paths.add(path)
                        }
                        blockResultBlock(paths)
                    }
                    
                    override fun onCancel() = blockResultBlock(null)
                }) //结果回调onActivityResult code
    }
    
    /**
     * 拍照-activity
     */
    fun onPickFromCapture(activity: Activity, isCut: Boolean = false, isGridCut: Boolean = true, isCompress: Boolean = true, blockResultBlock: (MutableList<String>?) -> Unit) {
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .isWeChatStyle(true) // 是否开启微信图片选择风格
                .isUseCustomCamera(false) // 是否使用自定义相机
                .selectionMode(PictureConfig.SINGLE) // 多选 or 单选
                .isPreviewImage(true) // 是否可预览图片
                .isCompress(isCompress) // 是否压缩
                .compressQuality(COMPRESS_SIZE) // 图片压缩后输出质量 0~ 100
                .synOrAsy(true) //同步false或异步true 压缩 默认同步
                .cutOutQuality(90) // 裁剪输出质量 默认100
                .isEnableCrop(isCut)
                .circleDimmedLayer(!isGridCut)
                .showCropFrame(isGridCut)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(isGridCut)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .isAndroidQTransform(false)
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {
                        val paths: MutableList<String> = mutableListOf()
                        result?.forEach {
                            val path: String = if (it.isCut && !it.isCompressed) it.cutPath //裁剪后的路径
                            else if (it.isCompressed) it.compressPath //压缩后的路径
                            else if (!it.realPath.isNullOrEmpty()) it.realPath //真实路径
                            else if (!it.androidQToPath.isNullOrEmpty()) it.androidQToPath //androidQ复制路径
                            else it.path //原图路径
                            
                            paths.add(path)
                        }
                        blockResultBlock(paths)
                    }
                    
                    override fun onCancel() = blockResultBlock(null)
                }) //结果回调onActivityResult code
    }
    
    /**
     * 相冊選擇-fragment
     */
    fun onPickFromGallery(fragment: Fragment, isCut: Boolean = false, isGridCut: Boolean = true, isCompress: Boolean = false, isSingle: Boolean = true, maxSize: Int = 1, blockResultBlock: (MutableList<String>?) -> Unit) {
        getDefaultStyle()
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .isWeChatStyle(true) // 是否开启微信图片选择风格
                .isUseCustomCamera(false) // 是否使用自定义相机
                .imageSpanCount(4) // 每行显示个数
                .isReturnEmpty(false) // 未选择数据时点击按钮是否可以返回
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) // 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(false) // 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                .selectionMode(if (isSingle) PictureConfig.SINGLE else PictureConfig.MULTIPLE) // 多选 or 单选
                .maxSelectNum(maxSize)
                .isSingleDirectReturn(true) // 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true) // 是否可预览图片
                .isCamera(false) // 是否显示拍照按钮
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
                .setPictureStyle(mPictureParameterStyle)
                .isCompress(isCompress) // 是否压缩
                .compressQuality(COMPRESS_SIZE) // 图片压缩后输出质量 0~ 100
                .synOrAsy(true) //同步false或异步true 压缩 默认同步
                .isGif(true) // 是否显示gif图片
                .isOpenClickSound(false) // 是否开启点击声音
                .isAndroidQTransform(false)
                .cutOutQuality(CUT_SIZE) // 裁剪输出质量 默认100
                .isEnableCrop(isCut) // 是否裁剪
                .circleDimmedLayer(!isGridCut)
                .showCropFrame(isCut)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(isCut)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {
                        val paths: MutableList<String> = mutableListOf()
                        result?.forEach {
                            val path: String = if (it.isCut && !it.isCompressed) it.cutPath //裁剪后的路径
                            else if (it.isCompressed) it.compressPath //压缩后的路径
                            else if (!it.realPath.isNullOrEmpty()) it.realPath //真实路径
                            else if (!it.androidQToPath.isNullOrEmpty()) it.androidQToPath //androidQ复制路径
                            else it.path //原图路径
                            
                            paths.add(path)
                        }
                        blockResultBlock(paths)
                    }
                    
                    override fun onCancel() = blockResultBlock(null)
                }) //结果回调onActivityResult code
    }
    
    /**
     * 相冊選擇-activity
     */
    fun onPickFromGallery(activity: Activity, isCut: Boolean = false, isGridCut: Boolean = true, isCompress: Boolean = false, isSingle: Boolean = true, maxSize: Int = 1, blockResultBlock: (MutableList<String>?) -> Unit) {
        getDefaultStyle()
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .isWeChatStyle(true) // 是否开启微信图片选择风格
                .isUseCustomCamera(false) // 是否使用自定义相机
                .imageSpanCount(4) // 每行显示个数
                .isReturnEmpty(false) // 未选择数据时点击按钮是否可以返回
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) // 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(false) // 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                .selectionMode(if (isSingle) PictureConfig.SINGLE else PictureConfig.MULTIPLE) // 多选 or 单选
                .maxSelectNum(maxSize)
                .isSingleDirectReturn(true) // 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true) // 是否可预览图片
                .isCamera(false) // 是否显示拍照按钮
                .circleDimmedLayer(!isGridCut)
                .showCropFrame(isGridCut)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(isGridCut)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
                .isEnableCrop(isCut) // 是否裁剪
                .isCompress(isCompress) // 是否压缩
                .compressQuality(COMPRESS_SIZE) // 图片压缩后输出质量 0~ 100
                .synOrAsy(true) //同步false或异步true 压缩 默认同步
                .isGif(true) // 是否显示gif图片
                .isOpenClickSound(false) // 是否开启点击声音
                .cutOutQuality(CUT_SIZE) // 裁剪输出质量 默认100
                .isAndroidQTransform(false)
                .setPictureStyle(mPictureParameterStyle)
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {
                        val paths: MutableList<String> = mutableListOf()
                        result?.forEach {
                            val path: String = if (it.isCut && !it.isCompressed) it.cutPath //裁剪后的路径
                            else if (it.isCompressed) it.compressPath //压缩后的路径
                            else if (!it.realPath.isNullOrEmpty()) it.realPath //真实路径
                            else if (!it.androidQToPath.isNullOrEmpty()) it.androidQToPath //androidQ复制路径
                            else it.path //原图路径
                            
                            paths.add(path)
                        }
                        blockResultBlock(paths)
                    }
                    
                    override fun onCancel() = blockResultBlock(null)
                }) //结果回调onActivityResult code
    }
    
    /**
     * 拍摄视频-fragment
     */
    fun onPickFromVideoCapture(fragment: Fragment, isCut: Boolean = false, isGridCut: Boolean = true, isCompress: Boolean = false, blockResultBlock: (MutableList<String>?) -> Unit) {
        PictureSelector.create(fragment)
                .openCamera(PictureMimeType.ofVideo()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .isWeChatStyle(true) // 是否开启微信图片选择风格
                .isUseCustomCamera(false) // 是否使用自定义相机
                .videoQuality(1)
                .recordVideoSecond(15)
                .selectionMode(PictureConfig.SINGLE) // 多选 or 单选
                .isPreviewImage(true) // 是否可预览图片
                .isCompress(isCompress) // 是否压缩
                .compressQuality(COMPRESS_SIZE) // 图片压缩后输出质量 0~ 100
                .synOrAsy(true) //同步false或异步true 压缩 默认同步
                .cutOutQuality(90) // 裁剪输出质量 默认100
                .isEnableCrop(isCut)
                .showCropFrame(isGridCut)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(isGridCut)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .isAndroidQTransform(false)
                .circleDimmedLayer(isGridCut)
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {
                        val paths: MutableList<String> = mutableListOf()
                        result?.forEach {
                            val path: String = if (it.isCut && !it.isCompressed) it.cutPath //裁剪后的路径
                            else if (it.isCompressed) it.compressPath //压缩后的路径
                            else if (!it.realPath.isNullOrEmpty()) it.realPath //真实路径
                            else if (!it.androidQToPath.isNullOrEmpty()) it.androidQToPath //androidQ复制路径
                            else it.path //原图路径
                            
                            paths.add(path)
                        }
                        blockResultBlock(paths)
                    }
                    
                    override fun onCancel() = blockResultBlock(null)
                }) //结果回调onActivityResult code
    }
    
    /**
     * 拍摄视频-activity
     */
    fun onPickFromVideoCapture(activity: Activity, isCut: Boolean = false, isGridCut: Boolean = true, isCompress: Boolean = false, blockResultBlock: (MutableList<String>?) -> Unit) {
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofVideo()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .isWeChatStyle(true) // 是否开启微信图片选择风格
                .isUseCustomCamera(false) // 是否使用自定义相机
                .videoQuality(1)
                .recordVideoSecond(15)
                .selectionMode(PictureConfig.SINGLE) // 多选 or 单选
                .isPreviewImage(true) // 是否可预览图片
                .isCompress(isCompress) // 是否压缩
                .compressQuality(COMPRESS_SIZE) // 图片压缩后输出质量 0~ 100
                .synOrAsy(true) //同步false或异步true 压缩 默认同步
                .cutOutQuality(90) // 裁剪输出质量 默认100
                .isEnableCrop(isCut)
                .showCropFrame(isGridCut)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(isGridCut)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .isAndroidQTransform(false)
                .circleDimmedLayer(isGridCut)
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {
                        val paths: MutableList<String> = mutableListOf()
                        result?.forEach {
                            val path: String = if (it.isCut && !it.isCompressed) it.cutPath //裁剪后的路径
                            else if (it.isCompressed) it.compressPath //压缩后的路径
                            else if (!it.realPath.isNullOrEmpty()) it.realPath //真实路径
                            else if (!it.androidQToPath.isNullOrEmpty()) it.androidQToPath //androidQ复制路径
                            else it.path //原图路径
                            
                            paths.add(path)
                        }
                        blockResultBlock(paths)
                    }
                    
                    override fun onCancel() = blockResultBlock(null)
                }) //结果回调onActivityResult code
    }
    
    /**
     * 选择视频-fragment
     */
    fun onPickFromAll(fragment: Fragment, isCut: Boolean = false, isGridCut: Boolean = true, isCompress: Boolean = false, isSingle: Boolean = true, maxSize: Int = 1, blockResultBlock: (MutableList<String>?) -> Unit) {
        getDefaultStyle()
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofAll()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .isWeChatStyle(true) // 是否开启微信图片选择风格
                .isUseCustomCamera(false) // 是否使用自定义相机
                .imageSpanCount(4) // 每行显示个数
                .isReturnEmpty(false) // 未选择数据时点击按钮是否可以返回
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) // 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(true) // 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                .selectionMode(if (isSingle) PictureConfig.SINGLE else PictureConfig.MULTIPLE) // 多选 or 单选
                .maxSelectNum(maxSize)
                .videoQuality(1)
                .isSingleDirectReturn(true) // 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true) // 是否可预览图片
                .isCamera(false) // 是否显示拍照按钮
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
                .setPictureStyle(mPictureParameterStyle)
                .isCompress(isCompress) // 是否压缩
                .compressQuality(COMPRESS_SIZE) // 图片压缩后输出质量 0~ 100
                .synOrAsy(true) //同步false或异步true 压缩 默认同步
                .isGif(true) // 是否显示gif图片
                .isOpenClickSound(false) // 是否开启点击声音
                .isAndroidQTransform(false)
                .cutOutQuality(CUT_SIZE) // 裁剪输出质量 默认100
                .isEnableCrop(isCut) // 是否裁剪
                .circleDimmedLayer(!isGridCut)
                .showCropFrame(isCut)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(isCut)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {
                        val paths: MutableList<String> = mutableListOf()
                        result?.forEach {
                            val path: String = if (it.isCut && !it.isCompressed) it.cutPath //裁剪后的路径
                            else if (it.isCompressed) it.compressPath //压缩后的路径
                            else if (!it.realPath.isNullOrEmpty()) it.realPath //真实路径
                            else if (!it.androidQToPath.isNullOrEmpty()) it.androidQToPath //androidQ复制路径
                            else it.path //原图路径
                            
                            paths.add(path)
                        }
                        blockResultBlock(paths)
                    }
                    
                    override fun onCancel() = blockResultBlock(null)
                }) //结果回调onActivityResult code
    }
    
    /**
     * 选择视频-activity
     */
    fun onPickFromAll(activity: Activity, isCut: Boolean = false, isGridCut: Boolean = true, isCompress: Boolean = false, isSingle: Boolean = true, maxSize: Int = 1, blockResultBlock: (MutableList<String>?) -> Unit) {
        getDefaultStyle()
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofAll()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
                .isWeChatStyle(true) // 是否开启微信图片选择风格
                .isUseCustomCamera(false) // 是否使用自定义相机
                .imageSpanCount(4) // 每行显示个数
                .isReturnEmpty(false) // 未选择数据时点击按钮是否可以返回
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) // 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(true) // 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                .selectionMode(if (isSingle) PictureConfig.SINGLE else PictureConfig.MULTIPLE) // 多选 or 单选
                .maxSelectNum(maxSize)
                .videoQuality(1)
                .isSingleDirectReturn(true) // 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true) // 是否可预览图片
                .isPreviewVideo(true)
                .isCamera(false) // 是否显示拍照按钮
                .circleDimmedLayer(!isGridCut)
                .showCropFrame(isGridCut)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(isGridCut)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
                .isEnableCrop(isCut) // 是否裁剪
                .isCompress(isCompress) // 是否压缩
                .compressQuality(COMPRESS_SIZE) // 图片压缩后输出质量 0~ 100
                .synOrAsy(true) //同步false或异步true 压缩 默认同步
                .isGif(true) // 是否显示gif图片
                .isOpenClickSound(false) // 是否开启点击声音
                .cutOutQuality(CUT_SIZE) // 裁剪输出质量 默认100
                .isAndroidQTransform(false)
                .setPictureStyle(mPictureParameterStyle)
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {
                        val paths: MutableList<String> = mutableListOf()
                        result?.forEach {
                            val path: String = if (it.isCut && !it.isCompressed) it.cutPath //裁剪后的路径
                            else if (it.isCompressed) it.compressPath //压缩后的路径
                            else if (!it.realPath.isNullOrEmpty()) it.realPath //真实路径
                            else if (!it.androidQToPath.isNullOrEmpty()) it.androidQToPath //androidQ复制路径
                            else it.path //原图路径
                            
                            paths.add(path)
                        }
                        blockResultBlock(paths)
                    }
                    
                    override fun onCancel() = blockResultBlock(null)
                }) //结果回调onActivityResult code
    }
    
    private fun getDefaultStyle(): PictureParameterStyle {
        if (mPictureParameterStyle == null) mPictureParameterStyle = PictureParameterStyle()
        // 相册列表勾选图片样式
//		mPictureParameterStyle?.pictureCheckedStyle = R.drawable.select_picture_checkbox
        mPictureParameterStyle?.isOpenCheckNumStyle = true
        // 相册状态栏背景色
        mPictureParameterStyle?.pictureStatusBarColor = Color.parseColor("#393a3e")
        // 相册列表标题栏背景色
        mPictureParameterStyle?.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e")
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle?.pictureTitleUpResId = R.drawable.picture_icon_arrow_up
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle?.pictureTitleDownResId = R.drawable.picture_icon_arrow_down
        // 相册文件夹列表选中圆点
        mPictureParameterStyle?.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval
        // 相册返回箭头
        mPictureParameterStyle?.pictureLeftBackIcon = R.drawable.picture_icon_back
        // 标题栏字体颜色
        mPictureParameterStyle?.pictureTitleTextColor =
                ContextCompat.getColor(MyApp.context, R.color.picture_color_white)
        return mPictureParameterStyle!!
    }
}
