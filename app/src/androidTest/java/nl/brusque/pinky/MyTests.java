package nl.brusque.pinky;

import android.util.Log;

import junit.framework.Assert;

import nl.brusque.pinky.helper.PromiseTest;
import nl.brusque.pinky.promise.Fulfillable;
import nl.brusque.pinky.promise.Rejectable;

public class MyTests extends PromiseTest {
    public void testMine() {
        Pinky d = deferred();

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