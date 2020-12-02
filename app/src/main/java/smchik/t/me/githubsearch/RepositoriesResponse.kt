package smchik.t.me.githubsearch

import com.google.gson.annotations.SerializedName

data class RepositoriesResponse(
    @SerializedName("total_count")
    val totalCount: Int,
    val items: List<Repository>
)