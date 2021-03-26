package com.mtg.galleryapp.modules.main

import androidx.lifecycle.ViewModel
import com.mtg.galleryapp.Application
import com.mtg.galleryapp.R
import com.mtg.galleryapp.net.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class MainViewModel : ViewModel() {

    lateinit var listener: MainModelCallback
    internal var pageObtain: Int = 0
    private var hasMore = true
    private val networkApi = NetworkApi().getNetworkService()
    val images: MutableList<PicturesResponse> = mutableListOf()

    fun validateAuthorization() {
        val body = AuthorizationRequest("23567b218376f79d9415")
        networkApi.requestAuth(body).enqueue(object : Callback<AuthorizationResponse> {
            override fun onResponse(call: Call<AuthorizationResponse>, response: Response<AuthorizationResponse>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val result = response.body()!!
                    if (result.auth) {
                        Application.getPreferences().saveData(R.string.sp_token_auth, result.token)
                        listener.onAuthorizationObtained()
                    } else {
                        listener.onError(Application.getContext().getString(R.string.e_auth_failed))
                    }
                } else {
                    listener.onError(Application.getContext().getString(R.string.e_code_status) + " ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AuthorizationResponse>, t: Throwable) {
                listener.onError(t.message
                        ?: Application.getContext().getString(R.string.e_service_failed))
            }

        })
    }

    fun loadImages() {
        if (hasMore) {
            val tokenAuth = Application.getPreferences().loadData(R.string.sp_token_auth, "")
            if (pageObtain == 0) {
                networkApi.loadImages(tokenAuth).enqueue(object : Callback<ImagesResponse> {
                    override fun onResponse(call: Call<ImagesResponse>, response: Response<ImagesResponse>) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            val result = response.body()!!
                            hasMore = result.hasMore
                            pageObtain = result.page
                            images.addAll(result.pictures)
                            listener.onImagesObtained()
                        } else {
                            listener.onError(Application.getContext().getString(R.string.e_code_status) + " ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<ImagesResponse>, t: Throwable) {
                        listener.onError(t.message
                                ?: Application.getContext().getString(R.string.e_service_failed))
                    }

                })
            } else {
                networkApi.loadImages(tokenAuth, pageObtain + 1).enqueue(object : Callback<ImagesResponse> {
                    override fun onResponse(call: Call<ImagesResponse>, response: Response<ImagesResponse>) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            val result = response.body()!!
                            hasMore = result.hasMore
                            pageObtain = result.page
                            images.addAll(result.pictures)
                            listener.onImagesObtained()
                        } else {
                            listener.onError(Application.getContext().getString(R.string.e_code_status) + " ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<ImagesResponse>, t: Throwable) {
                        listener.onError(t.message
                                ?: Application.getContext().getString(R.string.e_service_failed))
                    }

                })
            }
        } else {
            listener.onError(Application.getContext().getString(R.string.e_no_more_images))
        }
    }

    interface MainModelCallback {
        fun onError(msg: String)
        fun onAuthorizationObtained()
        fun onImagesObtained()
    }
}