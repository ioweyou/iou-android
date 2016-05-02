package nl.brusque.iou_android_samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import nl.brusque.iou.AndroidPromise;
import nl.brusque.iou.AndroidThenCallable;
import nl.brusque.iou.IOUAndroid;

public class JavaActivity extends AppCompatActivity {
    static String TAG = "JavaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IOUAndroid<String> iou = new IOUAndroid<>(this);

        iou.when("http://android.com")
        .then(new AndroidThenCallable<String, String>(AndroidPromise.ExecutionScope.BACKGROUND) {
            @Override
            public String apply(String url) throws Exception {
                return new HttpClient().get(url);
            }
        }).then(new AndroidThenCallable<String, Void>(AndroidPromise.ExecutionScope.UI) {
            @Override
            public Void apply(String response) throws Exception {
                Log.i(TAG, response);

                return null;
            }
        }).fail(new AndroidThenCallable<Object, Object>() {
            @Override
            public Object apply(Object error) throws Exception {
                Log.e(TAG, String.format("Error: %s", error));

                return null;
            }
        });
    }
}
