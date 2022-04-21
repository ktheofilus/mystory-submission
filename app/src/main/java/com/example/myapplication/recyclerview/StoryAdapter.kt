package com.example.myapplication.recyclerview

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.myapplication.DetailActivity
import com.example.myapplication.api.ListStoryItem
import com.example.myapplication.databinding.ItemRowStoryBinding
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private lateinit var circularProgressDrawable :CircularProgressDrawable

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        circularProgressDrawable = CircularProgressDrawable(parent.context)
        return ListViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)

        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        if (item != null) {
            Glide
                .with(holder.itemView.context)
                .load(item.photoUrl)
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(holder.binding.photoImageView)
            holder.binding.nameTextView.text = item.name
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("story", item)

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.photoImageView,"photo"),
                    Pair(holder.binding.nameTextView, "name"),
                    Pair(holder.binding.nameTextView, "description"),
                )
            holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
        }


    }

    class ListViewHolder(var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}
