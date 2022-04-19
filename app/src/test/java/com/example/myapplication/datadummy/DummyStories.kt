package com.example.myapplication

import com.example.myapplication.api.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                "photourl + $i",
                "created + $i",
                "name $i",
                "desc + $i",
                "id $i",
            )
            items.add(quote)
        }
        return items
    }
}