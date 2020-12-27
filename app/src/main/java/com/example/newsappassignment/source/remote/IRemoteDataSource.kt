package com.example.newsappassignment.source.remote

import com.example.newsappassignment.models.Model
import com.example.newsappassignment.source.Result

interface IRemoteDataSource {

    suspend fun getDataFromAPI(): Result<Model.Result>
}