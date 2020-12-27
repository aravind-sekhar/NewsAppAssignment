package com.example.newsappassignment.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsappassignment.models.Fact
import com.example.newsappassignment.models.FactCategory

interface IFactsDataRepository {
    val status: MutableLiveData<String>
    val factCategory: LiveData<FactCategory>
    val allFacts: LiveData<List<Fact>>
    val isDataSynced: MutableLiveData<Boolean>
    fun setStatus(status: String?)

    suspend fun updateDataFromAPIServer()
}