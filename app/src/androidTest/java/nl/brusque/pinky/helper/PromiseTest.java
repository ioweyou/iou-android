package nl.brusque.pinky.helper;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import nl.brusque.pinky.IPromise;
import nl.brusque.pinky.IThenable;
import nl.brusque.pinky.Pinky;

public class PromiseTest extends ApplicationTestCase<Application> {
    public PromiseTest() {
        super(Application.class);
    }

    public Pinky deferred() {
        return new Pinky();
    }

    public IPromise resolved() {
        return resolved(null);
    }

    public IPromise resolved(Object o) {
        return deferred().getPromise().resolve(o);
    }

    public IThenable rejected() {
        return rejected("");
    }

    public IThenable rejected(Object o) {
        return deferred().getPromise().reject(o);
    }

    public void describe(String description, Runnable runnable) {
        Log.i("PromiseTest", description);
        runnable.run();
    }

    public void specify(String description, Runnable test) {
        Log.i("PromiseTest", "  " + description);
        test.run();
    }

    public void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testFulfilled(final Object value, final Testable test) {
//        specify("already-fulfilled", new Runnable() {
//            @Override
//            public void run() {
//                test.setPromise(resolved(value));
//
//                test.run();
//            }
//        });
//
//        specify("immediately-fulfilled", new Runnable() {
//            @Override
//            public void run() {
//                Pinky d = deferred();
//                test.setPromise(d.getPromise());
//                test.run();
//                d.resolve(value);
//            }
//        });

        specify("eventually-fulfilled", new Runnable() {
            @Override
            public void run() {
                Pinky d = deferred();
                test.setPromise(d.getPromise());
                test.run();
                delay(50);
                d.resolve(value);
            }
        });
    }

    public FulfillableSpy fulfillableStub() {
        return new FulfillableSpy();
    }

    public RejectableSpy rejectableStub() {
        return new RejectableSpy();
    }
}