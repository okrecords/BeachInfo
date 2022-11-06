package com.okre.beachinfo

import retrofit2.Call
import retrofit2.http.GET

interface BeachService {
    @GET("getBeachCongestionApi.do")
    fun getBeachInfo() : Call<Beach>
}