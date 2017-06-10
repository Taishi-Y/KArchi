package com.taishi.kapp_mvvm.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import com.taishi.kapp_mvvm.R;
import com.taishi.kapp_mvvm.model.Repository;
import com.taishi.kapp_mvvm.view.RepositoryActivity;




/**
 * View model for each item in the repositories RecyclerView
 */
public class ItemRepoViewModel extends BaseObservable implements ViewModel {

    private Repository repository;
    private Context context;

    public ItemRepoViewModel(Context context, Repository repository) {
        this.repository = repository;
        this.context = context;
    }

    public String getName() {
        return repository.name;
    }

    public String getDescription() {
        return repository.description;
    }

    public String getStars() {
        return context.getString(R.string.text_stars, repository.stars);
    }

    public String getWatchers() {
        return context.getString(R.string.text_watchers, repository.watchers);
    }

    public String getForks() {
        return context.getString(R.string.text_forks, repository.forks);
    }

    public void onItemClick(View view) {
        context.startActivity(RepositoryActivity.newIntent(context, repository));
    }

    // Allows recycling ItemRepoViewModels within the recyclerview adapter
    public void setRepository(Repository repository) {
        this.repository = repository;
        notifyChange();
    }

    @Override
    public void destroy() {
        //In this case destroy doesn't need to do anything because there is not async calls
    }

}
