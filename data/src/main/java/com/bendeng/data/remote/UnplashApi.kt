package com.bendeng.data.remote

import com.bendeng.data.model.response.LikePhotoResponse
import com.bendeng.data.model.response.PhotoInfo
import com.bendeng.data.model.response.SearchPhotoResponse
import com.bendeng.data.model.response.UnLikePhotoResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UnplashApi {

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer"
        const val GYROH_ID = "gyroh"
    }

    @GET("search/photos")
    suspend fun getSearchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int?
    ): Response<SearchPhotoResponse>

    @POST("photos/{id}/like")
    suspend fun postLikePhoto(
        @Path("id") id: String
    ): Response<LikePhotoResponse>

    @DELETE("photos/{id}/like")
    suspend fun deleteUnlikePhoto(
        @Path("id") id: String
    ): Response<UnLikePhotoResponse>

    @GET("users/{id}/likes")
    suspend fun getLikePhotos(
        @Path("id") id : String = GYROH_ID,
        @Query("page") page : Int,
        @Query("per_page") perPage : Int
    ) : Response<List<PhotoInfo>>
}