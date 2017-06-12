package com.taishi.kapp_mvp.presenter

import android.util.Log
import com.taishi.kapp_mvp.ArchiApplication
import com.taishi.kapp_mvp.R
import com.taishi.kapp_mvp.Repository
import com.taishi.kapp_mvp.view.MainMvpView
import retrofit2.adapter.rxjava.HttpException
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers


class MainPresenter : Presenter<MainMvpView> {

    private var mainMvpView: MainMvpView? = null
    private var subscription: Subscription? = null
    private var repositories: List<Repository>? = null

    override fun attachView(view: MainMvpView) {
        this.mainMvpView = view
    }

    override fun detachView() {
        this.mainMvpView = null
        if (subscription != null) subscription!!.unsubscribe()
    }

    fun loadRepositories(usernameEntered: String) {
        val username = usernameEntered.trim { it <= ' ' }
        if (username.isEmpty()) return

        mainMvpView!!.showProgressIndicator()
        if (subscription != null) subscription!!.unsubscribe()
        val application = ArchiApplication[mainMvpView!!.context]
        val githubService = application.githubService
        subscription = githubService!!.publicRepositories(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(object : Subscriber<List<Repository>>() {
                    override fun onCompleted() {
                        Log.i(TAG, "Repos loaded " + repositories!!)
                        if (!repositories!!.isEmpty()) {
                            mainMvpView!!.showRepositories(repositories!!)
                        } else {
                            mainMvpView!!.showMessage(R.string.text_empty_repos)
                        }
                    }

                    override fun onError(error: Throwable) {
                        Log.e(TAG, "Error loading GitHub repos ", error)
                        if (isHttp404(error)) {
                            mainMvpView!!.showMessage(R.string.error_username_not_found)
                        } else {
                            mainMvpView!!.showMessage(R.string.error_loading_repos)
                        }
                    }

                    override fun onNext(repositories: List<Repository>) {
                        this@MainPresenter.repositories = repositories
                    }
                })
    }

    companion object {

        var TAG = "MainPresenter"

        private fun isHttp404(error: Throwable): Boolean {
            return error is HttpException && error.code() == 404
        }
    }

}
