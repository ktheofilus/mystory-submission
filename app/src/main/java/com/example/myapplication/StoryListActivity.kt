package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.example.myapplication.databinding.ActivityStoryListBinding
import com.example.myapplication.di.AppModule
import com.example.myapplication.di.AppModule.dataStore
import com.example.myapplication.recyclerview.LoadingStateAdapter
import com.example.myapplication.recyclerview.StoryAdapter
import com.example.myapplication.viewmodel.StoryListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class StoryListActivity : AppCompatActivity() {

    private val  model: StoryListViewModel by viewModels()
    private lateinit var binding: ActivityStoryListBinding
    private lateinit var adapter:StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.swiperefresh.isRefreshing=true

        adapter = StoryAdapter()


        binding.swiperefresh.setOnRefreshListener {
            adapter.refresh()
        }

        getData()

        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                val totalNumberOfItems = adapter.itemCount
                binding.swiperefresh.isRefreshing = totalNumberOfItems == 0
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.optionmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logoutButton -> {
                logOut()
                true
            }
            R.id.addPhoto -> {
                val uploadIntent = Intent(this@StoryListActivity, UploadActivity::class.java)
                uploadLauncher.launch(uploadIntent)

                true
            }
            R.id.mapButton -> {
                val mapIntent = Intent(this@StoryListActivity, MapsActivity::class.java)
                startActivity(mapIntent)
                true
            }
            else -> true
        }
    }

    private fun getData() {
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = adapter

        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{adapter.retry()}
        )

        model.storyItem.observe(this) {pagingData->
            adapter.submitData(lifecycle, pagingData)

        }

    }

    private val uploadLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == UploadActivity.RESULT_CODE) {
            adapter.refresh()
            binding.rvStory.invalidate()
        }
    }

    private fun logOut(){
        runBlocking {
            dataStore.edit { token ->
                token[AppModule.logged]=""
            }
        }
        val loginActivity = Intent(this@StoryListActivity, MainActivity::class.java)
        startActivity(loginActivity)
        finish()
    }

}


