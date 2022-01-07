package com.baseio.kmm.domain.usecases.trendingrepos.utils

import com.baseio.kmm.data.local.GithubTrendingLocal
import com.baseio.kmm.data.local.GithubTrendingLocalImpl
import com.baseio.kmm.db.DriverFactory
import com.baseio.kmm.domain.usecases.trendingrepos.GetLocalReposUseCase
import com.baseio.kmm.domain.usecases.trendingrepos.SaveTrendingReposUseCase
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

suspend fun loadRequiredModules() {
    val dbDriver = DriverFactory().createInMemoryDriver()
    loadKoinModules(
        module {
            single<GithubTrendingLocal> {
                GithubTrendingLocalImpl(driver = dbDriver)
            }
            single { GetLocalReposUseCase(githubTrendingLocal = get()) }
            single { SaveTrendingReposUseCase(githubTrendingLocal = get()) }
        }
    )
}