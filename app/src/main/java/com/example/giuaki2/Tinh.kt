package com.example.giuaki2

import java.io.Serializable

data class Tinh(
    var resouerceId: Int,
    val name: String,
    val population: String,
    val description: String
) : Serializable