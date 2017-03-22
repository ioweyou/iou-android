package nl.brusque.iou_android_samples

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import nl.brusque.iou.AndroidPromise
import nl.brusque.iou.IOUAndroid

class KotlinActivity : AppCompatActivity() {
    companion object {
        const val TAG: String = "KotlinActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val iou = IOUAndroid<String>(this)

        iou.`when`("http://android.com")
        .then { url ->
            HttpClient().get(url)
        }.then { response ->
            Log.i(TAG, response)

            response
        }.then(AndroidPromise.ExecutionScope.UI) { response ->
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show()

            response
        }.fail { error ->
            Log.e(TAG, "Failed: $error")
        }
    }
}
