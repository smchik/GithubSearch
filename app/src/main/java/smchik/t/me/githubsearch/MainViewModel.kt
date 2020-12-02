package smchik.t.me.githubsearch

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .apply {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            client(OkHttpClient.Builder().addInterceptor(interceptor).build())
        }
        .build()

    private val service = retrofit.create(GitHubService::class.java)

    private val disposables = CompositeDisposable()

    val repositories = MutableLiveData<List<Repository>>()

    fun searchRepositories(query: String) {
        disposables.clear()
        repositories.postValue(emptyList())
        if (query.isBlank()) {
            return
        }
        service.searchRepositories(query, page = 1, limit = 15)
            .subscribeOn(Schedulers.io())
            .zipWith(
                service.searchRepositories(query, page = 2, limit = 15)
                    .subscribeOn(Schedulers.io()),
                { first, second ->
                    first.items + second.items
                })
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