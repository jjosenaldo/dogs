package com.example.dogs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogs.model.DogBreed
import com.example.dogs.model.DogDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {
    val dogBreed = MutableLiveData<DogBreed>()
    val loading = MutableLiveData<Boolean>()

    fun fetchDogFromDatabaseByUuid(uuid: Long) {
        launch {
            loading.value = true
            val maybeDogBreed = DogDatabase(getApplication()).dogDao().getDogById(uuid)

            if (maybeDogBreed != null) {
                dogBreed.value = maybeDogBreed
            }

            loading.value = false
        }
    }

}