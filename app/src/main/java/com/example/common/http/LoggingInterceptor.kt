package com.example.common.http

import androidx.annotation.NonNull
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okio.Buffer
import java.nio.charset.Charset

class LoggingInterceptor : Interceptor {
	private val utf8 = Charset.forName("UTF-8")
	
	override fun intercept(@NonNull chain: Interceptor.Chain): Response {
		val newRequest = addParams(chain.request())
		val builder = addHeadParams(newRequest)
		val build = builder.build()
		return chain.proceed(build)
	}
	
	
	private fun getRequestBody(requestBody: RequestBody?): String? {
		var body: String? = null
		if (requestBody != null) {
			val buffer = Buffer()
			requestBody.writeTo(buffer)
			
			var charset: Charset? = utf8
			val contentType = requestBody.contentType()
			if (contentType != null) {
				charset = contentType.charset(utf8)
			}
			if (charset != null)
				body = buffer.readString(charset)
		}
		return body
	}
	
	
	//添加统一参数
	private fun addParams(request: Request): Request {
		val method = request.method
		val oldUrl = request.url
		val builder = oldUrl.newBuilder()
		val body = request.body
		return if (method == "POST" && body != null && body is FormBody) {
			val bodyBuilder = FormBody.Builder()
			for (i in 0 until body.size) {
				bodyBuilder.add(body.name(i), body.value(i))
			}
			bodyBuilder.add("language", "1")
			request.newBuilder().url(oldUrl).post(bodyBuilder.build()).build()
		} else {
			builder.addEncodedQueryParameter("language", "1")
			request.newBuilder()
					.url(builder.build())
					.build()
		}
	}
	
	
	/**
	 * 替换链接
	 */
	private fun modifyBaseUrl(oldUrl: HttpUrl, url: String): HttpUrl {
		val toHttpUrl = url.toHttpUrl()
		return oldUrl.newBuilder()
				.scheme(toHttpUrl.scheme)
				.host(toHttpUrl.host)//更换主机名
				.port(toHttpUrl.port)//更换端口
				.build()
	}
	
	private fun addHeadParams(request: Request): Request.Builder {
		val builder = request.newBuilder().url(request.url)
		builder.addHeader("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySW5mbyI6InVzZXJfMzI2MyJ9.elKLxCOqHptpIv-PvaC-igMPUQ_7KsWDGuJE9XLqtSI")
		builder.addHeader("version", "18")
		builder.addHeader("device", "and")
		return builder
	}
	
}