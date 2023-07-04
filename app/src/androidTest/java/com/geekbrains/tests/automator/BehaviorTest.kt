package com.geekbrains.tests.automator

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TIMEOUT = 5_000L

@RunWith(AndroidJUnit4::class)
class BehaviorTest {

    private val uiDevice = UiDevice.getInstance(getInstrumentation())

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val packageName = context.packageName

    @Before
    fun setUp() {
        uiDevice.pressHome()

        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        context.startActivity(intent)

        uiDevice.wait(Until.findObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

    @Test
    fun test_MainActivityIsStarted() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        assertNotNull(editText)
    }

    @Test
    fun test_SearchIsPositive() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        editText.text = "algol"

        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        searchButton.click()

        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextView")),
                TIMEOUT
            )

        assertEquals(changedText.text.toString(), "Number of results: 3807")
    }

    @Test
    fun test_OpenDetailsScreen() {
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))

        toDetails.click()

        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextView")),
                TIMEOUT
            )

        assertEquals(changedText.text, "Number of results: 0")
    }

    @Test
    fun test_CorrectSearchResultsInDetailsScreen() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        editText.text = "algol"

        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        searchButton.click()

        uiDevice.wait(
            Until.findObject(By.res(packageName, "totalCountTextView")),
            TIMEOUT
        )

        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))

        toDetails.click()

        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextView")),
                TIMEOUT
            )

        assertEquals(changedText.text, "Number of results: 3807")
    }

    @Test
    fun test_showErrorMessage() {
        val root =
            UiCollection(UiSelector().className("androidx.constraintlayout.widget.ConstraintLayout"))

        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        editText.text = ""

        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        searchButton.click()

        val errorToast = root.getChild(UiSelector().className("android.widget.Toast"))

        assertNotNull(errorToast)
    }

    @Test
    fun test_incrementButtonMotNull() {
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.click()

        val incrementButton = uiDevice.wait(
            Until.findObject(By.res(packageName, "incrementButton")),
            TIMEOUT
        )
        assertNotNull(incrementButton)
    }

    @Test
    fun test_decrementButtonMotNull() {
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.click()

        val decrementButton = uiDevice.wait(
            Until.findObject(By.res(packageName, "decrementButton")),
            TIMEOUT
        )
        assertNotNull(decrementButton)
    }

    @Test
    fun test_incrementButtonClicked() {
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.click()

        val incrementButton = uiDevice.wait(
            Until.findObject(By.res(packageName, "incrementButton")),
            TIMEOUT
        )
        val totalCountTextView = uiDevice.findObject(By.res(packageName, "totalCountTextView"))

        incrementButton.click()

        assertEquals(totalCountTextView.text, "Number of results: 1")
    }

    @Test
    fun test_decrementButtonClicked() {
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.click()

        val decrementButton = uiDevice.wait(
            Until.findObject(By.res(packageName, "decrementButton")),
            TIMEOUT
        )
        val totalCountTextView = uiDevice.findObject(By.res(packageName, "totalCountTextView"))

        decrementButton.click()

        assertEquals(totalCountTextView.text, "Number of results: -1")
    }

}