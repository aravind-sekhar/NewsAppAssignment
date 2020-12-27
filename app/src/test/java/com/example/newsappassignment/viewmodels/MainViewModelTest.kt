package com.example.newsappassignment.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.newsappassignment.source.FakeFactDataRepository
import com.example.newsappassignment.ServiceLocator.factsDataRepository
import com.example.newsappassignment.getOrAwaitValue
import com.example.newsappassignment.models.Fact
import com.example.newsappassignment.models.FactCategory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*


class MainViewModelTest {
    lateinit var fakeFactDataRepository : FakeFactDataRepository
    lateinit var viewModel: MainViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        fakeFactDataRepository = FakeFactDataRepository()
        viewModel = MainViewModel(fakeFactDataRepository)
    }

    @Test
    fun getFactCategoryTest() {
        val factCategory = FactCategory(UUID.randomUUID().toString(), "Title")
        fakeFactDataRepository.factCategory.value = factCategory
        val result = viewModel.getFactCategory().getOrAwaitValue()
        assertEquals(factCategory, result)
    }

    @Test
    fun getFactsTest() {
        val factCategory = FactCategory(UUID.randomUUID().toString(), "testTitle")
        val facts = ArrayList<Fact>()
        facts.add(Fact("1", "title 1", "description 1", "image1", factCategory.id))
        facts.add(Fact("2", "title 2", "description 2", "image2", factCategory.id))
        facts.add(Fact("3", "title 3", "description 3", "image3", factCategory.id))
        fakeFactDataRepository.allFacts.value = facts
        val result = factsDataRepository?.allFacts?.getOrAwaitValue()
        result?.let {
            for (i in result.indices) {
                assertEquals(facts[i].title, result[i].title)
                assertEquals(facts[i].description, result[i].description)
                assertEquals(facts[i].imageHref, result[i].imageHref)
            }
        }
    }

    @Test
    fun setStatusMsgTest() {
        val value = "Test Status"
        viewModel.setStatusMsg(value)
        val result = viewModel.getStatusMsg().getOrAwaitValue()
        assertEquals(value, result)
    }

    @Test
    fun isDataSynced_dataSynced_returnsTrue() {
        fakeFactDataRepository.isDataSynced.postValue(true)
        val output = viewModel.isDataSynced().getOrAwaitValue()
        assertEquals(true, output)
    }

    @Test
    fun isDataSynced_notDataSynced_returnsFalse() {
        fakeFactDataRepository.isDataSynced.postValue(false)
        val output = viewModel.isDataSynced().getOrAwaitValue()
        assertEquals(false, output)
    }
}