package com.example.luas.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "tram", strict = false)
data class Tram @JvmOverloads constructor(

    @field:Attribute(name = "destination")
    var destination: String? = null,

    @field:Attribute(name = "dueMins")
    var dueMin: String? = null
)