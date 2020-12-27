package com.example.newsappassignment

import android.content.Context
import androidx.room.Room
import com.example.newsappassignment.source.FactsDataRepository
import com.example.newsappassignment.source.IFactsDataRepository
import com.example.newsappassignment.source.local.FactsDataBase
import com.example.newsappassignment.source.local.LocalDataSource
import com.example.newsappassignment.source.remote.RemoteDataSource

object ServiceLocator {
    private var database: FactsDataBase? = null

    @Volatile
    var factsDataRepository: IFactsDataRepository? = null

    fun provideFactsRepository(context: Context): IFactsDataRepository {
        synchronized(this) {
            return factsDataRepository ?: createFactsRepository(context)
        }
    }

    private fun createFactsRepository(context: Context): FactsDataRepository {
        val newRepo = FactsDataRepository(createLocalDataSource(context), RemoteDataSource(context))
        factsDataRepository = newRepo
        return newRepo
    }


    private fun createLocalDataSource(context: Context): LocalDataSource {
        val database = database ?: createDataBase(context)
        return LocalDataSource(database.factDao())
    }

    private fun createDataBase(context: Context): FactsDataBase {
        val result = Room.databaseBuilder(
            context.applicationContext, FactsDataBase::class.java, context.getString(
                R.string.repository
            )
        ).build()
        database = result
        return result
    }
}