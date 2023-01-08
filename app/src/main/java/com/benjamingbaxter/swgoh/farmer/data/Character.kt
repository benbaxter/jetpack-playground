package com.benjamingbaxter.swgoh.farmer.data

import com.google.gson.annotations.SerializedName

data class Character(
  val name: String,
  @SerializedName("base_id")
  val baseId: String,
  val pk: String,
  val url: String,
  val image: String,
  val power: Long,
  val description: String,
  val alignment: String,
  val role: String,
  @SerializedName("activate_shard_count")
  val activateShardCount: Long
)
