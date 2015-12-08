package nl.brusque.pinky;

import android.util.Log;

import nl.brusque.pinky.android.AndroidFulfillable;
import nl.brusque.pinky.android.AndroidPinky;
import nl.brusque.pinky.android.AndroidPromise;
import nl.brusque.pinky.helper.PromiseTest;

public class MyAndroidTests extends PromiseTest {
    public void testMine() {
        AndroidPinky d = new AndroidPinky(getContext());



        d.getPromise().then(new AndroidFulfillable() {
            @Override
            public AndroidPromise.AndroidExecutionScope getExecutionScope() {
                return AndroidPromise.AndroidExecutionScope.BACKGROUND;
            }

            @Override
            public Object fulfill(Object o) throws Exception {
                Log.e("ONE", "ABCDE");

                return "ABCDE";
            }
        }).then(new AndroidFulfillable() {
            @Override
            public AndroidPromise.AndroidExecutionScope getExecutionScope() {
                return AndroidPromise.AndroidExecutionScope.UI;
            }

            @Override
            public Object fulfill(Object o) throws Exception {
                Log.e("TWO", "FGHIJ");

                return "FGHIJ";
            }
        });

        d.resolve("JUSTME");

        delay(3000);
    }
}