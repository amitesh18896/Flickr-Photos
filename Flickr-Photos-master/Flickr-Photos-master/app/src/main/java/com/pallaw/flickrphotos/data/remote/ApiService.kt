package com.pallaw.flickrphotos.data.remote

import com.pallaw.flickrphotos.data.model.RecentPhotoResponse
import com.pallaw.flickrphotos.data.model.SearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("content-type: application/json")
    @GET("/services/rest/?method=flickr.photos.getRecent&format=json&nojsoncallback=1&per_page=15")
    fun getRecentPhotos(@Query("page") page: Int): Single<RecentPhotoResponse>

    @Headers("content-type: application/json")
    @GET("/services/rest/?method=flickr.photos.search&format=json&nojsoncallback=1&per_page=15")
    fun searchTag(@Query("tags") tag: String, @Query("page") page: Int): Single<SearchResponse>
}