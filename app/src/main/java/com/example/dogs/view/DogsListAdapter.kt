package com.example.dogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.R
import com.example.dogs.databinding.ItemDogBinding
import com.example.dogs.model.DogBreed

class DogsListAdapter(private val dogsList : ArrayList<DogBreed>) : RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(){
    class DogViewHolder(val binding : ItemDogBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateDogList(newDogsList:List<DogBreed>){
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =  ItemDogBinding.inflate(inflater);
        return DogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.binding.itemDogLifespan.text = dogsList[position].dogBreed
        holder.binding.itemDogLifespan.text = dogsList[position].lifeSpan
    }

    override fun getItemCount(): Int {
        return dogsList.size
    }

}