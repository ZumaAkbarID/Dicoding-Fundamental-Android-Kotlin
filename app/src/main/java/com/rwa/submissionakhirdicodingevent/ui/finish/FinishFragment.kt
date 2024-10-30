package com.rwa.submissionakhirdicodingevent.ui.finish

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
import com.rwa.submissionakhirdicodingevent.databinding.FragmentFinishBinding
import com.rwa.submissionakhirdicodingevent.ui.ViewModelFactory

class FinishFragment : Fragment() {

    private var _binding: FragmentFinishBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val finishViewModel =
//            ViewModelProvider(this)[FinishViewModel::class.java]

        (activity as AppCompatActivity).supportActionBar?.title = "Finished Event"

        _binding = FragmentFinishBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: FinishViewModel by viewModels {
            factory
        }

        val adapter = FinishEventAdapter { event ->
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

        viewModel.getFinishedEvent().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        showLoading(false)
                        val event = result.data
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