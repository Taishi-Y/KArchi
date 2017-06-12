package com.taishi.kapp_mvp

import android.app.Application
import android.content.Context

import rx.Scheduler
import rx.schedulers.Schedulers

class ArchiApplication : Application() {

    //For setting mocks during testing
    var githubService: GithubService? = null
        get() {
            if (field == null) {
                this.githubService = GithubService.Factory.create()
            }
            return field
        }
    private var defaultSubscribeScheduler: Scheduler? = null

    fun defaultSubscribeScheduler(): Scheduler {
        if (defaultSubscribeScheduler == null) {
            defaultSubscribeScheduler = Schedulers.io()
        }
        return defaultSubscribeScheduler!!
    }

    //User to change scheduler from tests
    fun setDefaultSubscribeScheduler(scheduler: Scheduler) {
        this.defaultSubscribeScheduler = scheduler
    }

    companion object {

        operator fun get(context: Context): ArchiApplication {
            return context.applicationContext as ArchiApplication
        }
    }
}
