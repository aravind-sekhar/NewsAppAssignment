package com.example.newsappassignment

import android.app.Application
import com.example.newsappassignment.source.IFactsDataRepository

class MyApplication : Application() {

    val factsDataRepository: IFactsDataRepository
        get() = ServiceLocator.provideFactsRepository(this)
}