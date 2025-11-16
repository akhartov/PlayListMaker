package com.practicum.playlistmaker.playlist.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistEditorBinding
import com.practicum.playlistmaker.ui.BindingFragment
import com.practicum.playlistmaker.ui.floatDpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditorFragment : BindingFragment<FragmentPlaylistEditorBinding>() {
    private val viewModel: PlaylistEditorViewModel by viewModel()
    private lateinit var backCallback: OnBackPressedCallback
    lateinit var confirmDialog: MaterialAlertDialogBuilder


    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistEditorBinding {
        return FragmentPlaylistEditorBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistButton.setOnClickListener {
            if (!binding.playlistTitle.text.isNullOrEmpty()) {
                viewModel.savePlaylist()
            }

            findNavController().navigateUp()
        }

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
            binding.newPlaylistButton.isEnabled = !text.isNullOrBlank()
            binding.playlistTitleHint.isVisible = !text.isNullOrBlank()
        }

        binding.playlistDescription.addTextChangedListener { text ->
            viewModel.playlistDescription = text.toString()
            binding.playlistDescriptionHint.isVisible = !text.isNullOrBlank()
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.ask_finish_playlist_creation))
            .setMessage(resources.getString(R.string.all_data_will_lost))
            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
            }.setNegativeButton(resources.getString(R.string.finish)) { dialog, which ->
                findNavController().navigateUp()
            }

        backCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                confirmDialog.show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(backCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backCallback.remove()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(URI, viewModel.imageUri)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState == null)
            return

        applyImage(savedInstanceState.getParcelable(URI, Uri::class.java))
    }

    private fun applyImage(uri: Uri?) {
        uri?.let { existingUri ->
            Glide.with(requireContext())
                .load(existingUri)
                .placeholder(R.drawable.track_placeholder)
                .circleCrop()
                .transform(
                    RoundedCorners(
                        floatDpToPx(
                            resources,
                            resources.getDimension(R.dimen.big_image_radius)
                        )
                    )
                )
                .into(binding.coverImage)
        }
    }

    companion object {
        private const val URI = "URI"
    }
}