package nl.brusque.pinky;

import android.util.Log;

import junit.framework.Assert;

import java.util.Date;
import java.util.HashMap;

import nl.brusque.pinky.helper.PromiseTest;
import nl.brusque.pinky.helper.Testable;
import nl.brusque.pinky.promise.Fulfillable;
import nl.brusque.pinky.promise.Promise;
import nl.brusque.pinky.promise.Rejectable;
import nl.brusque.pinky.promise.TypeError;

public class Test231 extends PromiseTest {
    public void test231IfPromiseAndXReferToTheSameObjectRejectPromiseWithATypeError() {
        describe("2.3.1: If `promise` and `x` refer to the same object, reject `promise` with a `TypeError' as the reason.", new Runnable() {
            final String dummy     = "DUMMY";

            @Override
            public void run() {
                specify("via return from a fulfilled promise", new Runnable() {
                    @Override
                    public void run() {
                        final IThenable promise = resolved(dummy);

                        promise.then(new Fulfillable() {
                            @Override
                            public Object fulfill(Object o) throws Exception {
                                return promise;
                            }
                        });

                        promise.then(null, new Rejectable() {
                            @Override
                            public Object reject(Object o) throws Exception {
                                Assert.assertTrue("Object should be TypeError", o instanceof TypeError);

                                return null;
                            }
                        });
                    }
                });

                specify("via return from a rejected promise", new Runnable() {
                    @Override
                    public void run() {
                        final IThenable promise = rejected(dummy);

                        promise.then(null, new Rejectable() {
                            @Override
                            public Object reject(Object o) throws Exception {
                                return promise;
                            }
                        });

                        promise.then(null, new Rejectable() {
                            @Override
                            public Object reject(Object o) throws Exception {
                                Assert.assertTrue("Object should be TypeError", o instanceof TypeError);

                                return null;
                            }
                        });
                    }
                });
            }
        });

        delay(5000);
    }
}