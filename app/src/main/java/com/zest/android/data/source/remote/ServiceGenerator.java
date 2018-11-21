package com.zest.android.data.source.remote;


import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zest.android.BuildConfig;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Helper class for generate Services
 * As main responsibilities of this class is handle Authorization header and token in header of
 * request
 * <p>
 * Notice: 08/10/18 check this out for whole use cases in application with or without user auth token
 *
 * @Author ZARA.
 */
public final class ServiceGenerator {

    private static final String TAG = ServiceGenerator.class.getSimpleName();
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeNulls()
            .setDateFormat(DATE_FORMAT)
            .create();

    private static Retrofit.Builder mRetrofitBuilder = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.BASE_URL);


    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }


    /**
     * To generate api services with token to set in Authorization header
     *
     * @param serviceClass
     * @param token
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass, final String token) {
        return createService(serviceClass, token, 10);
    }


    public static <S> S createService(Class<S> serviceClass, final int networkTimeOut) {
        return createService(serviceClass, null, networkTimeOut);
    }

    /**
     * To generate api services with token to set in Authorization header
     *
     * @param serviceClass
     * @param token
     * @param timeout
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass, final String token, int timeout) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(new AuthorizationTokenInterceptor(token));
        httpClientBuilder.connectTimeout(timeout, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(timeout, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(timeout, TimeUnit.SECONDS);
        return mRetrofitBuilder.client(httpClientBuilder.build()).build().create(serviceClass);
    }


    /**
     * To prevent show logs in Debug mode !
     *
     * @param format
     */
    private static void logger(String format) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, format);
        }
    }

    /**
     * Add {@link Interceptor}
     */
    private static class AuthorizationTokenInterceptor implements Interceptor {
        private final String token;
        private final Headers.Builder mHeaderBuilder;

        /**
         * notice : all the customized params show have been used in this class with
         * some conditions, i.e : toke, language, etc.
         *
         * @param token
         */
        AuthorizationTokenInterceptor(String token) {
            this.token = token;
            mHeaderBuilder = new Headers.Builder()
                    .add("Content-Type", "application/json")
                    .add("Accept-Language", "fa");


            if (token != null && !TextUtils.isEmpty(token)) {
                mHeaderBuilder.add("Authorization", token);
            }
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            final long t1 = System.nanoTime();
            Request original = chain.request();
            // Request customization: add request headers
            // show some extra info for each of requests
            logger(String.format(Locale.ENGLISH,
                    "Sending request %s within headers%n %s",
                    original.url(), original.headers()));
            Request.Builder requestBuilder = original.newBuilder()
                    .headers(mHeaderBuilder.build())
                    .method(original.method(), original.body());
            final Response response = chain.proceed(requestBuilder.build());
            final long t2 = System.nanoTime();
            // To make calculation time distance between request and response
            logger(String.format(
                    Locale.ENGLISH,
                    "Received Response for %s in %.1fms%n %s ",
                    response.request().url(), (t2 - t1) / 1e6d,
                    response.headers()));

            return response;
        }
    }


}
