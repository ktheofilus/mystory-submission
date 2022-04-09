package com.example.myapplication.recyclerview

import android.app.Activity
import android.content.Intent
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

class StoryAdapter(private val listStory: List<ListStoryItem?>?) : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {

    private lateinit var circularProgressDrawable :CircularProgressDrawable

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        circularProgressDrawable = CircularProgressDrawable(parent.context)
        return ListViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = listStory?.get(position)


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

    override fun getItemCount(): Int = listStory?.size!!

    class ListViewHolder(var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root)


}
