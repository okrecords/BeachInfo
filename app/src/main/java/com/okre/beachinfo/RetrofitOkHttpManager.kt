package com.okre.beachinfo

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitOkHttpManager {
    private val TARGET_URL = """https://www.tournmaster.com/seantour_map/travel/"""

    private var okHttpClient: OkHttpClient

    private val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(TARGET_URL)

    val beachService = retrofitBuilder.build().create(BeachService::class.java)

    init {
        okHttpClient  = OkHttpClient.Builder()
            .addInterceptor(RetryInterceptor())
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        // OkHttp Retrofit 연동
        retrofitBuilder.client(okHttpClient)
    }

    // 연결 실패 시 다시 시도
    private class RetryInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request : Request = chain.request()
            var response : Response = chain.proceed(request)
            var tryCount = 0
            val maxLimit = 2
            while (!response.isSuccessful && tryCount < maxLimit) {
                Log.d("beachLog", "요청실패 - $tryCount")
                tryCount++
                response = chain.proceed(request)
            }
            return response
        }
    }
}