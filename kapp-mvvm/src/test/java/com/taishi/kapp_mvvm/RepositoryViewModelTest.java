package com.taishi.kapp_mvvm;

import android.view.View;

import com.taishi.kapp_mvvm.model.GithubService;
import com.taishi.kapp_mvvm.model.Repository;
import com.taishi.kapp_mvvm.model.User;
import com.taishi.kapp_mvvm.viewmodel.RepositoryViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import jp.nagisa.kapp_mvvm.BuildConfig;
import jp.nagisa.kapp_mvvm.R;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RepositoryViewModelTest {

    GithubService githubService;
    ArchiApplication application;
    Repository repository;
    User owner;
    RepositoryViewModel viewModel;

    @Before
    public void setUp() {
        githubService = mock(GithubService.class);
        application = (ArchiApplication) RuntimeEnvironment.application;
        // Mock the retrofit service so we don't call the API directly
        application.setGithubService(githubService);
        // Change the default subscribe schedulers so all observables
        // will now run on the same thread
        application.setDefaultSubscribeScheduler(Schedulers.immediate());
        // Default behaviour is to load a mock owner when the view model is instantiated
        repository = MockModelFabric.newRepository("Repository");
        owner = MockModelFabric.newUser("owner");
        when(githubService.userFromUrl(repository.owner.url))
                .thenReturn(Observable.just(owner));
        viewModel = new RepositoryViewModel(application, repository);
    }

    @Test
    public void shouldGetDescription() {
        assertEquals(repository.description, viewModel.getDescription());
    }

    @Test
    public void shouldGetHomepage() {
        assertEquals(repository.homepage, viewModel.getHomepage());
    }

    @Test
    public void shouldGetLanguage() {
        assertEquals(application.getString(R.string.text_language, repository.language),
                viewModel.getLanguage());
    }

    @Test
    public void shouldReturnHomepageVisibilityGone() {
        repository.homepage = null;
        assertEquals(View.GONE, viewModel.getHomepageVisibility());
    }

    @Test
    public void shouldReturnLanguageVisibilityGone() {
        repository.language = null;
        assertEquals(View.GONE, viewModel.getLanguageVisibility());
    }

    @Test
    public void shouldReturnForkVisibilityVisible() {
        repository.fork = true;
        assertEquals(View.VISIBLE, viewModel.getForkVisibility());
    }

    @Test
    public void shouldReturnForkVisibilityGone() {
        repository.fork = false;
        assertEquals(View.GONE, viewModel.getForkVisibility());
    }

    @Test
    public void shouldLoadFullOwnerOnInstantiation() {
        assertEquals(owner.name, viewModel.ownerName.get());
        assertEquals(owner.email, viewModel.ownerEmail.get());
        assertEquals(owner.location, viewModel.ownerLocation.get());
        assertEquals(View.VISIBLE, viewModel.ownerEmailVisibility.get());
        assertEquals(View.VISIBLE, viewModel.ownerLocationVisibility.get());
        assertEquals(View.VISIBLE, viewModel.ownerLayoutVisibility.get());
    }
}
