package com.geekbrains.tests.view.search


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.geekbrains.tests.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val materialButton = onView(
            allOf(
                withId(R.id.toDetailsActivityButton), withText("to details"), childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content), 0
                    ), 3
                ), isDisplayed()
            )
        )
        materialButton.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.totalCountTextView),
                withText("Number of results: 0"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))

        val textView2 = onView(
            allOf(
                withId(R.id.totalCountTextView),
                withText("Number of results: 0"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Number of results: 0")))

        val materialButton2 = onView(
            allOf(
                withId(R.id.incrementButton), withText("+"), childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content), 0
                    ), 2
                ), isDisplayed()
            )
        )
        materialButton2.perform(click())

        val textView3 = onView(
            allOf(
                withId(R.id.totalCountTextView),
                withText("Number of results: 1"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Number of results: 1")))

        val materialButton3 = onView(
            allOf(
                withId(R.id.decrementButton), withText("-"), childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content), 0
                    ), 0
                ), isDisplayed()
            )
        )
        materialButton3.perform(click())

        val textView4 = onView(
            allOf(
                withId(R.id.totalCountTextView),
                withText("Number of results: 0"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Number of results: 0")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int,
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent) && view == parent.getChildAt(
                    position
                )
            }
        }
    }
}
