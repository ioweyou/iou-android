package nl.brusque.pinky;

import junit.framework.Assert;

import nl.brusque.pinky.helper.PromiseTest;
import nl.brusque.pinky.promise.Fulfillable;
import nl.brusque.pinky.promise.Rejectable;

public class Test224 extends PromiseTest {
    public void test224onFulfilledOronRejectedMustNotBeCalledUntilTheExecutionContextStackContainsOnlyPlatformCode() {
        describe("2.2.4: `onFulfilled` or `onRejected` must not be called until the execution context stack contains only platform code.", new Runnable() {
            final String dummy = "DUMMY";

            @Override
            public void run() {
                describe("Clean-stack execution ordering tests (fulfillment case)", new Runnable() {
                    @Override
                    public void run() {
                        specify("when `onFulfilled` is added immediately before the promise is fulfilled", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final boolean[] onFullfilledCalled = {false};

                                d.getPromise().then(new Fulfillable() {
                                    @Override
                                    public Object fulfill(Object o) {
                                        onFullfilledCalled[0] = true;

                                        return null;
                                    }
                                });

                                d.resolve(dummy);

                                Assert.assertFalse("onFulfilled should not have been called.", onFullfilledCalled[0]);
                            }
                        });

                        specify("when `onFulfilled` is added immediately after the promise is fulfilled", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final boolean[] onFullfilledCalled = {false};

                                d.resolve(dummy);

                                d.getPromise().then(new Fulfillable() {
                                    @Override
                                    public Object fulfill(Object o) {
                                        onFullfilledCalled[0] = true;

                                        return null;
                                    }
                                });

                                Assert.assertFalse("onFulfilled should not have been called.", onFullfilledCalled[0]);
                            }
                        });

                        specify("when one `onFulfilled` is added inside another `onFulfilled`", new Runnable() {
                            @Override
                            public void run() {
                                final IPromise promise = resolved();
                                final boolean[] firstOnFulfilledFinished = {false};

                                promise.then(new Fulfillable() {
                                    @Override
                                    public Object fulfill(Object o) {
                                        promise.then(new Fulfillable() {
                                            @Override
                                            public Object fulfill(Object o) {
                                                Assert.assertTrue("first onFulfilled should have finished", firstOnFulfilledFinished[0]);
                                                return null;
                                            }
                                        });

                                        firstOnFulfilledFinished[0] = true;

                                        return null;
                                    }
                                });

                                delay(50);
                            }
                        });

                        specify("when `onFulfilled` is added inside an `onRejected`", new Runnable() {
                            @Override
                            public void run() {
                                final IThenable promise = rejected();
                                final IPromise promise2 = resolved();
                                final boolean[] firstOnFulfilledFinished = {false};

                                promise.then(null, new Rejectable() {
                                    @Override
                                    public Object reject(Object o) {
                                        promise2.then(new Fulfillable() {
                                            @Override
                                            public Object fulfill(Object o) {
                                                Assert.assertTrue("first onRejected should have finished", firstOnFulfilledFinished[0]);
                                                return null;
                                            }
                                        });

                                        firstOnFulfilledFinished[0] = true;

                                        return null;
                                    }
                                });

                                delay(50);
                            }
                        });

                        specify("when the promise is fulfilled asynchronously", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final boolean[] firstStackFinished = {false};

                                d.getPromise().then(new Fulfillable() {
                                    @Override
                                    public Object fulfill(Object o) {
                                        Assert.assertTrue("first stack should have finished", firstStackFinished[0]);

                                        return null;
                                    }
                                });

                                delay(50);
                                d.resolve(dummy);
                                delay(50);
                            }
                        });
                    }
                });

                describe("Clean-stack execution ordering tests (rejection case)", new Runnable() {
                    @Override
                    public void run() {
                        specify("when `onRejected` is added immediately before the promise is rejected", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final boolean[] onRejectedCalled = {false};

                                d.getPromise().then(null, new Rejectable() {
                                    @Override
                                    public Object reject(Object o) {
                                        onRejectedCalled[0] = true;

                                        return null;
                                    }
                                });

                                d.reject(dummy);

                                Assert.assertFalse("onRejected should not have been called.", onRejectedCalled[0]);
                            }
                        });

                        specify("when `onRejected` is added immediately after the promise is rejected", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final boolean[] onRejectedCalled = {false};

                                d.reject(dummy);

                                d.getPromise().then(null, new Rejectable() {
                                    @Override
                                    public Object reject(Object o) {
                                        onRejectedCalled[0] = true;

                                        return null;
                                    }
                                });



                                Assert.assertFalse("onRejected should not have been called.", onRejectedCalled[0]);
                            }
                        });

                        specify("when one `onRejected` is added inside another `onRejected`", new Runnable() {
                            @Override
                            public void run() {
                                final IThenable promise = rejected();
                                final boolean[] firstOnRejectedFinished = {false};

                                promise.then(null, new Rejectable() {
                                    @Override
                                    public Object reject(Object o) {
                                        promise.then(new Rejectable() {
                                            @Override
                                            public Object reject(Object o) {
                                                Assert.assertTrue("first onRejected should have finished", firstOnRejectedFinished[0]);
                                                return null;
                                            }
                                        });

                                        firstOnRejectedFinished[0] = true;

                                        return null;
                                    }
                                });

                                delay(50);
                            }
                        });

                        specify("when `onRejected` is added inside an `onFulfilled`", new Runnable() {
                            @Override
                            public void run() {
                                final IPromise promise = resolved();
                                final IThenable promise2 = rejected();
                                final boolean[] firstOnFulfilledFinished = {false};

                                promise.then(new Fulfillable() {
                                    @Override
                                    public Object fulfill(Object o) {
                                        promise2.then(null, new Rejectable() {
                                            @Override
                                            public Object reject(Object o) {
                                                Assert.assertTrue("first onFulfilled should have finished", firstOnFulfilledFinished[0]);

                                                return null;
                                            }
                                        });

                                        firstOnFulfilledFinished[0] = true;

                                        return null;
                                    }
                                });

                                delay(50);
                            }
                        });

                        specify("when the promise is rejected asynchronously", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final boolean[] firstStackFinished = {false};

                                d.getPromise().then(null, new Rejectable() {
                                    @Override
                                    public Object reject(Object o) {
                                        Assert.assertTrue("first stack should have finished", firstStackFinished[0]);

                                        return null;
                                    }
                                });

                                delay(50);
                                d.reject(dummy);
                                delay(50);
                            }
                        });
                    }
                });
            }
        });


    }
}