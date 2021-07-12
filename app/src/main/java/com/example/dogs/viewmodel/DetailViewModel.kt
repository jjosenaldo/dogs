package com.example.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogs.model.DogBreed

class DetailViewModel : ViewModel(){
    val dogBreed = MutableLiveData<DogBreed>()

}