package com.taishi.kapp_mvp.presenter

import android.util.Log
import com.taishi.kapp_mvp.ArchiApplication
import com.taishi.kapp_mvp.view.RepositoryMvpView
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers


class RepositoryPresenter : Presenter<RepositoryMvpView> {

    private var repositoryMvpView: RepositoryMvpView? = null
    private var subscription: Subscription? = null

    override fun attachView(view: RepositoryMvpView) {
        this.repositoryMvpView = view
    }

    override fun detachView() {
        this.repositoryMvpView = null
        if (subscription != null) subscription!!.unsubscribe()
    }

    fun loadOwner(userUrl: String) {
        val application = ArchiApplication[repositoryMvpView!!.getContext()]
        val githubService = application.githubService
        subscription = githubService!!.userFromUrl(userUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe { user ->
                    Log.i(TAG, "Full user data loaded " + user)
                    repositoryMvpView!!.showOwner(user)
                }
    }

    companion object {

        private val TAG = "RepositoryPresenter"
    }
}
