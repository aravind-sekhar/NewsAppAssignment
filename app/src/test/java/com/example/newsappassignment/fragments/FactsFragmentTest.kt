package com.example.newsappassignment.fragments

import android.os.Build
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.newsappassignment.models.Fact
import com.example.newsappassignment.models.FactCategory
import com.example.newsappassignment.source.FakeFactDataRepository
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*
import kotlin.collections.ArrayList

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.N])
class FactsFragmentTest {

    private lateinit var fakeFactsDataRepository: FakeFactDataRepository

    @Before
    fun initRepository() {
        fakeFactsDataRepository = FakeFactDataRepository()
    }


    @Test
    fun startFragment_shouldStartFragment() {
        val scenario = launchFragmentInContainer<FactsFragment>()
        scenario.moveToState(Lifecycle.State.CREATED)
        Assert.assertNotNull(scenario)
    }

    @Test
    fun factsTest_Loading() {
        launchFragmentInContainer<FactsFragment>()
        Espresso.onView(withId(com.example.newsappassignment.R.id.tv_loading_text)).check(
            ViewAssertions.matches(ViewMatchers.withText("loading…"))
        )
        Espresso.onView(withId(com.example.newsappassignment.R.id.progress_bar)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun factsDataTest() {
        val testFactCategory = FactCategory(UUID.randomUUID().toString(), "testTitle")
        val facts = ArrayList<Fact>()
        facts.add(Fact("1", "title 1", "description 1", "image 1", testFactCategory.id))
        facts.add(Fact("2", "title 3", "description 2", "image 2", testFactCategory.id))
        facts.add(Fact("3", "title 2", "description 3", "image 3", testFactCategory.id))
        fakeFactsDataRepository.setupData("Data synced with the server.", testFactCategory, facts, true)
        launchFragmentInContainer<FactsFragment>()
        Espresso.onData(Matchers.equalTo(facts[0]))
        Espresso.onData(Matchers.equalTo(facts[1]))
        Espresso.onData(Matchers.equalTo(facts[2]))
    }

    @Test
    fun factsDataTest_NoData() {
        val testFactCategory = FactCategory(UUID.randomUUID().toString(), "testTitle")
        val facts = ArrayList<Fact>()
        fakeFactsDataRepository.setupData("Data synced with the server.", testFactCategory, facts, true)
        launchFragmentInContainer<FactsFragment>()
        Espresso.onData(
            Matchers.allOf(
                Matchers.instanceOf(TextView::class.java),
                Matchers.equalTo("No data.")
            )
        )
    }

    @Test
    fun isRecyclerViewVisible() {
        launchFragmentInContainer<FactsFragment>()
        Espresso.onView(withId(com.example.newsappassignment.R.id.rv_facts_list))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))

    }

}