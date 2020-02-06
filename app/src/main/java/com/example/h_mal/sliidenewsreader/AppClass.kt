package com.example.h_mal.sliidenewsreader

import android.app.Application
import com.example.h_mal.sliidenewsreader.data.repositories.Repository
import com.example.h_mal.sliidenewsreader.data.db.AppDatabase
import com.example.h_mal.sliidenewsreader.data.network.MyApi
import com.example.h_mal.sliidenewsreader.data.network.NetworkConnectionInterceptor
import com.example.h_mal.sliidenewsreader.ui.login.LoginViewModelFactory
import com.example.h_mal.sliidenewsreader.ui.main.MainViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AppClass : Application(), KodeinAware{

    override val kodein = Kodein.lazy {
        import(androidXModule(this@AppClass))

        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { Repository(instance(),instance()) }
        bind() from provider { LoginViewModelFactory(instance()) }
        bind() from provider { MainViewModelFactory(instance()) }

    }

}