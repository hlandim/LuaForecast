package com.hlandim.luas.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "direction", strict = false)
data class Direction @JvmOverloads constructor(

    @field:Attribute(name = "name")
    var name: String? = null,

    @field:ElementList(name = "tram", inline = true)
    var trams: MutableList<Tram>? = null
)