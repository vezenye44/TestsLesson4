package com.geekbrains.tests

import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.presenter.search.SearchPresenter
import com.geekbrains.tests.stubs.ScheduleProviderStub
import com.geekbrains.tests.view.search.ViewSearchContract
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import repository.Repository

class SearchPresenterTestRx {

    private lateinit var presenter: SearchPresenter

    @Mock
    private lateinit var repository: Repository

    @Mock
    private lateinit var viewContract: ViewSearchContract

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = SearchPresenter(viewContract, repository, ScheduleProviderStub())
    }

    @Test
    fun searchGitHub_Test() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    1,
                    listOf()
                )
            )
        )

        presenter.searchGitHub(SEARCH_QUERY)

        verify(repository, times(1)).searchGithub(SEARCH_QUERY)
    }

    @Test
    fun handleRequestError_Test() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.error(Throwable(ERROR_TEXT))
        )

        presenter.searchGitHub(SEARCH_QUERY)

        verify(viewContract, times(1)).displayError(ERROR_TEXT)
    }

    @Test
    fun handleResponseError_TotalCountIsNull() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    null,
                    listOf()
                )
            )
        )

        presenter.searchGitHub(SEARCH_QUERY)

        verify(viewContract, times(1))
            .displayError(EMPTY_RESULT_TEXT)
    }

    @Test
    fun handleResponseSuccess_ViewContractMethodOrder() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    42,
                    listOf()
                )
            )
        )
        val inOrder = inOrder(viewContract)

        presenter.searchGitHub(SEARCH_QUERY)

        inOrder.verify(viewContract).displayLoading(true)
        inOrder.verify(viewContract).displaySearchResults(listOf(),42)
        inOrder.verify(viewContract).displayLoading(false)
    }

    @Test
    fun handleResponseError_TotalCountIsNull_ViewContractMethodOrder() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    null,
                    listOf()
                )
            )
        )
        val inOrder = inOrder(viewContract)

        presenter.searchGitHub(SEARCH_QUERY)

        inOrder.verify(viewContract).displayLoading(true)
        inOrder.verify(viewContract).displayError(EMPTY_RESULT_TEXT)
        inOrder.verify(viewContract).displayLoading(false)
    }

    @Test
    fun handleResponseError_AnyError_ViewContractMethodOrder() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.error(Throwable(ERROR_TEXT))
        )
        val inOrder = inOrder(viewContract)

        presenter.searchGitHub(SEARCH_QUERY)

        inOrder.verify(viewContract).displayLoading(true)
        inOrder.verify(viewContract).displayError(ERROR_TEXT)
     }

    @Test
    fun handleResponseSuccess() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    42,
                    listOf()
                )
            )
        )

        presenter.searchGitHub(SEARCH_QUERY)

        verify(viewContract, times(1)).displaySearchResults(listOf(), 42)
    }

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
        private const val EMPTY_RESULT_TEXT = "Search results or total count are null"
    }
}