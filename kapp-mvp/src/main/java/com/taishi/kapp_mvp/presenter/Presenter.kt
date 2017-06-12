package com.taishi.kapp_mvp.presenter

interface Presenter<V> {

    fun attachView(view: V)

    fun detachView()

}
