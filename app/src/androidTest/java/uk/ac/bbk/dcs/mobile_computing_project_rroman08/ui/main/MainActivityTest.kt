package uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.R

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testRecyclerViewIsDisplayed() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.recycler_view_data)).check(matches(isDisplayed()))
    }
}
