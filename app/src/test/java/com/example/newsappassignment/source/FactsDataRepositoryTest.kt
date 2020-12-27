package com.example.newsappassignment.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.newsappassignment.models.Fact
import com.example.newsappassignment.models.FactCategory
import com.example.newsappassignment.models.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

class FactsDataRepositoryTest {
    lateinit var factsDataRepository : FactsDataRepository
    lateinit var localDataSource: FakeLocalDataSource
    lateinit var remoteDataSource: FakeRemoteDataSource

    val factCategory: MutableLiveData<FactCategory> = MutableLiveData()
    val allFacts: MutableLiveData<List<Fact>> = MutableLiveData()

    var isConnected = false
    var response : ArrayList<Model.Row> = ArrayList()
    var title : String =""


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @ExperimentalCoroutinesApi
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun createRepository(){
        localDataSource = FakeLocalDataSource(factCategory,allFacts)
        remoteDataSource = FakeRemoteDataSource(title,isConnected,response)
        factsDataRepository = FactsDataRepository(localDataSource,remoteDataSource)
    }

    @Before
    fun setupDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun updateFromAPIServer_notConnected_returnError() = runBlocking {


        val result = Result.Error(Exception("Not Loading")).toString()
        factsDataRepository.updateDataFromAPIServer()
        assertEquals(result,factsDataRepository.status.value)
    }

    @Test
    fun updateFromAPIServer_Connected_returnError() = runBlocking {


        val result = "Data Synced with the Server"
        val rows = ArrayList<Model.Row>()
        rows.add(Model.Row("title1","description1","image1"))
        rows.add(Model.Row("title2","description2","image2"))
        rows.add(Model.Row("title3","description3","image3"))
        val title = "facts"
        remoteDataSource.isConnected = true
        remoteDataSource.title = title
        remoteDataSource.response = rows
        factsDataRepository.updateDataFromAPIServer()
        assertEquals(result,factsDataRepository.status.value)
    }
}