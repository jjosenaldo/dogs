package com.example.dogs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.dogs.model.DogBreed
import com.example.dogs.model.DogDatabase
import com.example.dogs.model.DogsApiService
import com.example.dogs.util.NotificationsHelper
import com.example.dogs.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers.newThread
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class ListViewModel(application: Application) : BaseViewModel(application) {
    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable()
    private val preferencesHelper = SharedPreferencesHelper(getApplication())
    private var refreshTimeSeconds: Long = DEFAULT_SECONDS * SECONDS_MULTIPLIER

    fun refresh() {
        checkCacheDuration()

        val updateTime = preferencesHelper.getUpdateTime()
        if (updateTime == 0L || System.nanoTime() - updateTime > refreshTimeSeconds) {
            fetchFromRemote()
        } else {
            fetchFromDatabase()
        }
    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    private fun checkCacheDuration() {
        try {
            refreshTimeSeconds = (preferencesHelper.getCacheDurationInSeconds()?.toInt()
                ?: DEFAULT_SECONDS) * SECONDS_MULTIPLIER
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }


    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val dogsList = DogDatabase(getApplication()).dogDao().getAllDogs()
            onDogsRetrieved(dogsList)
            Toast.makeText(getApplication(), "Fetch from database", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(dogsList: List<DogBreed>) {
                        Toast.makeText(getApplication(), "Fetch from network", Toast.LENGTH_SHORT)
                            .show()
                        NotificationsHelper(getApplication()).createNotification()
                        storeDogsLocally(dogsList)
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        dogsLoadError.value = true
                    }
                })
        )

    }

    private fun storeDogsLocally(dogsList: List<DogBreed>) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val dogsIds = dao.insertAllDogs(*dogsList.toTypedArray())
            dogsList.forEachIndexed { index, dog ->
                dog.uuid = dogsIds[index].toInt()
            }
            onDogsRetrieved(dogsList)
        }
        preferencesHelper.saveUpdateTime(System.nanoTime())
    }

    private fun onDogsRetrieved(dogsList: List<DogBreed>) {
        dogs.value = dogsList
        loading.value = false
        dogsLoadError.value = false
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    companion object {
        private const val SECONDS_MULTIPLIER = 1000 * 1000 * 1000L
        private const val DEFAULT_SECONDS = 30
    }
}