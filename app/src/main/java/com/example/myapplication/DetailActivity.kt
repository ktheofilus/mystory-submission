package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.myapplication.api.ListStoryItem
import com.example.myapplication.api.ListStoryItemLocation
import com.example.myapplication.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupData()
    }


    private fun setupData() {
        val story = intent.getParcelableExtra<ListStoryItem>("story") as ListStoryItem?
        val storyLoc = intent.getParcelableExtra<ListStoryItemLocation>("storyLoc") as ListStoryItemLocation?
        if (story!=null){
            Glide.with(applicationContext)
                .load(story.photoUrl)
                .into(binding.imageView)
            binding.nameTextView.text = story.name
            binding.descTextView.text = story.description
        }
        else {
            Glide.with(applicationContext)
                .load(storyLoc?.photoUrl)
                .into(binding.imageView)
            binding.nameTextView.text = storyLoc?.name
            binding.descTextView.text = storyLoc?.description
        }

    }
}