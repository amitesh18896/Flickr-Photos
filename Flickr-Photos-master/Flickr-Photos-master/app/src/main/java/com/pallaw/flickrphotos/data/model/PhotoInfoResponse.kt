package com.pallaw.flickrphotos.data.model


import com.google.gson.annotations.SerializedName

data class PhotoInfoResponse(
    @SerializedName("photo")
    var photo: Photo = Photo(),
    @SerializedName("stat")
    var stat: String = ""
)