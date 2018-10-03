package com.zest.android.data.source.remote

import android.text.TextUtils
import android.util.Log
import com.google.gson.GsonBuilder
import com.zest.android.BuildConfig
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Helper class for generate Services
 * As main responsibilities of this class is handle Authorization header and token in header of
 * request
 *
 *
 * Notice: 08/10/18 check this out for whole use cases in application with or without user auth token
 *
 * @Author ZARA.
 */
object ServiceGenerator {

    private val TAG = ServiceGenerator::class.java.simpleName
    private val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    private val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeNulls()
            .setDateFormat(DATE_FORMAT)
            .create()

    private val mRetrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.BASE_URL)


    fun <S> createService(serviceClass: Class<S>): S {
        return createService<S>(serviceClass, null)
    }


    /**
     * To generate api services with token to set in Authorization header
     *
     * @param serviceClass
     * @param token
     * @param <S>
     * @return
    </S> */
    fun <S> createService(serviceClass: Class<S>, token: String?): S {
        return createService(serviceClass, token, 10)
    }


    fun <S> createService(serviceClass: Class<S>, networkTimeOut: Int): S {
        return createService(serviceClass, null, networkTimeOut)
    }

    /**
     * To generate api services with token to set in Authorization header
     *
     * @param serviceClass
     * @param token
     * @param timeout
     * @param <S>
     * @return
    </S> */
    fun <S> createService(serviceClass: Class<S>, token: String?, timeout: Int): S {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(AuthorizationTokenInterceptor(token))
        httpClientBuilder.connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
        httpClientBuilder.readTimeout(timeout.toLong(), TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(timeout.toLong(), TimeUnit.SECONDS)
        return mRetrofitBuilder.client(httpClientBuilder.build()).build().create(serviceClass)
    }


    /**
     * To prevent show logs in Debug mode !
     *
     * @param format
     */
    private fun logger(format: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, format)
        }
    }

    /**
     * Add [Interceptor]
     */
    private class AuthorizationTokenInterceptor
    /**
     * notice : all the customized params show have been used in this class with
     * some conditions, i.e : toke, language, etc.
     *
     * @param token
     */
    internal constructor(private val token: String?) : Interceptor {

        private val mHeaderBuilder: Headers.Builder

        init {
            mHeaderBuilder = Headers.Builder()
                    .add("Content-Type", "application/json")
                    .add("Accept-Language", "fa")


            if (token != null && !TextUtils.isEmpty(token)) {
                mHeaderBuilder.add("Authorization", token)
            }
        }

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val t1 = System.nanoTime()
            val original = chain.request()
            // Request customization: add request headers
            // show some extra info for each of requests
            logger(String.format(Locale.ENGLISH,
                    "Sending request %s within headers%n %s",
                    original.url(), original.headers()))
            val requestBuilder = original.newBuilder()
                    .headers(mHeaderBuilder.build())
                    .method(original.method(), original.body())
            val response = chain.proceed(requestBuilder.build())
            val t2 = System.nanoTime()
            // To make calculation time distance between request and response
            logger(String.format(
                    Locale.ENGLISH,
                    "Received Response for %s in %.1fms%n %s ",
                    response.request().url(), (t2 - t1) / 1e6,
                    response.headers()))

            return response
        }
    }
}
