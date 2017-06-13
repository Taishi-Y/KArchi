package com.taishi.kapp_mvvm.view

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.taishi.kapp_mvvm.R
import com.taishi.kapp_mvvm.databinding.RepositoryActivityBinding
import com.taishi.kapp_mvvm.model.Repository
import com.taishi.kapp_mvvm.viewmodel.RepositoryViewModel


class RepositoryActivity : AppCompatActivity() {

    private var binding: RepositoryActivityBinding? = null
    private var repositoryViewModel: RepositoryViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<RepositoryActivityBinding>(this, R.layout.repository_activity)
        setSupportActionBar(binding!!.toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val repository = intent.getParcelableExtra<Repository>(EXTRA_REPOSITORY)
        repositoryViewModel = RepositoryViewModel(this, repository)
        binding!!.viewModel = repositoryViewModel

        //Currently there is no way of setting an activity title using data binding
        title = repository.name
    }

    override fun onDestroy() {
        super.onDestroy()
        repositoryViewModel!!.destroy()
    }

    companion object {

        private val EXTRA_REPOSITORY = "EXTRA_REPOSITORY"

        fun newIntent(context: Context, repository: Repository): Intent {
            val intent = Intent(context, RepositoryActivity::class.java)
            intent.putExtra(EXTRA_REPOSITORY, repository)
            return intent
        }
    }
}
