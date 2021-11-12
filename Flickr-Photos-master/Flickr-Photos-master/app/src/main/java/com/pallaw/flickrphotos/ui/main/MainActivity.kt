package com.pallaw.flickrphotos.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.pallaw.flickrphotos.R
import com.pallaw.flickrphotos.data.remote.ApiClient
import com.pallaw.flickrphotos.data.remote.ApiService
import com.pallaw.flickrphotos.data.repository.PhotoPagedListRepository
import com.pallaw.flickrphotos.util.NetworkState
import com.pallaw.flickrphotos.util.SearchObservable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.view.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    val photoRepository: PhotoPagedListRepository by lazy {
        PhotoPagedListRepository(
            apiService
        )
    }
    val apiService: ApiService by lazy { ApiClient.getClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = getViewModel()

        //Observe search text changes to switch between search list and recent photo list
        observeSearchText()

        //setup list to show  items from search API
        setupSearchPhotosList()

    }

    private fun setupSearchPhotosList() {

        val photosAdapter = SearchPhotosPagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 2)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = photosAdapter.getItemViewType(position)
                if (viewType == photosAdapter.PHOTO_VIEW_TYPE) return 1 else return 2
            }
        }

        searchList.rv_photo_list.layoutManager = gridLayoutManager
        searchList.rv_photo_list.setHasFixedSize(true)
        searchList.rv_photo_list.adapter = photosAdapter

        viewModel.searchPagedList.observe(this, Observer {
            photosAdapter.submitList(it)
        })

        viewModel.searchPhotosNetworkState?.observe(this, Observer {
            searchList.progress_bar.visibility =
                if (viewModel.isSearchListEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            searchList.txt_error.visibility =
                if (viewModel.isSearchListEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.isSearchListEmpty()) {
                photosAdapter.setNetworkState(it)
            }
        })
    }

    private fun observeSearchText() {
        SearchObservable.fromView(searchView)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .distinctUntilChanged()
            .subscribe(object : DisposableObserver<String>() {
                override fun onComplete() {
                }
                override fun onNext(t: String) {
                    viewModel.searchTag(t)
                }

                override fun onError(e: Throwable) {
                }

            })

        //make initial call with empty search tag
        searchView.setText("")
    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(photoRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }

}
