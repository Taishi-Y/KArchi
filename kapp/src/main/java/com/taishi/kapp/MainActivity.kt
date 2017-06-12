package com.taishi.kapp

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.taishi.kapp.model.Repository
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.adapter.rxjava.HttpException
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "MainActivity"
    }

    private var subscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //Set up RecyclerView
        setupRecyclerView(repos_recycler_view)
        // Set up search button
        button_search.setOnClickListener { loadGithubRepos(edit_text_username.text.toString()) }
        //Set up username EditText
        edit_text_username.addTextChangedListener(mHideShowButtonTextWatcher)
        edit_text_username.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val username: String = edit_text_username.text.toString()
                if (username.isNotEmpty()) loadGithubRepos(username)
                return@OnEditorActionListener true
            }
            false
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (subscription != null) subscription!!.unsubscribe()
    }

    fun loadGithubRepos(username: String) {
        progress.visibility = View.VISIBLE
        repos_recycler_view.visibility = View.GONE
        text_info.visibility = View.GONE
        val application = ArchiApplication.get(this)
        val githubService = application.githubService
        subscription = githubService!!.publicRepositories(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(object : Subscriber<List<Repository>>() {
                    override fun onCompleted() {
                        progress.visibility = View.GONE
                        if (repos_recycler_view.adapter.itemCount > 0) {
                            repos_recycler_view.requestFocus()
                            hideSoftKeyboard()
                            repos_recycler_view.visibility = View.VISIBLE
                        } else {
                            text_info.setText(R.string.text_empty_repos)
                            text_info.visibility = View.VISIBLE
                        }
                    }

                    override fun onError(error: Throwable) {
                        Log.e(TAG, "Error loading GitHub repos ", error)
                        progress!!.visibility = View.GONE
                        if (error is HttpException && error.code() == 404) {
                            text_info.setText(R.string.error_username_not_found)
                        } else {
                            text_info.setText(R.string.error_loading_repos)
                        }
                        text_info.visibility = View.VISIBLE
                    }

                    override fun onNext(repositories: List<Repository>) {
                        Log.i(TAG, "Repos loaded " + repositories)
                        val adapter = repos_recycler_view.adapter as RepositoryAdapter
                        adapter.setRepositories(repositories)
                        adapter.notifyDataSetChanged()
                    }
                })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val adapter = RepositoryAdapter()
        adapter.setCallback(object : RepositoryAdapter.Callback {
            override fun onItemClick(repository: Repository) {
                startActivity(RepositoryActivity.newIntent(this@MainActivity, repository))
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edit_text_username.windowToken, 0)
    }

    private val mHideShowButtonTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
            button_search.visibility = if (charSequence.isNotEmpty()) View.VISIBLE else View.GONE
        }

        override fun afterTextChanged(editable: Editable) {

        }
    }
}
