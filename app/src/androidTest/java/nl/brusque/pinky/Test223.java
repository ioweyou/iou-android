package nl.brusque.pinky;

import junit.framework.Assert;

import nl.brusque.pinky.helper.PromiseTest;
import nl.brusque.pinky.promise.Fulfillable;
import nl.brusque.pinky.promise.Rejectable;

public class Test223 extends PromiseTest {
    public void test223IfOnRejectedIsAFunction() {
        describe("2.2.3: If `onRejected` is a function,", new Runnable() {
            final String dummy = "DUMMY";

            @Override
            public void run() {
                describe("2.2.3.2: it must not be called before `promise` is rejected", new Runnable() {
                    @Override
                    public void run() {
                    specify("rejected after a delay", new Runnable() {
                        @Override
                        public void run() {
                            Pinky d = deferred();
                            final boolean[] isRejected = {false};

                            d.getPromise().then(null, new Rejectable() {
                                @Override
                                public Object reject(Object o) {
                                    Assert.assertTrue("isRejected should be true", isRejected[0]);

                                    return null;
                                }
                            });

                            delay(50);

                            d.resolve(dummy);
                            isRejected[0] = true;
                        }
                    });

                    specify("never rejected", new Runnable() {
                        @Override
                        public void run() {
                            Pinky d = deferred();
                            final boolean[] onRejectedCalled = {false};

                            d.getPromise().then(null, new Rejectable() {
                                @Override
                                public Object reject(Object o) {
                                    Assert.assertTrue("isRejected should be true", onRejectedCalled[0]);

                                    return null;
                                }
                            });

                            delay(50);
                            Assert.assertFalse("onRejected should not have been called", onRejectedCalled[0]);
                        }
                    });
                    }
                });

                describe("2.2.3.3: it must not be called more than once.", new Runnable() {
                    @Override
                    public void run() {
                        specify("already-rejected", new Runnable() {
                            @Override
                            public void run() {
                                final int[] timesCalled = {0};

                                rejected(dummy).then(null, new Rejectable() {
                                    @Override
                                    public Object reject(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                        return null;
                                    }
                                });
                            }
                        });

                        specify("trying to reject a pending promise more than once, immediately", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final int[] timesCalled = {0};

                                d.getPromise().then(null, new Rejectable() {
                                    @Override
                                    public Object reject(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                        return null;
                                    }
                                });

                                d.reject(dummy);
                                d.reject(dummy);
                            }
                        });

                        specify("trying to reject a pending promise more than once, delayed", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final int[] timesCalled = {0};

                                d.getPromise().then(null, new Rejectable() {
                                    @Override
                                    public Object reject(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                        return null;
                                    }
                                });

                                delay(50);
                                d.reject(dummy);
                                d.reject(dummy);
                            }
                        });

                        specify("trying to reject a pending promise more than once, immediately then delayed", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final int[] timesCalled = {0};

                                d.getPromise().then(null, new Rejectable() {
                                    @Override
                                    public Object reject(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                        return null;
                                    }
                                });

                                d.reject(dummy);
                                delay(50);
                                d.reject(dummy);
                            }
                        });

                        specify("when multiple `then` calls are made, spaced apart in time", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final int[] timesCalled = {0, 0, 0};

                                d.getPromise().then(null, new Rejectable() {
                                    @Override
                                    public Object reject(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                        return null;
                                    }
                                });

                                delay(50);
                                d.getPromise().then(null, new Rejectable() {
                                    @Override
                                    public Object reject(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[1]);

                                        return null;
                                    }
                                });

                                delay(50);
                                d.getPromise().then(null, new Rejectable() {
                                    @Override
                                    public Object reject(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[2]);

                                        return null;
                                    }
                                });

                                delay(50);
                                d.reject(dummy);
                            }
                        });

                        specify("when `then` is interleaved with rejection", new Runnable() {
                            @Override
                            public void run() {
                                Pinky d = deferred();
                                final int[] timesCalled = {0, 0};

                                d.getPromise().then(null, new Rejectable() {
                                    @Override
                                    public Object reject(Object o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                        return null;
                                    }
                                });

                                d.reject(dummy);

                                d.getPromise().then(null, new Rejectable() {
                                    @Override
                                    public Object reject(Object o) {
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
    }
}