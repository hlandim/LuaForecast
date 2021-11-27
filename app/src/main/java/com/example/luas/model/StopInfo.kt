package com.example.luas.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "stopInfo", strict = false)
data class StopInfo @JvmOverloads constructor(

    @field:Attribute(name = "stop")
    var stop: String? = null,

    @field:ElementList(name = "direction", inline = true)
    var directions: MutableList<Direction>? = null
)