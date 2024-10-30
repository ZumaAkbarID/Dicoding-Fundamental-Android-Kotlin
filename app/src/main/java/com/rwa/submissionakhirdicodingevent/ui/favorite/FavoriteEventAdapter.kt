package com.rwa.submissionakhirdicodingevent.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rwa.submissionakhirdicodingevent.R
import com.rwa.submissionakhirdicodingevent.data.local.entity.EventEntity
import com.rwa.submissionakhirdicodingevent.databinding.ItemRowEventBinding

class FavoriteEventAdapter(private val onFavClick: (EventEntity) -> Unit) :
    ListAdapter<EventEntity, FavoriteEventAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemRowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

        holder.itemView.setOnClickListener { view ->
            event?.let { event ->
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment2(
                    event.id,
                    event.name,
                    false
                )
                view.findNavController().navigate(action)
            }
        }

        // Kasih tau kalo ada perubahan di item ini
        holder.itemView.findViewById<View>(R.id.ivFav).setOnClickListener {
            onFavClick(event)
            notifyItemChanged(position)
        }
    }

    class MyViewHolder(private val binding: ItemRowEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity) {
            Glide.with(binding.imgItemPhoto.context)
                .load(event.imageLogo)
                .into(binding.imgItemPhoto)

            binding.tvItemTitle.text = event.name
            binding.tvItemDescription.text = event.summary

            val ivFav = binding.ivFav
            ivFav.isClickable = true
            ivFav.isFocusable = true

            if (event.isFav) {
                ivFav.setImageDrawable(ContextCompat.getDrawable(ivFav.context, R.drawable.fav))
            } else {
                ivFav.setImageDrawable(ContextCompat.getDrawable(ivFav.context, R.drawable.not_fav))
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
