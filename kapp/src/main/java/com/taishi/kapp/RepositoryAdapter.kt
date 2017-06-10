package com.taishi.kapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.taishi.kapp.model.Repository


class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {

    private var repositories: List<Repository>? = null
    private var callback: Callback? = null

    constructor() {
        this.repositories = emptyList<Repository>()
    }

    constructor(repositories: List<Repository>) {
        this.repositories = repositories
    }

    fun setRepositories(repositories: List<Repository>) {
        this.repositories = repositories
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_repo, parent, false)
        val viewHolder = RepositoryViewHolder(itemView)
        viewHolder.contentLayout.setOnClickListener {
            if (callback != null) {
                callback!!.onItemClick(viewHolder.repository!!)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = repositories!![position]
        val context = holder.titleTextView.context
        holder.repository = repository
        holder.titleTextView.text = repository.name
        holder.descriptionTextView.text = repository.description
        holder.watchersTextView.text = context.resources.getString(R.string.text_watchers, repository.watchers)
        holder.starsTextView.text = context.resources.getString(R.string.text_stars, repository.stars)
        holder.forksTextView.text = context.resources.getString(R.string.text_forks, repository.forks)
    }

    override fun getItemCount(): Int {
        return repositories!!.size
    }

    class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contentLayout: View
        var titleTextView: TextView
        var descriptionTextView: TextView
        var watchersTextView: TextView
        var starsTextView: TextView
        var forksTextView: TextView
        var repository: Repository? = null

        init {
            contentLayout = itemView.findViewById(R.id.layout_content)
            titleTextView = itemView.findViewById(R.id.text_repo_title) as TextView
            descriptionTextView = itemView.findViewById(R.id.text_repo_description) as TextView
            watchersTextView = itemView.findViewById(R.id.text_watchers) as TextView
            starsTextView = itemView.findViewById(R.id.text_stars) as TextView
            forksTextView = itemView.findViewById(R.id.text_forks) as TextView
        }
    }

    interface Callback {
        fun onItemClick(repository: Repository)
    }
}
