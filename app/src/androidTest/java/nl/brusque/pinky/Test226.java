package nl.brusque.pinky;

import android.util.Log;

import junit.framework.Assert;

import nl.brusque.pinky.helper.FulfillableSpy;
import nl.brusque.pinky.helper.ISpy;
import nl.brusque.pinky.helper.PromiseTest;
import nl.brusque.pinky.helper.RejectableSpy;
import nl.brusque.pinky.helper.Testable;
import nl.brusque.pinky.promise.Fulfillable;
import nl.brusque.pinky.promise.Promise;

public class Test226 extends PromiseTest {
    public void test226ThenMayBeCalledMultipleTimesOnTheSamePromise() {
        describe("2.2.6: `then` may be called multiple times on the same promise.", new Runnable() {
            final String dummy     = "DUMMY";
            final String other     = "other";
            final String sentinel  = "sentinel";
            final String sentinel2 = "sentinel2";
            final String sentinel3 = "sentinel3";

            @Override
            public void run() {
                describe("2.2.6.1: If/when `promise` is fulfilled, all respective `onFulfilled` callbacks must execute in the order of their originating calls to `then`.", new Runnable() {
                    @Override
                    public void run() {
                        describe("multiple boring fulfillment handlers", new Runnable() {
                            @Override
                            public void run() {
                                testFulfilled(sentinel, new Testable() {
                                    @Override
                                    public void run() {
                                        final ISpy handler1 = fulfillableStub().returns(other);
                                        final ISpy handler2 = fulfillableStub().returns(other);
                                        final ISpy handler3 = fulfillableStub().returns(other);
                                        final ISpy spy      = rejectableStub();

                                        IPromise promise = getPromise();
                                        promise.then(handler1, spy);
                                        promise.then(handler2, spy);
                                        promise.then(handler3, spy);

                                        promise.then(new Fulfillable() {
                                            @Override
                                            public Object fulfill(Object o) {
                                                Assert.assertEquals("Value should equal sentinel", sentinel, o);

                                                Assert.assertEquals("Handler1 not called with sentinel", sentinel, handler1.getCalledWith());
                                                Assert.assertEquals("Handler2 not called with sentinel", sentinel, handler2.getCalledWith());
                                                Assert.assertEquals("Handler3 not called with sentinel", sentinel, handler3.getCalledWith());
                                                Assert.assertEquals("Rejected spy should not have been called", 0, spy.getCallCount());

                                                return null;
                                            }
                                        });
                                    }
                                });
                            }
                        });

                        describe("multiple fulfillment handlers, one of which throws", new Runnable() {
                            @Override
                            public void run() {
                                testFulfilled(sentinel, new Testable() {
                                    @Override
                                    public void run() {
                                        final ISpy handler1 = fulfillableStub().returns(other);
                                        final ISpy handler2 = fulfillableStub().throwsError();
                                        final ISpy handler3 = fulfillableStub().returns(other);
                                        final ISpy spy      = rejectableStub();

                                        IPromise promise = getPromise();
                                        promise.then(handler1, spy);
                                        promise.then(handler2, spy);
                                        promise.then(handler3, spy);

                                        promise.then(new Fulfillable() {
                                            @Override
                                            public Object fulfill(Object o) {
                                                Assert.assertEquals("Value should equal sentinel", sentinel, o);

                                                Assert.assertEquals("Handler1 not called with sentinel", sentinel, handler1.getCalledWith());
                                                Assert.assertEquals("Handler2 not called with sentinel", sentinel, handler2.getCalledWith());
                                                Assert.assertEquals("Handler3 not called with sentinel", sentinel, handler3.getCalledWith());
                                                Assert.assertEquals("Rejected spy should not have been called", 0, spy.getCallCount());

                                                return null;
                                            }
                                        });
                                    }
                                });
                            }
                        });


                        describe("results in multiple branching chains with their own fulfillment values", new Runnable() {
                            @Override
                            public void run() {
                                testFulfilled(dummy, new Testable() {
                                    @Override
                                    public void run() {
                                        IPromise promise = getPromise();

                                        promise
                                            .then(new Fulfillable() {
                                                @Override
                                                public Object fulfill(Object o) {
                                                    return sentinel;
                                                }
                                            }).then(new Fulfillable() {
                                                @Override
                                                public Object fulfill(Object o) {
                                                    Assert.assertEquals("Object should equal sentinel", sentinel, o);

                                                    return null;
                                                }
                                            });

                                        promise
                                            .then(new Fulfillable() {
                                                @Override
                                                public Object fulfill(Object o) {
                                                    return sentinel2;
                                                }
                                            })
                                            .then(new Fulfillable() {
                                                @Override
                                                public Object fulfill(Object o) {
                                                    Assert.assertEquals("Object should equal sentinel2", sentinel2, o);

                                                    return null;
                                                }
                                            });

                                        promise
                                            .then(new Fulfillable() {
                                                @Override
                                                public Object fulfill(Object o) {
                                                    return sentinel3;
                                                }
                                            })
                                            .then(new Fulfillable() {
                                                @Override
                                                public Object fulfill(Object o) {
                                                    Assert.assertEquals("Object should equal sentinel3", sentinel3, o);

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
        });


    }
}