package com.taishi.kapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.squareup.picasso.Picasso
import com.taishi.kapp.model.Repository
import com.taishi.kapp.model.User
import kotlinx.android.synthetic.main.activity_repository.*
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers


class RepositoryActivity : AppCompatActivity() {

    private var subscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val repository = intent.getParcelableExtra<Repository>(EXTRA_REPOSITORY)
        bindRepositoryData(repository)
        loadFullUser(repository.owner!!.url!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (subscription != null) subscription!!.unsubscribe()
    }

    private fun bindRepositoryData(repository: Repository) {
        title = repository.name
        text_repo_description.text = repository.description
        text_homepage.text = repository.homepage
        text_homepage.visibility = if (repository.hasHomepage()) View.VISIBLE else View.GONE
        text_language.text = getString(R.string.text_language, repository.language)
        text_language.visibility = if (repository.hasLanguage()) View.VISIBLE else View.GONE
        text_fork.visibility = if (repository.isFork) View.VISIBLE else View.GONE
        //Preload image for user because we already have it before loading the full user
        Picasso.with(this)
                .load(repository.owner!!.avatarUrl)
                .placeholder(R.drawable.placeholder)
                .into(image_owner)
    }

    private fun bindOwnerData(owner: User) {
        text_owner_name.text = owner.name
        text_owner_email.text = owner.email
        text_owner_email.visibility = if (owner.hasEmail()) View.VISIBLE else View.GONE
        text_owner_location.text = owner.location
        text_owner_location.visibility = if (owner.hasLocation()) View.VISIBLE else View.GONE
    }


    private fun loadFullUser(url: String) {
        val application = ArchiApplication[this]
        val githubService = application.githubService
        subscription = githubService!!.userFromUrl(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe { user ->
                    Log.i(TAG, "Full user data loaded " + user)
                    bindOwnerData(user)
                    layout_owner.visibility = View.VISIBLE
                }
    }

    companion object {

        private val EXTRA_REPOSITORY = "EXTRA_REPOSITORY"
        private val TAG = "RepositoryActivity"

        fun newIntent(context: Context, repository: Repository): Intent {
            val intent = Intent(context, RepositoryActivity::class.java)
            intent.putExtra(EXTRA_REPOSITORY, repository)
            return intent
        }
    }
}
