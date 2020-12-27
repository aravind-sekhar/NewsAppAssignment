package com.example.newsappassignment.viewmodels

import androidx.lifecycle.*
import com.example.newsappassignment.models.Fact
import com.example.newsappassignment.models.FactCategory
import com.example.newsappassignment.source.IFactsDataRepository
import kotlinx.coroutines.launch

class MainViewModel(private val factsDataRepository: IFactsDataRepository) : ViewModel() {

    private val factCategory: LiveData<FactCategory> = factsDataRepository.factCategory
    private val facts: LiveData<List<Fact>> = factsDataRepository.allFacts
    private val statusMsg: MutableLiveData<String> = factsDataRepository.status
    private val isDataSynced: MutableLiveData<Boolean> = factsDataRepository.isDataSynced

    fun getFactCategory(): LiveData<FactCategory> {
        return factCategory
    }

    fun getFacts(): LiveData<List<Fact>> {
        return facts
    }

    fun getStatusMsg(): MutableLiveData<String> {
        return statusMsg
    }

    fun isDataSynced(): MutableLiveData<Boolean> {
        return isDataSynced
    }


    fun syncDataFromAPI(swiped: Boolean = false) {
        if (!isDataSynced.value!! || swiped) {
            viewModelScope.launch {
                factsDataRepository.updateDataFromAPIServer()
            }
        }
    }

    fun setStatusMsg(status: String) {
        factsDataRepository.setStatus(status)
    }


    class FactsViewModelFactory(
        private val factsDataRepository: IFactsDataRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            (MainViewModel(factsDataRepository) as T)
    }
}