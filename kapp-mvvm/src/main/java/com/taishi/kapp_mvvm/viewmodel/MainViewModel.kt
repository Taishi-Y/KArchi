package com.taishi.kapp_mvvm.viewmodel

import android.content.Context
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.taishi.kapp_mvvm.ArchiApplication
import com.taishi.kapp_mvvm.R
import com.taishi.kapp_mvvm.model.Repository
import retrofit2.adapter.rxjava.HttpException
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

/**
 * View model for the MainActivity
 */
class MainViewModel(private var context: Context?, private var dataListener: MainViewModel.DataListener?) : ViewModel {

    var infoMessageVisibility: ObservableInt
    var progressVisibility: ObservableInt
    var recyclerViewVisibility: ObservableInt
    var searchButtonVisibility: ObservableInt
    var infoMessage: ObservableField<String>
    private var subscription: Subscription? = null
    private var repositories: List<Repository>? = null
    private var editTextUsernameValue: String? = null

    init {
        infoMessageVisibility = ObservableInt(View.VISIBLE)
        progressVisibility = ObservableInt(View.INVISIBLE)
        recyclerViewVisibility = ObservableInt(View.INVISIBLE)
        searchButtonVisibility = ObservableInt(View.GONE)
        infoMessage = ObservableField(context!!.getString(R.string.default_info_message))
    }

    fun setDataListener(dataListener: DataListener) {
        this.dataListener = dataListener
    }

    override fun destroy() {
        if (subscription != null && !subscription!!.isUnsubscribed) subscription!!.unsubscribe()
        subscription = null
        context = null
        dataListener = null
    }

    fun onSearchAction(view: TextView, actionId: Int, event: KeyEvent): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val username = view.text.toString()
            if (username.length > 0) loadGithubRepos(username)
            return true
        }
        return false
    }

    fun onClickSearch(view: View) {
        loadGithubRepos(editTextUsernameValue!!)
    }

    val usernameEditTextWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                editTextUsernameValue = charSequence.toString()
                searchButtonVisibility.set(if (charSequence.length > 0) View.VISIBLE else View.GONE)
            }

            override fun afterTextChanged(editable: Editable) {

            }
        }

    private fun loadGithubRepos(username: String) {
        progressVisibility.set(View.VISIBLE)
        recyclerViewVisibility.set(View.INVISIBLE)
        infoMessageVisibility.set(View.INVISIBLE)
        if (subscription != null && !subscription!!.isUnsubscribed) subscription!!.unsubscribe()
        val application = ArchiApplication[context!!]
        val githubService = application.githubService
        subscription = githubService!!.publicRepositories(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(object : Subscriber<List<Repository>>() {
                    override fun onCompleted() {
                        if (dataListener != null) dataListener!!.onRepositoriesChanged(repositories!!)
                        progressVisibility.set(View.INVISIBLE)
                        if (!repositories!!.isEmpty()) {
                            recyclerViewVisibility.set(View.VISIBLE)
                        } else {
                            infoMessage.set(context!!.getString(R.string.text_empty_repos))
                            infoMessageVisibility.set(View.VISIBLE)
                        }
                    }

                    override fun onError(error: Throwable) {
                        Log.e(TAG, "Error loading GitHub repos ", error)
                        progressVisibility.set(View.INVISIBLE)
                        if (isHttp404(error)) {
                            infoMessage.set(context!!.getString(R.string.error_username_not_found))
                        } else {
                            infoMessage.set(context!!.getString(R.string.error_loading_repos))
                        }
                        infoMessageVisibility.set(View.VISIBLE)
                    }

                    override fun onNext(repositories: List<Repository>) {
                        Log.i(TAG, "Repos loaded " + repositories)
                        this@MainViewModel.repositories = repositories
                    }
                })
    }

    interface DataListener {
        fun onRepositoriesChanged(repositories: List<Repository>)
    }

    companion object {

        private val TAG = "MainViewModel"

        private fun isHttp404(error: Throwable): Boolean {
            return error is HttpException && error.code() == 404
        }
    }
}
