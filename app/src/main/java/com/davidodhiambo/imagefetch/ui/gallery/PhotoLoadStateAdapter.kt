package com.davidodhiambo.imagefetch.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davidodhiambo.imagefetch.databinding.PhotoLoadStateFooterBinding

class PhotoLoadStateAdapter(private val retry: () -> Unit):
    LoadStateAdapter<PhotoLoadStateAdapter.PhotoLoadStateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PhotoLoadStateViewHolder {
        val binding = PhotoLoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoLoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class PhotoLoadStateViewHolder(private val binding: PhotoLoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.buttonRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState is LoadState.Error
                textViewError.isVisible = loadState is LoadState.Error
                textViewError.text = (loadState as? LoadState.Error)?.error?.message
            }

        }
    }
}