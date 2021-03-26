package com.mtg.galleryapp.net

import com.squareup.picasso.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

class NetworkApi {

    companion object {
        private const val SERVICE_URL = "http://interview.agileengine.com/"
    }

    fun getNetworkService(): ApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val customClient = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
        /* Enable interceptor only in debug mode */
        if (BuildConfig.DEBUG) {
            customClient.addInterceptor(interceptor)
        }
        val clientBuilder = customClient.build()
        val builder = Retrofit.Builder().baseUrl(SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder).build()
        return builder.create(ApiService::class.java)
    }

    interface ApiService {
        @POST("auth/")
        fun requestAuth(@Body body: AuthorizationRequest): Call<AuthorizationResponse>

        @GET("images")
        fun loadImages(@Header("Authorization") tokenAuth: String): Call<ImagesResponse>

        @GET("images")
        fun loadImages(@Header("Authorization") tokenAuth: String, @Query("page") page: Int): Call<ImagesResponse>

        @GET("images/{idImage}")
        fun loadImageDetail(@Header("Authorization") tokenAuth: String, @Path(value = "idImage", encoded = true) idImage: String): Call<ImageDetailResponse>
    }
}