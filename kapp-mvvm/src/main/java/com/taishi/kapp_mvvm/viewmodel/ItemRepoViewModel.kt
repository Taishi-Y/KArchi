package com.taishi.kapp_mvvm.viewmodel

import android.content.Context
import android.databinding.BaseObservable
import android.view.View

import com.taishi.kapp_mvvm.R
import com.taishi.kapp_mvvm.model.Repository
import com.taishi.kapp_mvvm.view.RepositoryActivity


/**
 * View model for each item in the repositories RecyclerView
 */
class ItemRepoViewModel(private val context: Context, private var repository: Repository?) : BaseObservable(), ViewModel {

    val name: String
        get() = repository!!.name!!

    val description: String
        get() = repository!!.description!!

    val stars: String
        get() = context.getString(R.string.text_stars, repository!!.stars)

    val watchers: String
        get() = context.getString(R.string.text_watchers, repository!!.watchers)

    val forks: String
        get() = context.getString(R.string.text_forks, repository!!.forks)

    fun onItemClick(view: View) {
        context.startActivity(RepositoryActivity.newIntent(context, repository!!))
    }

    // Allows recycling ItemRepoViewModels within the recyclerview adapter
    fun setRepository(repository: Repository) {
        this.repository = repository
        notifyChange()
    }

    override fun destroy() {
        //In this case destroy doesn't need to do anything because there is not async calls
    }

}
