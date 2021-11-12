package com.pallaw.flickrphotos.data.repository

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.pallaw.flickrphotos.data.model.Photo
import com.pallaw.flickrphotos.data.remote.ApiService
import com.pallaw.flickrphotos.data.remote.ITEM_PER_PAGE
import com.pallaw.flickrphotos.util.NetworkState
import io.reactivex.disposables.CompositeDisposable

class PhotoPagedListRepository(private val apiService: ApiService) {

    lateinit var photoPagedList: LiveData<PagedList<Photo>>
    lateinit var photosDataSourceFactory: PhotoDataSourceFactory

    fun fetchLiveSearchPhotoPagedList(
        searchTagLive: MutableLiveData<String>,
        compositeDisposable: CompositeDisposable
    ): LiveData<PagedList<Photo>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(ITEM_PER_PAGE)
            .build()


        photoPagedList = Transformations.switchMap<String, PagedList<Photo>>(
            searchTagLive
        ) { searchInput: String ->

            photosDataSourceFactory =
                PhotoDataSourceFactory(
                    searchInput,
                    apiService,
                    compositeDisposable
                )

            return@switchMap LivePagedListBuilder(photosDataSourceFactory!!, config).build()
        }
        return photoPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap(photoPagedList, Function {
            photosDataSourceFactory.photosLiveDataSource.value?.networkState
        })
    }
}