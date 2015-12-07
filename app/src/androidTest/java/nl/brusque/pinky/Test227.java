package nl.brusque.pinky;

import junit.framework.Assert;

import java.util.Date;
import java.util.HashMap;

import nl.brusque.pinky.helper.PromiseTest;
import nl.brusque.pinky.helper.Testable;
import nl.brusque.pinky.promise.Fulfillable;
import nl.brusque.pinky.promise.Promise;
import nl.brusque.pinky.promise.Rejectable;

public class Test227 extends PromiseTest {
    public void test227ThenMustReturnAPromise() {
        describe("2.2.7: `then` must return a promise: `promise2 = promise1.then(onFulfilled, onRejected)`", new Runnable() {
            final String dummy     = "DUMMY";
            final String other     = "other";
            final String sentinel  = "sentinel";
            final String sentinel2 = "sentinel2";
            final String sentinel3 = "sentinel3";

            final HashMap<String, Object> reasons = new HashMap<String, Object>() {{
                put("`null`", null);
                put("`false`", false);
                put("`0`", 0);
                put("an error", new Error());
                put("a date", new Date());
                put("an object", new Object());
                put("an always-pending thenable", new IThenable() {
                    @Override
                    public IPromise then() {
                        return new Promise();
                    }

                    @Override
                    public IPromise then(Object onFulfilled) {
                        return new Promise();
                    }

                    @Override
                    public IPromise then(Object onFulfilled, Object onRejected) {
                        return new Promise();
                    }
                });
                put("a fulfilled promise", resolved(dummy));
                put("a rejected promise", resolved(dummy));
            }};



            @Override
            public void run() {
                specify("is a promise", new Runnable() {
                    @Override
                    public void run() {
                        IPromise promise1 = deferred().getPromise();
                        Object promise2 = promise1.then();

                        Assert.assertFalse("AndroidPromise should not be null", promise2==null);
                    }
                });

                describe("2.2.7.2: If either `onFulfilled` or `onRejected` throws an exception `e`, `promise2` must be rejected with `e` as the reason.", new Runnable() {
                    private void testReason(final Object expectedReason, String stringRepresentation) {
                        describe(String.format("The reason is %s", stringRepresentation), new Runnable() {

                            @Override
                            public void run() {
                                testFulfilled(dummy, new Testable() {
                                    IPromise promise1 = getPromise();

                                    @Override
                                    public void run() {
                                        IThenable promise2 = promise1.then(new Fulfillable() {
                                            @Override
                                            public Object fulfill(Object o) throws Exception {
                                                throw new Exception(String.valueOf(expectedReason));
                                            }
                                        });

                                        promise2.then(null, new Rejectable() {
                                            @Override
                                            public Object reject(Object o) throws Exception {
                                                Assert.assertEquals("Incorrect reaason", expectedReason, o);

                                                return null;
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void run() {
                        for (String stringRepresentation : reasons.keySet()) {
                            testReason(reasons.get(stringRepresentation), stringRepresentation);
                        }
                    }
                });

            }
        });


    }
}