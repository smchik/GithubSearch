package smchik.t.me.githubsearch

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(GitHubService::class.java)

    private val disposables = CompositeDisposable()

    val repositories = MutableLiveData<List<Repository>>()

    fun searchRepositories(query: String) {
        disposables.clear()
        if (query.isBlank()) {
            repositories.postValue(emptyList())
            return
        }
        service.searchRepositories(query)
            .map { response ->
                response.items
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    repositories.postValue(it)
                }, {
                    Log.e("MainViewModel", "error fetching repos", it)
                    // TODO: 12/2/20 add error handling
                }
            )
            .also {
                disposables.add(it)
            }
    }
}