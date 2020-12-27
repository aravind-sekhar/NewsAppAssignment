package com.example.newsappassignment.source

import com.example.newsappassignment.models.Model
import com.example.newsappassignment.source.remote.IRemoteDataSource
import java.lang.Exception

class FakeRemoteDataSource (
    var title : String,
    var isConnected : Boolean,
    var response : ArrayList<Model.Row>
) : IRemoteDataSource {
    override suspend fun getDataFromAPI(): Result<Model.Result> {
        return if(isConnected) {
            Result.Success(Model.Result(title, response))
        }else {
            Result.Error(Exception("Not Loading"))
        }
    }

}