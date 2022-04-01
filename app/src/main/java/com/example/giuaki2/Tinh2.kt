package com.example.giuaki2

import android.net.Uri
import java.io.Serializable
import java.net.URI

data class Tinh2(
    var id: String? = null,
    var resouerceUrl: String? = "",
    var name: String? = "",
    var population: String? = "",
    var description: String? = "",
) : Serializable