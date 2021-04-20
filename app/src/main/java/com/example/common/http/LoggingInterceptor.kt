package com.example.common.http

import androidx.annotation.NonNull
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl

class LoggingInterceptor : Interceptor {
	override fun intercept(@NonNull chain: Interceptor.Chain): Response {
		return chain.proceed(addParams(chain.request()).build())
	}
	
	/**
	 * 添加统一参数
	 */
	private fun addParams(request: Request): Request.Builder {
		return getRequestData(request, request.url)
	}
	
	private fun getRequestData(request: Request, newUrl: HttpUrl): Request.Builder {
		val method = request.method
		val body = request.body
		val requestData = if (method == "POST") {
			if (body != null && body is FormBody) {
				val bodyBuilder = FormBody.Builder()
				for (i in 0 until body.size) {
					bodyBuilder.add(body.name(i), body.value(i))
				}
				bodyBuilder.add("source", "2")
				bodyBuilder.add("appname", "highrisk")
				bodyBuilder.add("version", "126.0")
				bodyBuilder.add("device", "and")
				bodyBuilder.add("token", "eee3ee56-a04d-11e6-8893-00155d00f055")
				request.newBuilder().url(newUrl).post(bodyBuilder.build()).build()
			} else if (body != null && body is MultipartBody) {
				val oldPartList = body.parts
				val builder = MultipartBody.Builder()
				builder.setType(MultipartBody.FORM)
				for (part in oldPartList) {
					builder.addPart(part)
				}
				builder.addPart(MultipartBody.Part.createFormData("source", "2"))
				builder.addPart(MultipartBody.Part.createFormData("appname", "highrisk"))
				builder.addPart(MultipartBody.Part.createFormData("version", "126.0"))
				builder.addPart(MultipartBody.Part.createFormData("device", "and"))
				builder.addPart(MultipartBody.Part.createFormData("token", "eee3ee56-a04d-11e6-8893-00155d00f055"))
				request.newBuilder().post(builder.build()).url(newUrl).build()
			} else {
				val builder = newUrl.newBuilder()
				builder.addEncodedQueryParameter("source", "2")
				builder.addEncodedQueryParameter("appname", "highrisk")
				builder.addEncodedQueryParameter("version", "126.0")
				builder.addEncodedQueryParameter("device", "and")
				builder.addEncodedQueryParameter("token", "eee3ee56-a04d-11e6-8893-00155d00f055")
				request.newBuilder().url(builder.build()).build()
			}
			
		} else {
			val builder = newUrl.newBuilder()
			builder.addEncodedQueryParameter("source", "2")
			builder.addEncodedQueryParameter("appname", "highrisk")
			builder.addEncodedQueryParameter("version", "126.0")
			builder.addEncodedQueryParameter("device", "and")
			builder.addEncodedQueryParameter("token", "eee3ee56-a04d-11e6-8893-00155d00f055")
			request.newBuilder().url(builder.build()).build()
		}
		
		return addHeadParams(requestData)
	}
	
	/**
	 * 添加请求头
	 */
	private fun addHeadParams(request: Request): Request.Builder {
		return request.newBuilder().url(request.url).apply {
			addHeader("projectId", "2f36f162-13ee-4edc-8eb4-ecb1c5a5beb7")
		}
	}
}