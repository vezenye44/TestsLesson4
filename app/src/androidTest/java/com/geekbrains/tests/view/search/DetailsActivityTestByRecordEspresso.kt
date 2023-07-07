package com.geekbrains.tests.view.search


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.R
import com.geekbrains.tests.view.details.DetailsActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

const val TEST_NUMBER_OF_RESULTS_ZERO = "Number of results: 0"
const val TEST_NUMBER_OF_RESULTS_PLUS_1 = "Number of results: 1"

@RunWith(AndroidJUnit4::class)
class DetailsActivityTestByRecordEspresso {

    private lateinit var totalCountTextView: ViewInteraction
    private lateinit var incrementButton: ViewInteraction
    private lateinit var decrementButton: ViewInteraction

    @Rule
    @JvmField
    var activityScenarioRule = ActivityScenarioRule(DetailsActivity::class.java)

    @Before
    fun setUp() {
        totalCountTextView = onView(withId(R.id.totalCountTextView))
        incrementButton = onView(withId(R.id.incrementButton))
        decrementButton = onView(withId(R.id.decrementButton))
    }

    @Test
    fun viewsIsDisplayedTest() {
        totalCountTextView.check(matches(isDisplayed()))
        incrementButton.check(matches(isDisplayed()))
        decrementButton.check(matches(isDisplayed()))
    }

    @Test
    fun detailsActivityTest() {

        totalCountTextView.check(matches(withText(TEST_NUMBER_OF_RESULTS_ZERO)))

        incrementButton.perform(click())

        totalCountTextView.check(matches(withText(TEST_NUMBER_OF_RESULTS_PLUS_1)))

        decrementButton.perform(click())

        totalCountTextView.check(matches(withText(TEST_NUMBER_OF_RESULTS_ZERO)))
    }
}
