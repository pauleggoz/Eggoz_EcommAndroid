package com.eggoz.ecommerce.network.repository

import android.content.Context
import android.util.Log
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.utils.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {


    private val BASE_URL = Constants.AppUrl
    private var userPreferences: UserPreferences? = null
    private lateinit var context: Context
    private lateinit var token: String

    private val client = OkHttpClient().newBuilder()
        .connectTimeout(0, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
    val retrofitApiSerwithoutInterceptor: RetrofitApiService by lazy {
        retrofit.create(RetrofitApiService::class.java)
    }

    private val clientwithInterceptor = OkHttpClient.Builder().apply {
        addInterceptor(Interceptor { chain ->
            val req = chain.request()
                .newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer $token"
                )
                .build()
            Log.d("TAG", "$token ")
            return@Interceptor chain.proceed(req)
        })
    }.connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val retrofitApiSer: RetrofitApiService by lazy {

        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                clientwithInterceptor
            )
            .build().create(RetrofitApiService::class.java)
    }

    fun retrofitApiSerInterceptor(context: Context): RetrofitApiService {
        this.context = context
        userPreferences = UserPreferences(context = context)
        token = runBlocking {
            userPreferences!!.authtoken.first() ?: ""
        }
        Log.d("data","token $token \n $retrofitApiSer")
        return retrofitApiSer
    }

    fun retrofitApiSerInterceptor(token: String): RetrofitApiService {
        this.token = token
        return retrofitApiSer
    }
}