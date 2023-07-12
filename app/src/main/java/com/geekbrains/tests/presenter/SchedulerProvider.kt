package com.geekbrains.tests.presenter

import io.reactivex.rxjava3.core.Scheduler

internal interface SchedulerProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}