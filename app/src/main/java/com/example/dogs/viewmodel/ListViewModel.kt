package com.example.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogs.model.DogBreed
import com.example.dogs.model.DogsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.newThread

class ListViewModel : ViewModel() {
    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable()

    fun refresh() {
        fetchFromRemote()
    }

    private fun fetchFromRemote(){
        loading.value = true
        disposable.add(
        dogsService.getDogs()
            .subscribeOn(newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<List<DogBreed>>() {
                override fun onSuccess(dogsList: List<DogBreed>) {
                    dogs.value = dogsList
                    loading.value = false
                    dogsLoadError.value = false
                }

                override fun onError(e: Throwable) {
                    loading.value = false
                    dogsLoadError.value = true
                }

            }))

    }
}