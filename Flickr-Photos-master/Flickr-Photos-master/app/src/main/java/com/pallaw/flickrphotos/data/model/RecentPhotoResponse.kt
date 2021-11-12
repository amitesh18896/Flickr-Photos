package com.pallaw.flickrphotos.data.model


import com.google.gson.annotations.SerializedName

data class RecentPhotoResponse(
    @SerializedName("photos")
    var photos: Photos = Photos(),
    @SerializedName("stat")
    var stat: String = ""
) {
    data class Photos(
        @SerializedName("page")
        var page: Int = 0,
        @SerializedName("pages")
        var pages: Int = 0,
        @SerializedName("perpage")
        var perpage: Int = 0,
        @SerializedName("photo")
        var photo: List<Photo> = listOf(),
        @SerializedName("total")
        var total: Int = 0
    )
}