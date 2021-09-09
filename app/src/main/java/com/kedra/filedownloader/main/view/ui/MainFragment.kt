package com.kedra.filedownloader.main.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.kedra.filedownloader.databinding.MainFragmentBinding
import com.kedra.filedownloader.main.di.Injectable
import com.kedra.filedownloader.main.view.adapter.MainAdapter
import com.kedra.filedownloader.main.viewModel.MainViewModel
import com.kedra.filedownloader.utils.DataState
import javax.inject.Inject

class MainFragment : Fragment(), Injectable {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var adapter: MainAdapter

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
        viewModel.refreshHomeList().observe(viewLifecycleOwner, {
            when (it.getStatus()) {

                DataState.DataStatus.LOADING -> {
                    showLayoutLoadingOrError(isLoading = true)
                }

                DataState.DataStatus.SUCCESS -> {
                    showLayoutLoadingOrError(isLoading = false)
                    it.getData()?.let { list ->
                        if (!list.isNullOrEmpty()) {
                            adapter.addList(list)
                        } else {
                            showLayoutLoadingOrError(
                                isLoading = false,
                                hasError = true,
                                error = "Empty List"
                            )
                        }
                    }?: kotlin.run {
                        showLayoutLoadingOrError(
                            isLoading = false,
                            hasError = true,
                            error = "Empty List"
                        )
                    }
                }

                DataState.DataStatus.ERROR -> {
                    showLayoutLoadingOrError(isLoading = false, hasError = true)
                }

                DataState.DataStatus.NO_INTERNET -> {

                    showLayoutLoadingOrError(
                        isLoading = false,
                        hasError = true,
                        error = "No Internet Connection"
                    )
                }

            }
        })
        initUi()
    }

    private fun initUi() {
        with(binding) {
            adapter = MainAdapter()
            rvList.adapter = adapter

        }
    }

    private fun showLayoutLoadingOrError(
        isLoading: Boolean,
        hasError: Boolean = false,
        error: String = "Error in api"
    ) {
        with(binding) {
            progressbar.isVisible = isLoading
            tvError.isVisible = hasError
            tvError.text = error
            rvList.isVisible = !(isLoading || hasError)
        }
    }
}