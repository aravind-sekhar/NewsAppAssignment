package com.example.newsappassignment.source

import androidx.lifecycle.MutableLiveData
import com.example.newsappassignment.models.Fact
import com.example.newsappassignment.models.FactCategory

class FakeFactDataRepository : IFactsDataRepository {
    override val status: MutableLiveData<String> = MutableLiveData()
    override val factCategory: MutableLiveData<FactCategory> = MutableLiveData()
    override val allFacts: MutableLiveData<List<Fact>> = MutableLiveData()
    override val isDataSynced: MutableLiveData<Boolean> = MutableLiveData()


    fun setupData(
        status: String,
        factCategory: FactCategory,
        facts: ArrayList<Fact>,
        isSynced: Boolean
    ) {
        this.status.postValue(status)
        this.factCategory.postValue(factCategory)
        this.allFacts.postValue(facts)
        this.isDataSynced.postValue(isSynced)
    }

    override fun setStatus(status: String?) {
        this.status.postValue(status)
    }

    override suspend fun updateDataFromAPIServer() {
        TODO("Not yet implemented")
    }

}