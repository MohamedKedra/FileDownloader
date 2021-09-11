package com.kedra.filedownloader.main.viewModel

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.kedra.filedownloader.main.network.models.ItemsResponseItem
import com.kedra.filedownloader.main.network.source.ItemsRepository
import com.kedra.filedownloader.utils.BaseViewModel
import com.kedra.filedownloader.utils.LiveDataState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.net.URL
import javax.inject.Inject


class MainViewModel @Inject constructor(private val repository: ItemsRepository) : BaseViewModel() {

    private var liveDataState = LiveDataState<List<ItemsResponseItem>>()
    private val disposable = CompositeDisposable()

    fun refreshHomeList(): LiveDataState<List<ItemsResponseItem>> {

        publishLoading(liveDataState)

        disposable.add(
            repository.getListItems().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                    object : DisposableSingleObserver<List<ItemsResponseItem>>() {
                        override fun onSuccess(response: List<ItemsResponseItem>) {
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


    fun downloadFile(
        context: Context,
        type: String,
        url: URL,
        filePath: String
    ): MutableLiveData<Boolean> {

        val data = MutableLiveData<Boolean>()

        val request = DownloadManager.Request(Uri.parse(url.toString()))
        request.setTitle(filePath)
        request.setMimeType(type)
        request.allowScanningByMediaScanner()
        request.setAllowedOverMetered(true)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filePath)

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = dm.enqueue(request)

        val br = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (downloadId == intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)) {
                    data.postValue(true)
                } else {
                    data.postValue(false)
                }
            }
        }
        context.registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        return data
    }
}