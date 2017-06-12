package com.taishi.kapp_mvp.view

import com.taishi.kapp_mvp.Repository


interface MainMvpView : MvpView {

    fun showRepositories(repositories: List<Repository>)

    fun showMessage(stringId: Int)

    fun showProgressIndicator()
}
