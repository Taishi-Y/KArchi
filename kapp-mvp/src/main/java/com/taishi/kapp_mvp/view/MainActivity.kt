package com.taishi.kapp_mvp.view

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.taishi.kapp_mvp.R
import com.taishi.kapp_mvp.Repository
import com.taishi.kapp_mvp.RepositoryAdapter
import com.taishi.kapp_mvp.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainMvpView {
    override val context: Context
        get() = this //To change initializer of created properties use File | Settings | File Templates.

    private var presenter: MainPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Set up presenter
        presenter = MainPresenter()
        presenter?.attachView(this)
        setContentView(R.layout.activity_main)
        //Set up ToolBar
        setSupportActionBar(toolbar)
        //Set up RecyclerView
        setupRecyclerView(repos_recycler_view)
        // Set up search button
        button_search.setOnClickListener { presenter?.loadRepositories(edit_text_username!!.text.toString()) }
        //Set up username EditText
        edit_text_username.addTextChangedListener(mHideShowButtonTextWatcher)
        edit_text_username.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter?.loadRepositories(edit_text_username!!.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
    }

    // MainMvpView interface methods implementation

    override fun onDestroy() {
        presenter!!.detachView()
        super.onDestroy()
    }

    override fun showRepositories(repositories: List<Repository>) {
        val adapter = repos_recycler_view.adapter as RepositoryAdapter
        adapter.setRepositories(repositories)
        adapter.notifyDataSetChanged()
        repos_recycler_view.requestFocus()
        hideSoftKeyboard()
        progress.visibility = View.INVISIBLE
        text_info.visibility = View.INVISIBLE
        repos_recycler_view.visibility = View.VISIBLE
    }

    override fun showMessage(stringId: Int) {
        progress.visibility = View.INVISIBLE
        text_info.visibility = View.VISIBLE
        repos_recycler_view.visibility = View.INVISIBLE
        text_info.text = getString(stringId)
    }

    override fun showProgressIndicator() {
        progress.visibility = View.VISIBLE
        text_info.visibility = View.INVISIBLE
        repos_recycler_view.visibility = View.INVISIBLE
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val adapter = RepositoryAdapter()
        adapter.setCallback(object : RepositoryAdapter.Callback{
            override fun onItemClick(repository: Repository) {
                startActivity(RepositoryActivity.newIntent(this@MainActivity, repository))
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edit_text_username!!.windowToken, 0)
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
