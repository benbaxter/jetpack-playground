package com.benjamingbaxter.swgoh.farmer.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SwgohApi {

  companion object {
    const val BASE_URL = "https://swgoh.gg/api/"
    const val CHARACTERS = "characters"

    private val retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()

    val swgohService = retrofit.create(SwgohService::class.java)
  }
}