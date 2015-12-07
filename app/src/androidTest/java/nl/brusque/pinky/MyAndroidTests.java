package nl.brusque.pinky;

import android.util.Log;

import nl.brusque.pinky.android.AndroidPinky;
import nl.brusque.pinky.helper.PromiseTest;
import nl.brusque.pinky.promise.Fulfillable;

public class MyAndroidTests extends PromiseTest {
    public void testMine() {
        AndroidPinky d = new AndroidPinky(getContext());

        d.getPromise().then(new Fulfillable() {
            @Override
            public Object fulfill(Object o) throws Exception {
                Log.i("ONE", "ABCDE");

                return "ABCDE";
            }
        }).then(new Fulfillable() {
            @Override
            public Object fulfill(Object o) throws Exception {
                Log.i("TWO", "FGHIJ");

                return "FGHIJ";
            }
        });

        d.resolve("JUSTME");
    }
}