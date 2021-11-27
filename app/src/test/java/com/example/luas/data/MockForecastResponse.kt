package com.example.luas.data

import com.example.luas.model.StopInfo
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class MockForecastResponse {

    companion object {
        val MARLBOROUGH = readFileFromResources("marlborough_response.xml")
        val STILLORGAN = readFileFromResources("stillorgan_response.xml")

        @Throws(IOException::class)
        fun readFileFromResources(fileName: String): StopInfo {
            var inputStream: InputStream? = null
            try {
                inputStream =
                    MockForecastResponse::class.java.classLoader?.getResourceAsStream(
                        fileName
                    )
                val builder = StringBuilder()
                val reader = BufferedReader(InputStreamReader(inputStream))

                var str: String? = reader.readLine()
                while (str != null) {
                    builder.append(str)
                    str = reader.readLine()
                }
                val serializer: Serializer = Persister()
                return serializer.read(StopInfo::class.java, builder.toString())
            } finally {
                inputStream?.close()
            }
        }

    }
}