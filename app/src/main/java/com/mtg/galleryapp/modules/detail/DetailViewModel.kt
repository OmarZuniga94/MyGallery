package com.mtg.galleryapp.modules.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mtg.galleryapp.Application
import com.mtg.galleryapp.R
import com.mtg.galleryapp.net.ImageDetailResponse
import com.mtg.galleryapp.net.NetworkApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class DetailViewModel : ViewModel() {

    internal lateinit var listener: DetailModelCallback
    private var detail = MutableLiveData<ImageDetailResponse>()
    private val networkApi = NetworkApi().getNetworkService()
    val detailData: LiveData<ImageDetailResponse>
        get() = detail

    init {
        detail.value = ImageDetailResponse("", "", "", "", "", "")
    }

    fun loadDetail(idPicture: String) {
        val token = Application.getPreferences().loadData(R.string.sp_token_auth, "")
        networkApi.loadImageDetail(token, idPicture).enqueue(object : Callback<ImageDetailResponse> {
            override fun onResponse(call: Call<ImageDetailResponse>, response: Response<ImageDetailResponse>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val result = response.body()!!
                    detail.value = result
                    listener.onDetailObtained()
                } else {
                    listener.onError(Application.getContext().getString(R.string.e_code_status) + " ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ImageDetailResponse>, t: Throwable) {
                listener.onError(t.message
                        ?: Application.getContext().getString(R.string.e_service_failed))
            }
        })
    }

    interface DetailModelCallback {
        fun onError(msg: String)
        fun onDetailObtained()
    }
}