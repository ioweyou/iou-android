package nl.brusque.pinky;

import junit.framework.Assert;

import nl.brusque.pinky.helper.PromiseTest;
import nl.brusque.pinky.promise.Fulfillable;
import nl.brusque.pinky.promise.Rejectable;

public class Test213 extends PromiseTest {
    public void test2131WhenRejectedAPromiseMustNotTransitionToAnyOtherState() {
        describe("2.1.3.1: When rejected, a promise: must not transition to any other state.", new Runnable() {
            @Override
            public void run() {
                final String dummy = "DUMMY";

                specify("trying to reject then immediately fulfill", new Runnable() {
                    @Override
                    public void run() {
                        Pinky d = deferred();

                        final boolean[] onRejectedCalled = {false};

                        d.getPromise().then(new Fulfillable() {

                            @Override
                            public Object fulfill(Object o) {
                                Assert.assertEquals("onFulfilled should not have been called", false, onRejectedCalled[0]);

                                return o;
                            }
                        }, new Rejectable() {
                            @Override
                            public Object reject(Object o) {
                                onRejectedCalled[0] = true;

                                return o;
                            }
                        });

                        d.reject(dummy);
                        d.resolve(dummy);
                        delay(100);
                    }
                });

                specify("trying to reject then fulfill, delayed", new Runnable() {
                    @Override
                    public void run() {
                        Pinky d = deferred();

                        final boolean[] onRejectedCalled = {false};

                        d.getPromise().then(new Fulfillable() {

                            @Override
                            public Object fulfill(Object o) {
                                Assert.assertEquals("onFulfilled should not have been called", false, onRejectedCalled[0]);

                                return o;
                            }
                        }, new Rejectable() {
                            @Override
                            public Object reject(Object o) {
                                onRejectedCalled[0] = true;

                                return o;
                            }
                        });

                        delay(50);
                        d.reject(dummy);
                        d.resolve(dummy);
                        delay(100);
                    }
                });

                specify("trying to reject immediately then fulfill delayed", new Runnable() {
                    @Override
                    public void run() {
                        Pinky d = deferred();

                        final boolean[] onRejectedCalled = {false};

                        d.getPromise().then(new Fulfillable() {

                            @Override
                            public Object fulfill(Object o) {
                                Assert.assertEquals("onFulfilled should not have been called", false, onRejectedCalled[0]);

                                return o;
                            }
                        }, new Rejectable() {
                            @Override
                            public Object reject(Object o) {
                                onRejectedCalled[0] = true;

                                return o;
                            }
                        });

                        d.reject(dummy);
                        delay(50);
                        d.resolve(dummy);
                        delay(100);
                    }
                });
            }
        });
    }
}