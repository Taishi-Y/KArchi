package com.taishi.kapp_mvvm

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.taishi.kapp_mvvm.databinding.ItemRepoBinding
import com.taishi.kapp_mvvm.model.Repository
import com.taishi.kapp_mvvm.viewmodel.ItemRepoViewModel


class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {

    private var repositories: List<Repository>? = null

    constructor() {
        this.repositories = emptyList<Repository>()
    }

    constructor(repositories: List<Repository>) {
        this.repositories = repositories
    }

    fun setRepositories(repositories: List<Repository>) {
        this.repositories = repositories
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val binding = DataBindingUtil.inflate<ItemRepoBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_repo,
                parent,
                false)
        return RepositoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bindRepository(repositories!![position])
    }

    override fun getItemCount(): Int {
        return repositories!!.size
    }

    class RepositoryViewHolder(internal val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.cardView) {

        internal fun bindRepository(repository: Repository) {
            if (binding.viewModel == null) {
                binding.viewModel = ItemRepoViewModel(itemView.context, repository)
            } else {
                binding.viewModel.setRepository(repository)
            }
        }
    }
}
