package com.kedra.filedownloader.main.viewModel

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kedra.filedownloader.main.network.models.ItemsResponseItem
import com.kedra.filedownloader.main.network.source.ItemsRepository
import com.kedra.filedownloader.utils.BaseViewModel
import com.kedra.filedownloader.utils.LiveDataState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.net.URL
import javax.inject.Inject


class MainViewModel @Inject constructor(private val repository: ItemsRepository) : BaseViewModel() {

    private var liveDataState = LiveDataState<List<ItemsResponseItem>>()
    private val disposable = CompositeDisposable()

    val percentData = MutableLiveData<Int>()

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

    fun downloadFile(context: Context, url: String): MutableLiveData<Int> {

        val call: Call<ResponseBody?>? = repository.getFileDownloaded(url)
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
                    response.body()?.let {

                        val writtenToDisk = writeResponseBodyToDisk(context, it)
                        Log.d("data result", writtenToDisk.toString())
                        percentData.postValue(writtenToDisk)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                percentData.postValue(0)
            }
        })
        return percentData
    }

    private fun writeResponseBodyToDisk(context: Context, body: ResponseBody): Int {
        return try {
            var percent = 0
            val futureStudioIconFile =
                File(context.filesDir, "FilesDownload")
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Log.d("File Download: ", "$fileSizeDownloaded of $fileSize")
                    percent = ((fileSizeDownloaded * 100L) / fileSize).toInt()
                }
                outputStream.flush()
                percent
            } catch (e: IOException) {
                percent
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            500
        }
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

    private fun updateDisplay(
        downloadManager: DownloadManager,
        downloadId: Long,
        onUpdateUi: ((item: ItemsResponseItem, position: Int) -> Unit)
    ) {
        var isDownloading = true
        var downloadStatus: Int
        var totalBytesDownloaded: Int
        var totalBytes: Int
        while (isDownloading) {
            val downloadQuery = DownloadManager.Query()
            downloadQuery.setFilterById(downloadId)
            val cursor: Cursor = downloadManager.query(downloadQuery)
            cursor.moveToFirst()
            totalBytesDownloaded = cursor.getInt(
                cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
            )
            totalBytes = cursor.getInt(
                cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
            )

            val downloadProgress =
                (totalBytesDownloaded.toDouble() / totalBytes.toDouble() * 100f).toInt()

            downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
                isDownloading = false
                break
            } else if (downloadStatus == DownloadManager.STATUS_FAILED) {
                isDownloading = true
                break
            }
        }
    }

    private fun startDownload(downloadManager: DownloadManager, downloadId: Long) {

        val updateHandler = Handler()

        val runnable = Runnable {
            var isDownloading = true
            var downloadStatus: Int
            var totalBytesDownloaded: Int
            var totalBytes: Int
            while (isDownloading) {
                val downloadQuery = DownloadManager.Query()
                downloadQuery.setFilterById(downloadId)
                val cursor: Cursor = downloadManager.query(downloadQuery)
                cursor.moveToFirst()
                totalBytesDownloaded = cursor.getInt(
                    cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                )
                totalBytes = cursor.getInt(
                    cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                )

                val downloadProgress =
                    (totalBytesDownloaded.toDouble() / totalBytes.toDouble() * 100f).toInt()
                run {

                }

                downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
                    isDownloading = false
                    break
                } else if (downloadStatus == DownloadManager.STATUS_FAILED) {
                    isDownloading = true
                    break
                }
            }
        }

        updateHandler.postDelayed(runnable, 500)

    }
}