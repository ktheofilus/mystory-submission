package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.datastore.preferences.core.edit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityStoryListBinding
import com.example.myapplication.di.DataStoreDI
import com.example.myapplication.di.DataStoreDI.dataStore
import com.example.myapplication.recyclerview.StoryAdapter
import com.example.myapplication.viewmodel.StoryListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class StoryListActivity : AppCompatActivity() {

    private lateinit var model: StoryListViewModel
    private lateinit var binding: ActivityStoryListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.swiperefresh.setOnRefreshListener {
            model.getStory()
        }

        val storyViewModel: StoryListViewModel by viewModels()

        model=storyViewModel

        model.getStory()

        model.isLoading.observe(this){
            model.showLoading(binding.swiperefresh)
        }

        model.message.observe(this){
            Snackbar.make(
                binding.root,
                it,
                Snackbar.LENGTH_SHORT
            ).show()
        }


        model.stories.observe(this){

            binding.rvStory.layoutManager = LinearLayoutManager(this)
            val listStoryItem = StoryAdapter(it)
            binding.rvStory.adapter = listStoryItem
        }

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

            else -> true
        }
    }

    private val uploadLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == UploadActivity.RESULT_CODE) {
            model.getStory()
        }
    }

    private fun logOut(){
        runBlocking {
            dataStore.edit { token ->
                token[DataStoreDI.logged]=""
            }
        }
        val loginActivity = Intent(this@StoryListActivity, MainActivity::class.java)
        startActivity(loginActivity)
        finish()
    }



//    override fun onResume() {
//        super.onResume()
//        model.getStory()
//    }

}
