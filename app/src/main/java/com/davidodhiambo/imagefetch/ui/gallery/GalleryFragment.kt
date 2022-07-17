package com.davidodhiambo.imagefetch.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import com.davidodhiambo.imagefetch.R
import com.davidodhiambo.imagefetch.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment: androidx.fragment.app.Fragment(R.layout.fragment_gallery) {
    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding : FragmentGalleryBinding? = null

    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        val adapter = PhotoAdapter()
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = PhotoLoadStateAdapter {
                    adapter.retry()
                },
                footer = PhotoLoadStateAdapter {
                    adapter.retry()
                }
            )
        }

        viewModel.photos.observe(viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle, it)

        }

        //setHasOptionsMenu(true)
    }
    @Deprecated("onCreateOptionsMenu() is deprecated")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_gallery, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}