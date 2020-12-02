package smchik.t.me.githubsearch

import com.google.gson.annotations.SerializedName

data class Repository(
    val id: Long,
    val name: String,
    @SerializedName("stargazers_count")
    val stargazersCount: Int
)