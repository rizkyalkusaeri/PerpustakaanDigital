package com.example.perpustakaandigital.network

import android.content.Context
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class NetworkProvider {

    companion object {
        private var retrofit: Retrofit? = null
        fun getClient(context: Context): Retrofit? {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val clientTest = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build()

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(clientTest)
                        .build()
            }
            return retrofit
        }
    }
}