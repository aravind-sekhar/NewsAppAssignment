package com.example.newsappassignment

import android.app.Activity
import android.os.Build
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import junit.framework.Assert.assertNotNull
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityTest {

    private lateinit var activity : Activity

    @Before
    @Throws(Exception::class)
    fun setUp() {
        activity = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .get()

    }

    @Test
    @Throws(java.lang.Exception::class)
    fun shouldNotBeNull() {
        assertNotNull(activity)
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun shouldHaveCorrectAppName() {
        val hello = activity.resources.getString(R.string.app_name)
        assertThat(hello, equalTo("NewsAppAssignment"))
    }

    @Test
    fun testView() {
        ActivityScenario.launch(MainActivity::class.java)
        Espresso.onData(
            Matchers.allOf(
                Matchers.instanceOf(TextView::class.java),
                equalTo("Beavers")
            )
        )
        Espresso.onView(withId(R.id.swipe_refresh_view)).perform(ViewActions.swipeDown())

    }

}
