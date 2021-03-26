package com.mtg.galleryapp.net

import com.google.gson.annotations.SerializedName

data class AuthorizationRequest(@SerializedName("apiKey") val apiKey: String)