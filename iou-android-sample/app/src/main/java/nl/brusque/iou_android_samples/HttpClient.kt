package nl.brusque.iou_android_samples

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpClient {
    fun get(url: String) : String {
        val url = URL(url)
        val con = url.openConnection() as HttpURLConnection

        return readStream(con.inputStream)
    }

    private fun readStream(inputStream: InputStream): String {
        val sb = StringBuilder()
        try {
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.forEachLine {
                    l -> sb.append(l)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return sb.toString()
    }

}