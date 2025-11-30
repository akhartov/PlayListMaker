package com.practicum.playlistmaker.playlist.ui.editor

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistEditorBinding
import com.practicum.playlistmaker.ui.BindingFragment
import com.practicum.playlistmaker.ui.floatDpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel

open class PlaylistCoverFragment : BindingFragment<FragmentPlaylistEditorBinding>() {
    open val viewModel: PlaylistMakerViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistEditorBinding {
        return FragmentPlaylistEditorBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                viewModel.imageUri = uri
                applyImage(uri)
            }

        binding.coverImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.newPlaylistButton.isEnabled = false
        binding.playlistTitle.addTextChangedListener { text ->
            viewModel.playlistName = text.toString()
            binding.newPlaylistButton.isEnabled = viewModel.canSavePlaylist()
            binding.playlistTitleHint.isVisible = !text.isNullOrBlank()
        }

        binding.playlistDescription.addTextChangedListener { text ->
            viewModel.playlistDescription = text.toString()
            binding.playlistDescriptionHint.isVisible = !text.isNullOrBlank()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(URI, viewModel.imageUri)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null)
            return

        applyImage(savedInstanceState.getParcelable(URI, Uri::class.java))
    }

    private fun applyImage(uri: Uri?) {
        uri?.let { existingUri ->
            Glide.with(requireContext())
                .load(existingUri)
                .transform(
                    CenterCrop(),
                    RoundedCorners(
                        floatDpToPx(resources, resources.getDimension(R.dimen.big_image_radius))
                    )
                )
                .into(binding.coverImage)
        }
    }

    companion object {
        private const val URI = "URI"
    }
}