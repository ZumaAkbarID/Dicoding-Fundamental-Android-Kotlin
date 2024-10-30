package com.rwa.submissionakhirdicodingevent.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.rwa.submissionakhirdicodingevent.R
import com.rwa.submissionakhirdicodingevent.databinding.FragmentDetailBinding
import com.rwa.submissionakhirdicodingevent.ui.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: DetailViewModel
    private lateinit var factory: ViewModelFactory

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val detailViewModel =
//            ViewModelProvider(this)[DetailViewModel::class.java]

        factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        val args: DetailFragmentArgs by navArgs()
        val eventId = args.eventId
        val title = args.title
        val showRegister = args.showRegister

        (activity as AppCompatActivity).supportActionBar?.title = title

        viewModel.showDetailEvent(eventId)

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.showError.observe(viewLifecycleOwner) { showError ->
            if (showError) {
                showErrorToast()
                viewModel.resetError()
            }
        }

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        viewModel.event.observe(viewLifecycleOwner) { data ->
            data?.let {
                Glide.with(binding.ivPicture.context).load(data.mediaCover).into(binding.ivPicture)

                binding.tvTitle.text = data.name
                binding.tvOwner.text = data.ownerName

                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

                val datetime = inputFormat.parse(data.beginTime)

                binding.tvDetail.text = """
                    Category        : ${data.category}
                    City            : ${data.cityName}
                    Quota Remaining : ${data.quota.minus(data.registrants)}
                    Date            : ${
                    datetime?.let { it1 ->
                        SimpleDateFormat("d MMMM yyyy", Locale("en", "US")).format(
                            it1
                        )
                    }
                }
                    Time            : ${
                    datetime?.let { it1 ->
                        SimpleDateFormat("HH:mm", Locale.getDefault()).format(
                            it1
                        )
                    }
                }
                """.trimIndent()

                binding.tvContent.text =
                    Html.fromHtml(data.description, Html.FROM_HTML_MODE_COMPACT)

                binding.btnEvent.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse(data.link))
                    startActivity(intent)
                }

                favToggle(data.isFav)

                binding.ivFav.setOnClickListener {
                    favToggle(!data.isFav)

                    if (data.isFav) {
                        viewModel.deleteFavEvent(data)
                    } else {
                        viewModel.favEvent(data)
                    }
                }
            }
        }

        if (!showRegister) {
            binding.btnEvent.text = "View Event"
        }

        return root
    }

    private fun favToggle(isFav: Boolean) {
        if (isFav) {
            binding.ivFav.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.ivFav.context,
                    R.drawable.fav
                )
            )
        } else {
            binding.ivFav.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.ivFav.context,
                    R.drawable.not_fav
                )
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorToast() {
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearEventData()
        _binding = null
    }
}