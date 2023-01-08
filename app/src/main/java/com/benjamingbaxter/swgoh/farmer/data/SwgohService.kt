package com.benjamingbaxter.swgoh.farmer.data

import retrofit2.Call
import retrofit2.http.GET

interface SwgohService {

  @GET(SwgohApi.CHARACTERS)
  fun getCharacters() : Call<List<Character>>

}