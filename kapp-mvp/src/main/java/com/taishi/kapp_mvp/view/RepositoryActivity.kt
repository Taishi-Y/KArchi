package com.taishi.kapp_mvp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.taishi.kapp_mvp.R
import com.taishi.kapp_mvp.Repository
import com.taishi.kapp_mvp.User
import com.taishi.kapp_mvp.presenter.RepositoryPresenter


class RepositoryActivity : AppCompatActivity(), RepositoryMvpView {

    private var toolbar: Toolbar? = null
    private var descriptionText: TextView? = null
    private var homepageText: TextView? = null
    private var languageText: TextView? = null
    private var forkText: TextView? = null
    private var ownerNameText: TextView? = null
    private var ownerEmailText: TextView? = null
    private var ownerLocationText: TextView? = null
    private var ownerImage: ImageView? = null
    private var ownerLayout: View? = null

    private var presenter: RepositoryPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = RepositoryPresenter()
        presenter!!.attachView(this)

        setContentView(R.layout.activity_repository)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        descriptionText = findViewById(R.id.text_repo_description) as TextView
        homepageText = findViewById(R.id.text_homepage) as TextView
        languageText = findViewById(R.id.text_language) as TextView
        forkText = findViewById(R.id.text_fork) as TextView
        ownerNameText = findViewById(R.id.text_owner_name) as TextView
        ownerEmailText = findViewById(R.id.text_owner_email) as TextView
        ownerLocationText = findViewById(R.id.text_owner_location) as TextView
        ownerImage = findViewById(R.id.image_owner) as ImageView
        ownerLayout = findViewById(R.id.layout_owner)

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val repository = intent.getParcelableExtra<Repository>(EXTRA_REPOSITORY)
        bindRepositoryData(repository)
        presenter!!.loadOwner(repository.owner!!.url)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.detachView()
    }

    override val context: Context
        get() = this

    override fun showOwner(owner: User) {
        ownerNameText!!.text = owner.name
        ownerEmailText!!.text = owner.email
        ownerEmailText!!.visibility = if (owner.hasEmail()) View.VISIBLE else View.GONE
        ownerLocationText!!.text = owner.location
        ownerLocationText!!.visibility = if (owner.hasLocation()) View.VISIBLE else View.GONE
        ownerLayout!!.visibility = View.VISIBLE
    }

    private fun bindRepositoryData(repository: Repository) {
        title = repository.name
        descriptionText!!.text = repository.description
        homepageText!!.text = repository.homepage
        homepageText!!.visibility = if (repository.hasHomepage()) View.VISIBLE else View.GONE
        languageText!!.text = getString(R.string.text_language, repository.language)
        languageText!!.visibility = if (repository.hasLanguage()) View.VISIBLE else View.GONE
        forkText!!.visibility = if (repository.isFork) View.VISIBLE else View.GONE
        //Preload image for user because we already have it before loading the full user
        Picasso.with(this)
                .load(repository.owner!!.avatarUrl)
                .placeholder(R.drawable.placeholder)
                .into(ownerImage)
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
