package com.example.dogs.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogs.databinding.FragmentListBinding
import com.example.dogs.model.DogBreed
import com.example.dogs.viewmodel.DetailViewModel
import com.example.dogs.viewmodel.ListViewModel

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ListViewModel
    private val dogsAdapter: DogsListAdapter =
        DogsListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        binding.swiper.setOnRefreshListener {
            viewModel.dogs.value = arrayListOf()
            viewModel.dogsLoadError.value = false
            viewModel.loading.value = true
            binding.swiper.isRefreshing = false
            viewModel.refreshBypassCache()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsAdapter
        }
        observeViewModel()
        viewModel.refresh()
    }

    private fun observeViewModel() {
        viewModel.dogs.observe(viewLifecycleOwner, {
            it?.let {
                binding.recyclerView.visibility = View.VISIBLE
                dogsAdapter.updateDogList(it)
            }
        })

        viewModel.dogsLoadError.observe(viewLifecycleOwner, {
            it?.let {
                binding.errorText.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, {
            it?.let {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
    }
}