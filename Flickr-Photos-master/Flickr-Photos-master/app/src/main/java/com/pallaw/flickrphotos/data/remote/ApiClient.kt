package com.pallaw.flickrphotos.data.remote

import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


const val API_KEY = "25b6ea7cf47427f19898a3e98b8bf1e3"
const val BASE_URL = "https://api.flickr.com"

const val FIRST_PAGE = 1
const val ITEM_PER_PAGE = 15

object ApiClient {

    fun getClient(): ApiService {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(getCommonRequestInterceptor())
            .addInterceptor(getLoggingInterceptor())
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    }

    private fun getCommonRequestInterceptor(): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val url = chain.request()
                    .url
                    .newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return chain.proceed(request)
            }
        }
    }

    private fun getLoggingInterceptor(): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request: Request = chain.request()
                Timber.tag("Okhttp_request").d(request.toString())

                val response = chain.proceed(request)
                val rawJson = response.body!!.string()

                Timber.tag("Okhttp_response").d(rawJson)

                return response.newBuilder()
                    .body(rawJson.toResponseBody(response.body!!.contentType())).build()
            }
        }
    }
}