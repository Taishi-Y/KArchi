package com.taishi.kapp_mvvm.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.inputmethod.InputMethodManager

import com.taishi.kapp_mvvm.R
import com.taishi.kapp_mvvm.RepositoryAdapter
import com.taishi.kapp_mvvm.databinding.MainActivityBinding
import com.taishi.kapp_mvvm.model.Repository
import com.taishi.kapp_mvvm.viewmodel.MainViewModel


class MainActivity : AppCompatActivity(), MainViewModel.DataListener {

    private var binding: MainActivityBinding? = null
    private var mainViewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)
        mainViewModel = MainViewModel(this, this)
        binding!!.viewModel = mainViewModel
        setSupportActionBar(binding!!.toolbar)
        setupRecyclerView(binding!!.reposRecyclerView)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel!!.destroy()
    }

    override fun onRepositoriesChanged(repositories: List<Repository>) {
        val adapter = binding!!.reposRecyclerView.adapter as RepositoryAdapter
        adapter.setRepositories(repositories)
        adapter.notifyDataSetChanged()
        hideSoftKeyboard()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val adapter = RepositoryAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding!!.editTextUsername.windowToken, 0)
    }

}
