package com.kedra.filedownloader.main.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kedra.filedownloader.main.data.models.AllItemsResponse
import com.kedra.filedownloader.main.data.network.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val moviesResult = MutableLiveData<AllItemsResponse>()
    private val isLoading = MutableLiveData<Boolean>()
    private val messageError = MutableLiveData<String>()

    fun getMoviesResult(): MutableLiveData<AllItemsResponse> {
        observeData()
        return moviesResult
    }

    private fun observeData() {
        isLoading.value = true
        disposable.add(
            repository.getListItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AllItemsResponse>() {
                    override fun onSuccess(response: AllItemsResponse) {
                        moviesResult.value = response
                        isLoading.value = false
                    }

                    override fun onError(error: Throwable) {
                        messageError.value = error.message
                        isLoading.value = false
                    }
                })
        )
    }

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading
    fun getMessageError(): MutableLiveData<String> = messageError
}