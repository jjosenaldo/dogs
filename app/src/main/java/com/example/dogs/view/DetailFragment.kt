package com.example.dogs.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.dogs.R
import com.example.dogs.databinding.FragmentDetailBinding
import com.example.dogs.util.getProgressDrawable
import com.example.dogs.util.loadImage
import com.example.dogs.viewmodel.DetailViewModel

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        observe()

        val args: DetailFragmentArgs by navArgs()
        viewModel.fetchDogFromDatabaseByUuid(args.dogUuid.toLong())

    }

    private fun observe() {
        viewModel.dogBreed.observe(viewLifecycleOwner, { dogBreed ->
            binding.dogLifespanView.text = dogBreed.lifeSpan ?: "NO LIFESPAN PROVIDED"
            binding.dogNameView.text = dogBreed.dogBreed ?: "NO DOG BREED PROVIDED"
            binding.dogPurposeView.text = dogBreed.bredFor ?: "NO PURPOSE PROVIDED"
            binding.dogTemperamentView.text = dogBreed.temperament ?: "NO TEMPERAMENT PROVIDED"
            context?.let {
                binding.dogImageView.loadImage(
                    dogBreed.imageUrl ?: "",
                    getProgressDrawable(context!!)
                )
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, {
            binding.dogNameView.text = if (!it) viewModel.dogBreed.value?.dogBreed
                ?: "NO DOG BREED PROVIDED" else "Loading..."
        })
    }
}