package com.rwa.submissionakhirdicodingevent.ui.favorite

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rwa.submissionakhirdicodingevent.data.Result
import com.rwa.submissionakhirdicodingevent.databinding.FragmentFavoriteBinding
import com.rwa.submissionakhirdicodingevent.ui.ViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as AppCompatActivity).supportActionBar?.title = "Favorite"

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: FavoriteViewModel by viewModels {
            factory
        }

        val adapter = FavoriteEventAdapter { event ->
            if (event.isFav) {
                viewModel.deleteFavEvent(event)
            } else {
                viewModel.favEvent(event)
            }
        }
        binding.rvEvent.adapter = adapter

        val layoutManager = LinearLayoutManager(this.context)
        binding.rvEvent.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this.context, layoutManager.orientation)
        binding.rvEvent.addItemDecoration(itemDecoration)

        viewModel.getFavEvent().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        showLoading(false)
                        val event = result.data
                        if (event.isEmpty()) {
                            binding.emptyFav.text = "Your Favorite is empty!"
                            binding.emptyFav.visibility = View.VISIBLE
                        } else {
                            binding.emptyFav.visibility = View.GONE
                        }
                        adapter.submitList(event)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(
                            context,
                            "Something went wrong: " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        val textView: TextView = binding.subtextFinish
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                binding.rvEvent.layoutManager = LinearLayoutManager(this.context)
            }

            Configuration.ORIENTATION_LANDSCAPE -> {
                binding.rvEvent.layoutManager = GridLayoutManager(this.context, 2)
            }

            else -> {
                Toast.makeText(this.context, "Error Orientation", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}