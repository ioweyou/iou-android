package nl.brusque.iou;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import junit.framework.Assert;

public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testJustATest() {
        Assert.assertEquals(true, true);
    }

    public void testRegularResolve() {
        final int[] _fulfillableCount = new int[1];
        final int[] _rejectableCount  = new int[1];

        AndroidIOU iou = new AndroidIOU(ApplicationTest.this.getContext());
        iou.getPromise().then(new AndroidThenCallable() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Object call(Object o) throws Exception {
                _fulfillableCount[0]++;

                return o;
            }
        }, new AndroidThenCallable() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Object call(Object o) throws Exception {
                _rejectableCount[0]++;

                return o;
            }
        });

        iou.resolve("resolve");

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("Fulfilled should have been called exactly once", 1, _fulfillableCount[0]);
        Assert.assertEquals("Rejected should never have been called", 0, _rejectableCount[0]);
    }

    public void testRegularReject() {
        final int[] _fulfillableCount = new int[1];
        final int[] _rejectableCount  = new int[1];

        AndroidIOU iou = new AndroidIOU(ApplicationTest.this.getContext());
        iou.getPromise().then(new AndroidThenCallable() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Object call(Object o) throws Exception {
                _fulfillableCount[0]++;

                return o;
            }
        }, new AndroidThenCallable() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Object call(Object o) throws Exception {
                _rejectableCount[0]++;

                return o;
            }
        });

        iou.reject("reject");

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("Rejected should have been called exactly once", 1, _rejectableCount[0]);
        Assert.assertEquals("Fulfilled should never have been called", 0, _fulfillableCount[0]);
    }

    public void testResolveWithFailMethod() {
        final int[] _fulfillableCount = new int[1];
        final int[] _rejectableCount  = new int[1];

        AndroidIOU iou = new AndroidIOU(ApplicationTest.this.getContext());
        iou.getPromise().then(new AndroidThenCallable() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Object call(Object o) throws Exception {
                _fulfillableCount[0]++;

                return o;
            }
        }).fail(new AndroidThenCallable() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Object call(Object o) throws Exception {
                _rejectableCount[0]++;

                return o;
            }
        });

        iou.resolve("resolve");

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("Fulfilled should have been called exactly once", 1, _fulfillableCount[0]);
        Assert.assertEquals("Rejected should never have been called", 0, _rejectableCount[0]);
    }

    public void testRejectWithFailMethod() {
        final int[] _fulfillableCount = new int[1];
        final int[] _rejectableCount  = new int[1];

        AndroidIOU iou = new AndroidIOU(ApplicationTest.this.getContext());
        iou.getPromise().then(new AndroidThenCallable() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Object call(Object o) throws Exception {
                _fulfillableCount[0]++;

                return o;
            }
        }).fail(new AndroidThenCallable() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Object call(Object o) throws Exception {
                _rejectableCount[0]++;

                return o;
            }
        });

        iou.reject("reject");

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("Fulfilled should never have been called", 0, _fulfillableCount[0]);
        Assert.assertEquals("Rejected should have been called exactly once", 1, _rejectableCount[0]);
    }

}