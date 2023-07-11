package com.geekbrains.tests.espresso

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.view.details.DetailsFragment
import com.geekbrains.tests.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

const val TEST_NUMBER_OF_RESULTS_ZERO = "Number of results: 0"
const val TEST_NUMBER_OF_RESULTS_PLUS_1 = "Number of results: 1"
const val TEST_NUMBER_OF_RESULTS_MINUS_1 = "Number of results: -1"

@RunWith(AndroidJUnit4::class)
class DetailsFragmentEspressoTest {

    private lateinit var scenario: FragmentScenario<DetailsFragment>

    private lateinit var totalCountTextView: ViewInteraction
    private lateinit var incrementButton: ViewInteraction
    private lateinit var decrementButton: ViewInteraction

    @Before
    fun setup() {
        scenario = launchFragmentInContainer()

        totalCountTextView = onView(withId(R.id.totalCountTextView))
        incrementButton = onView(withId(R.id.incrementButton))
        decrementButton = onView(withId(R.id.decrementButton))
    }

    @Test
    fun fragment_testCorrectInitialize() {
        totalCountTextView.check(matches(ViewMatchers.isDisplayed()))
        incrementButton.check(matches(ViewMatchers.isDisplayed()))
        decrementButton.check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun fragment_testBundle() {
        val fragmentArgs = bundleOf("TOTAL_COUNT_EXTRA" to 10)

        scenario = launchFragmentInContainer<DetailsFragment>(fragmentArgs)

        val assertion = matches(withText("Number of results: 10"))
        totalCountTextView.check(assertion)
    }

    @Test
    fun fragment_testIncrementButton() {
        incrementButton.perform(click())
        totalCountTextView.check(matches(withText(TEST_NUMBER_OF_RESULTS_PLUS_1)))
    }

    @Test
    fun fragment_testDecrementButton() {
        decrementButton.perform(click())
        totalCountTextView.check(matches(withText(TEST_NUMBER_OF_RESULTS_MINUS_1)))
    }

    @After
    fun close() {
        scenario.close()
    }

}