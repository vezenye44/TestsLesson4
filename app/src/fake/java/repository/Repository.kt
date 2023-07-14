package repository

import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.model.SearchResult
import com.geekbrains.tests.presenter.RepositoryContract
import com.geekbrains.tests.repository.RepositoryCallback
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import kotlin.random.Random

class Repository : RepositoryContract {

    override fun searchGithub(
        query: String,
        callback: RepositoryCallback,
    ) {
        callback.handleGitHubResponse(Response.success(getFakeResponse()))
    }

    override fun searchGithub(query: String): Observable<SearchResponse> {
        return Observable.just(getFakeResponse())
    }

    private fun getFakeResponse(): SearchResponse {
        val list: MutableList<SearchResult> = mutableListOf()
        for (index in 1..100) {
            list.add(
                SearchResult(
                    id = index,
                    name = "Name: $index",
                    fullName = "FullName: $index",
                    private = Random.nextBoolean(),
                    description = "Description: $index",
                    updatedAt = "Updated: $index",
                    size = index,
                    stargazersCount = Random.nextInt(100),
                    language = "",
                    hasWiki = Random.nextBoolean(),
                    archived = Random.nextBoolean(),
                    score = index.toDouble()
                )
            )
        }
        return SearchResponse(list.size, list)
    }

    override suspend fun searchGithubAsync(query: String): SearchResponse {
        return generateSearchResponse()
    }

}