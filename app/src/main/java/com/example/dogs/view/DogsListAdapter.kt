package com.example.dogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.R
import com.example.dogs.databinding.ItemDogBinding
import com.example.dogs.model.DogBreed
import com.example.dogs.util.getProgressDrawable
import com.example.dogs.util.loadImage
import com.example.dogs.viewmodel.DetailViewModel

class DogsListAdapter(private val dogsList : ArrayList<DogBreed>) : RecyclerView.Adapter<DogsListAdapter.DogViewHolder>() , DogClickListener{
    class DogViewHolder(val binding : ItemDogBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateDogList(newDogsList:List<DogBreed>){
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =  ItemDogBinding.inflate(inflater, parent, false)
        return DogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.binding.dog = dogsList[position]
        holder.binding.listener = this
    }

    override fun getItemCount(): Int {
        return dogsList.size
    }

    override fun onDogClicked(view: View, dog : DogBreed) {
        val action = ListFragmentDirections.actionListFragmentToDetailFragment(dog.uuid)
        Navigation.findNavController(view).navigate(action)
    }

}