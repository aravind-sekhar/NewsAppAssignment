package com.example.newsappassignment.source.local

import androidx.lifecycle.LiveData
import com.example.newsappassignment.models.Fact
import com.example.newsappassignment.models.FactCategory
import com.example.newsappassignment.models.Model

interface ILocalDataSource {
    val factCategory: LiveData<FactCategory>
    val allFacts: LiveData<List<Fact>>

    suspend fun syncDB(data: Model.Result?): Boolean

    fun generateHash(input: String): String

    companion object {
        val HEX_CHARS = "0123456789ABCDEF".toCharArray()
    }
}