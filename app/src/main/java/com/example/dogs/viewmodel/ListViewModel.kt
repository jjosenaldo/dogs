package com.example.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogs.model.DogBreed

class ListViewModel : ViewModel() {
    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh(): Unit {
        dogs.value = arrayListOf<DogBreed>(
            DogBreed(
                "1",
                "viralata caramelo",
                "50 anos",
                "breedGroup",
                "",
                "",
                ""
            ), DogBreed("2", "dalmata", "50 anos", "breedGroup", "", "", "")
        )
        dogsLoadError.value = false
        loading.value = false
    }
}