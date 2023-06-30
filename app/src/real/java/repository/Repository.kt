package repository

import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.presenter.RepositoryContract
import com.geekbrains.tests.repository.RepositoryCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(
    private val gitHubApi: GitHubApi = RetrofitService()
        .createRetrofit()
        .create(GitHubApi::class.java),
) : RepositoryContract {

    override fun searchGithub(
        query: String,
        callback: RepositoryCallback,
    ) {
        val call = gitHubApi.searchGithub(query)
        call?.enqueue(object : Callback<SearchResponse?> {

            override fun onResponse(
                call: Call<SearchResponse?>,
                response: Response<SearchResponse?>,
            ) {
                callback.handleGitHubResponse(response)
            }

            override fun onFailure(
                call: Call<SearchResponse?>,
                t: Throwable,
            ) {
                callback.handleGitHubError()
            }
        })
    }
}