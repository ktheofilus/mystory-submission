package com.example.myapplication.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.myapplication.repository.StoryDao
import org.junit.After
import org.junit.Before
import org.junit.Rule


class StoryDbTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: StoryDb
    private lateinit var dao: StoryDao
    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            StoryDb::class.java
        ).build()
        dao = database.storyDao()
    }

    @After
    fun closeDb() = database.close()
}