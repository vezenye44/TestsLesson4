package com.geekbrains.tests

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.view.search.ScreenState
import com.geekbrains.tests.view.search.SearchViewModel
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config
import repository.Repository

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        searchViewModel = SearchViewModel(repository)
    }

    @Test
    fun search_Test() {
        testCoroutineRule.runBlockingTest {

            Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
                Observable.just(
                    SearchResponse(
                        1,
                        listOf()
                    )
                )
            )
            searchViewModel.searchGitHub(SEARCH_QUERY)
            verify(repository, times(1)).searchGithub(SEARCH_QUERY)
        }
    }

    @Test
    fun liveData_TestReturnValueIsNotNull() {
        testCoroutineRule.runBlockingTest {

            val observer = Observer<ScreenState> {}

            val liveData = searchViewModel.subscribeToLiveData()

            Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
                Observable.just(
                    SearchResponse(
                        1,
                        listOf()
                    )
                )
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)
                Assert.assertNotNull(liveData.value)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun liveData_TestReturnLoadingValueAfterObserve() {
        testCoroutineRule.runBlockingTest {

            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
                Observable.never()
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)
                val value: ScreenState.Loading = liveData.value as ScreenState.Loading
                Assert.assertNotNull(value)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun liveData_TestReturnValueIsError() {
        testCoroutineRule.runBlockingTest {

            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()
            val error = Throwable(ERROR_TEXT)

            Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
                Observable.error(error)
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)
                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, error.message)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun liveData_TestReturnValueIsSuccess() {
        testCoroutineRule.runBlockingTest {

            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()
            val successValue = SearchResponse(42, listOf())

            Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
                Observable.just(successValue)
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)
                val value: ScreenState.Working = liveData.value as ScreenState.Working
                Assert.assertEquals(value.searchResponse.totalCount, successValue.totalCount)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun coroutines_TestException() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()
            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)
                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, EXCEPTION_TEXT)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }


    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
        private const val EXCEPTION_TEXT = "Response is null or unsuccessful"
    }
}