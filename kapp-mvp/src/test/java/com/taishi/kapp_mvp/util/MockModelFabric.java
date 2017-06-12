package com.taishi.kapp_mvp.util;

import com.taishi.kapp_mvp.Repository;
import com.taishi.kapp_mvp.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MockModelFabric {

    public static List<Repository> newListOfRepositories(int numRepos) {
        List<Repository> repositories = new ArrayList<>(numRepos);
        for (int i = 0; i < numRepos; i++) {
            repositories.add(newRepository("Repo " + i));
        }
        return repositories;
    }

    public static Repository newRepository(String name) {
        Random random = new Random();
        Repository repository = new Repository();
        repository.setName(name);
        repository.setId(random.nextInt(10000));
        repository.setDescription("Description for " + name);
        repository.setWatchers(random.nextInt(100));
        repository.setForks(random.nextInt(100));
        repository.setStars(random.nextInt(100));
        repository.setOwner(newUser("User-" + name));
        return repository;
    }

    public static User newUser(String name) {
        Random random = new Random();
        User user = new User();
        user.setId(random.nextInt(10000));
        user.setName(name);
        user.setEmail(name + "@email.com");
        user.setLocation("Location of " + name);
        user.setUrl("http://user.com/" + name);
        user.setAvatarUrl("http://user.com/image/" + name);
        return user;
    }
}
