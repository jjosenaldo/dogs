package com.example.dogs.view

import android.view.View
import com.example.dogs.model.DogBreed

interface DogClickListener {
    fun onDogClicked(view: View, dog: DogBreed)
}