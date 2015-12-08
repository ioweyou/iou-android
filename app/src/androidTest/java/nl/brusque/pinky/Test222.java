package nl.brusque.pinky;

import junit.framework.Assert;

import nl.brusque.pinky.helper.PromiseTest;
import nl.brusque.pinky.promise.Fulfillable;
import nl.brusque.pinky.promise.Rejectable;

public class Test222 extends PromiseTest {
    public void test222IfOnFulfilledIsAFunction() {
        describe("2.2.2: If `onFulfilled` is a function,", new Runnable() {
            final String dummy = "DUMMY";

            @Override
            public void run() {
                describe("2.2.2.2: it must not be called before `promise` is fulfilled", new Runnable() {
                    @Override
                    public void run() {
                    specify("fulfilled after a delay", new Runnable() {
                        @Override
                        public void run() {
                            Pinky d = deferred();
                            final boolean[] isFulfilled = {false};

                            d.getPromise().then(new Fulfillable() {
                                @Override
                                public Object fulfill(Object o) {
                                    Assert.assertTrue("isFulfilled should be true", isFulfilled[0]);

                                    return null;
                                }
                            });

                            delay(50);

                            d.resolve(dummy);
                            isFulfilled[0] = true;

                        }
                    });

                    specify("never fulfilled", new Runnable() {
                        @Override
                        public void run() {
                            Pinky d = deferred();
                            final boolean[] onFulfilledCalled = {false};

                            d.getPromise().then(new Fulfillable() {
                                @Override
                                public Object fulfill(Object o) {
                                    onFulfilledCalled[0] = true;

                                    return null;
                                }
                            });

                            delay(150);
                            Assert.assertFalse("OnFulfilled should not have been called", onFulfilledCalled[0]);
                        }
                    });
                    }
                });

                describe("2.2.2.3: it must not be called more than once.", new Runnable() {
                    @Override
                    public void run() {
                        specify("already-fulfilled", new Runnable() {
                            @Override
                            public void run() {
                                final int[] timesCalled = {0};

                                resolved(dummy).then(new Fulfillable() {
                                    @Override
                                    public Object fulfill(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                        return null;
                                    }
                                });
                            }
                        });

                        specify("trying to fulfill a pending promise more than once, immediately", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final int[] timesCalled = {0};

                                d.getPromise().then(new Fulfillable() {
                                    @Override
                                    public Object fulfill(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                        return null;
                                    }
                                });

                                d.resolve(dummy);
                                d.resolve(dummy);
                            }
                        });

                        specify("trying to fulfill a pending promise more than once, delayed", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final int[] timesCalled = {0};

                                d.getPromise().then(new Fulfillable() {
                                    @Override
                                    public Object fulfill(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                        return null;
                                    }
                                });

                                delay(50);
                                d.resolve(dummy);
                                d.resolve(dummy);
                            }
                        });

                        specify("trying to fulfill a pending promise more than once, immediately then delayed", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final int[] timesCalled = {0};

                                d.getPromise().then(new Fulfillable() {
                                    @Override
                                    public Object fulfill(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                        return null;
                                    }
                                });

                                d.resolve(dummy);
                                delay(50);
                                d.resolve(dummy);
                            }
                        });

                        specify("when multiple `then` calls are made, spaced apart in time", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final int[] timesCalled = {0, 0, 0};

                                d.getPromise().then(new Fulfillable() {
                                    @Override
                                    public Object fulfill(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                        return null;
                                    }
                                });

                                delay(50);
                                d.getPromise().then(new Fulfillable() {
                                    @Override
                                    public Object fulfill(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[1]);

                                        return null;
                                    }
                                });

                                delay(50);
                                d.getPromise().then(new Fulfillable() {
                                    @Override
                                    public Object fulfill(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[2]);

                                        return null;
                                    }
                                });

                                delay(50);
                                d.resolve(dummy);
                            }
                        });

                        specify("when `then` is interleaved with fulfillment", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final int[] timesCalled = {0, 0};

                                d.getPromise().then(new Fulfillable() {
                                    @Override
                                    public Object fulfill(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                        return null;
                                    }
                                });

                                d.resolve(dummy);

                                d.getPromise().then(new Fulfillable() {
                                    @Override
                                    public Object fulfill(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[1]);

                                        return null;
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        delay(3000);
    }
}