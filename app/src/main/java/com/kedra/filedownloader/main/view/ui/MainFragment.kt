package com.kedra.filedownloader.main.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.kedra.filedownloader.databinding.MainFragmentBinding
import com.kedra.filedownloader.main.view.adapter.MainAdapter
import com.kedra.filedownloader.main.viewModel.MainViewModel
import javax.inject.Inject

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }
//
//    private val viewModel: MainViewModel by viewModels {
//        viewModelFactory
//    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        with(binding) {
            rvList.adapter = MainAdapter()

        }
    }

    fun showLayoutLoadingOrError(
        isLoading: Boolean = true,
        hasError: Boolean = false,
        error: String
    ) {

        with(binding) {
            progressbar.isVisible = isLoading
            tvError.isVisible = hasError
            tvError.text = error
        }
    }
}