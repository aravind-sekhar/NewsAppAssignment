package com.example.newsappassignment.source.remote

import com.example.newsappassignment.models.Model
import retrofit2.Call
import retrofit2.http.GET

interface FactAPIService {


    @GET("/s/2iodh4vg0eortkl/facts")
    fun getFactsCallObject(): Call<Model.Result>


}