package com.example.common.http

import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okio.Buffer
import java.nio.charset.Charset

class LoggingInterceptor : Interceptor {
	private val utf8 = Charset.forName("UTF-8")

	override fun intercept(chain: Interceptor.Chain): Response {
		val newRequest = addParams(chain.request())
		val build = newRequest.build()
		val body = getRequestBody(build.body)
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


	/**
	 * 添加统一参数
	 */
	private fun addParams(request: Request): Request.Builder {
		val oldUrl: HttpUrl = request.url
		return getRequestData(request, oldUrl, 0)
	}

	private fun getRequestData(request: Request, newUrl: HttpUrl, isAddParams: Int): Request.Builder {
		val method = request.method
		val body = request.body
		val requestData = if (isAddParams == 1 || isAddParams == 2) {
			request.newBuilder().url(newUrl).build()
		} else if (isAddParams == 3) {
			request.newBuilder().url(newUrl).build()
		} else {
			if (method == "POST") {
				if (body != null && body is FormBody) {
					val bodyBuilder = FormBody.Builder()
					for (i in 0 until body.size) {
						bodyBuilder.add(body.name(i), body.value(i))
					}
					bodyBuilder.add("source", "2")
					bodyBuilder.add("appname", "91trial")
					bodyBuilder.add("version", "118.0")
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
					builder.addPart(MultipartBody.Part.createFormData("appname","91trial"))
					builder.addPart(MultipartBody.Part.createFormData("version", "118.0"))
					builder.addPart(MultipartBody.Part.createFormData("device", "and"))
					builder.addPart(MultipartBody.Part.createFormData("token", "eee3ee56-a04d-11e6-8893-00155d00f055"))
					request.newBuilder().post(builder.build()).url(newUrl).build()
				} else {
					val builder = newUrl.newBuilder()
					builder.addEncodedQueryParameter("source", "2")
					builder.addEncodedQueryParameter("appname", "91trial")
					builder.addEncodedQueryParameter("version", "118.0")
					builder.addEncodedQueryParameter("device", "and")
					builder.addEncodedQueryParameter("token","eee3ee56-a04d-11e6-8893-00155d00f055")
					request.newBuilder().url(builder.build()).build()
				}

			} else {
				val builder = newUrl.newBuilder()
				builder.addEncodedQueryParameter("source", "2")
				builder.addEncodedQueryParameter("appname","91trial")
				builder.addEncodedQueryParameter("version", "118.0")
				builder.addEncodedQueryParameter("device", "and")
				builder.addEncodedQueryParameter("token", "eee3ee56-a04d-11e6-8893-00155d00f055")
				request.newBuilder().url(builder.build()).build()
			}
		}

		return addHeadParams(requestData, isAddParams)
	}

	/**
	 * 替换链接
	 */
	private fun modifyBaseUrl(oldUrl: HttpUrl, urlStr: String): HttpUrl {
		val toUrl = oldUrl.toString()
		if (toUrl.contains(Api.BASE_URL)) {
			val replace = toUrl.replace(Api.BASE_URL, urlStr)
			return replace.toHttpUrl()
		}
		return oldUrl
	}

	/**
	 * 添加请求头
	 */
	private fun addHeadParams(request: Request, isAddParams: Int): Request.Builder {
		return request.newBuilder().url(request.url).apply {
			when {
				isAddParams == 1 -> {
					addHeader("Host", "recognition.image.myqcloud.com")
				}
				isAddParams == 2 -> {
				}
				isAddParams != 3 -> {
				}
			}
		}
	}

	private fun getOcrValue(str: String?): String {
		if (str.isNullOrEmpty()) return ""
		return str
	}

}