package smchik.t.me.githubsearch

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface GitHubService {

    @GET("/search/repositories?sort=stars&order=desc")
    fun searchRepositories(@Query("q") query: String): Single<RepositoriesResponse>
}