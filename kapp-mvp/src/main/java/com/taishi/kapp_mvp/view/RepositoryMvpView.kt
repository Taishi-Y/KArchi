package com.taishi.kapp_mvp.view


import com.taishi.kapp_mvp.User

interface RepositoryMvpView : MvpView {

    fun showOwner(owner: User)

}
