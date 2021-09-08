package com.kedra.filedownloader.main.viewModel

import com.kedra.filedownloader.main.network.models.ItemsResponse
import com.kedra.filedownloader.main.network.source.ItemsRepository
import com.kedra.filedownloader.utils.BaseViewModel
import com.kedra.filedownloader.utils.LiveDataState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: ItemsRepository) : BaseViewModel() {

    private var liveDataState = LiveDataState<ItemsResponse>()
    private val disposable = CompositeDisposable()

    fun refreshHomeList(): LiveDataState<ItemsResponse> {

        publishLoading(liveDataState)

        disposable.add(
            repository.getListItems().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                    object : DisposableSingleObserver<ItemsResponse>() {
                        override fun onSuccess(response: ItemsResponse) {
                            publishResult(liveDataState, response)
                        }

                        override fun onError(error: Throwable) {
                            publishError(liveDataState, error)
                        }
                    }
                )
        )
        return liveDataState
    }

}