package com.taishi.kapp_mvvm.viewmodel

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.taishi.kapp_mvvm.ArchiApplication
import com.taishi.kapp_mvvm.R
import com.taishi.kapp_mvvm.model.Repository
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers


/**
 * ViewModel for the RepositoryActivity
 */
class RepositoryViewModel(private var context: Context?, private val repository: Repository) : ViewModel {
    private var subscription: Subscription? = null

    var ownerName: ObservableField<String>
    var ownerEmail: ObservableField<String>
    var ownerLocation: ObservableField<String>
    var ownerEmailVisibility: ObservableInt
    var ownerLocationVisibility: ObservableInt
    var ownerLayoutVisibility: ObservableInt

    init {
        this.ownerName = ObservableField<String>()
        this.ownerEmail = ObservableField<String>()
        this.ownerLocation = ObservableField<String>()
        this.ownerLayoutVisibility = ObservableInt(View.INVISIBLE)
        this.ownerEmailVisibility = ObservableInt(View.VISIBLE)
        this.ownerLocationVisibility = ObservableInt(View.VISIBLE)
        // Trigger loading the rest of the user data as soon as the view model is created.
        // It's odd having to trigger this from here. Cases where accessing to the data model
        // needs to happen because of a change in the Activity/Fragment lifecycle
        // (i.e. an activity created) don't work very well with this MVVM pattern.
        // It also makes this class more difficult to test. Hopefully a better solution will be found
        loadFullUser(repository.owner!!.url!!)
    }

    val description: String
        get() = repository.description!!

    val homepage: String
        get() = repository.homepage!!

    val homepageVisibility: Int
        get() = if (repository.hasHomepage()) View.VISIBLE else View.GONE

    val language: String
        get() = context!!.getString(R.string.text_language, repository.language)

    val languageVisibility: Int
        get() = if (repository.hasLanguage()) View.VISIBLE else View.GONE

    val forkVisibility: Int
        get() = if (repository.isFork) View.VISIBLE else View.GONE

    val ownerAvatarUrl: String
        get() = repository.owner!!.avatarUrl!!

    override fun destroy() {
        this.context = null
        if (subscription != null && !subscription!!.isUnsubscribed) subscription!!.unsubscribe()
    }

    private fun loadFullUser(url: String) {
        val application = ArchiApplication[context!!]
        val githubService = application.githubService
        subscription = githubService!!.userFromUrl(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe { user ->
                    Log.i(TAG, "Full user data loaded " + user)
                    ownerName.set(user.name)
                    ownerEmail.set(user.email)
                    ownerLocation.set(user.location)
                    ownerEmailVisibility.set(if (user.hasEmail()) View.VISIBLE else View.GONE)
                    ownerLocationVisibility.set(if (user.hasLocation()) View.VISIBLE else View.GONE)
                    ownerLayoutVisibility.set(View.VISIBLE)
                }
    }

    companion object {

        private val TAG = "RepositoryViewModel"

        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, imageUrl: String) {
            Picasso.with(view.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(view)
        }
    }
}
