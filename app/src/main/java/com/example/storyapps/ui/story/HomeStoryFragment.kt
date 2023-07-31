package com.example.storyapps.ui.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapps.ViewModelFactory
import com.example.storyapps.databinding.FragmentHomeStoryBinding

class HomeStoryFragment : Fragment() {

    private var _binding: FragmentHomeStoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeStoryViewModel by viewModels {
        ViewModelFactory(context = requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        binding.rvStory.layoutManager = layoutManager

        setupView()
        setListStoryData()
    }

    private fun setupView() {
        val layoutManager = LinearLayoutManager(activity)
        binding?.rvStory?.layoutManager = layoutManager
    }

    private fun setListStoryData() {
        val adapter = StoryAdapter { story ->
            val toDetailFragment =
                HomeStoryFragmentDirections.actionHomeStoryFragmentToDetailStoryFragment()
            toDetailFragment.id = story.id
            findNavController().navigate(toDetailFragment)
        }
        binding?.rvStory?.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.story.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}