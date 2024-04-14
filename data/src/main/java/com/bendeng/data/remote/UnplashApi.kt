package com.bendeng.data.remote

import com.bendeng.data.model.response.SearchPhotoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnplashApi {

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val AUTHORIZATION = "Authorization"
        const val CLIENT_ID = "Client-ID"
    }

    @GET("search/photos")
    suspend fun getSearchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int? = 30
    ): Response<SearchPhotoResponse>
}