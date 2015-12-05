package nl.brusque.pinky;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import nl.brusque.pinky.promise.Promise;

public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testWhen() {
        IPromise testPromise = Pinky.resolve(1);

        testPromise
            .then(new Promise() {
                @Override
                public Object run(Object o) {
                    Log.d("PIPE", o.toString());

                    return "MY PIPE";
                }
            })
            .then(new Promise() {
                @Override
                public Object run(Object o) {
                    Log.d("HALLO", o.toString());

                    return o;
                }
            });

        Log.d("OK", "KLAAR");

    }
}