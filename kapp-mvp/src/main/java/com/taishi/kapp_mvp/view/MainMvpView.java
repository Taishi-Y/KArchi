package com.taishi.kapp_mvp.view;

import com.taishi.kapp_mvp.Repository;

import java.util.List;


public interface MainMvpView extends MvpView {

    void showRepositories(List<Repository> repositories);

    void showMessage(int stringId);

    void showProgressIndicator();
}
