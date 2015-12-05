package nl.brusque.iou.some_package;

import android.app.Activity;
import android.os.Looper;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;

import nl.brusque.iou.IOUAndroid;
import nl.brusque.iou.AndroidPromise;
import nl.brusque.iou.AndroidThenCallable;
import nl.brusque.iou.TestMainActivity;

public class ApplicationTest extends ActivityInstrumentationTestCase2<TestMainActivity> {

    public ApplicationTest() {
        super(TestMainActivity.class);
    }

    private final String DUMMY = "DUMMY";

    public void testRegularResolve() {
        final int[] _fulfillableCount = new int[1];
        final int[] _rejectableCount  = new int[1];

        IOUAndroid<String> iou = new IOUAndroid<>(getActivity());
        iou.getPromise()
            .then(new AndroidThenCallable<String, Void>() {
                @Override
                public AndroidPromise.ExecutionScope getExecutionScope() {
                    return AndroidPromise.ExecutionScope.BACKGROUND;
                }

                @Override
                public Void apply(String o) throws Exception {
                    _fulfillableCount[0]++;

                    return null;
                }
            }, new AndroidThenCallable<Object, Void>() {
                @Override
                public AndroidPromise.ExecutionScope getExecutionScope() {
                    return AndroidPromise.ExecutionScope.BACKGROUND;
                }

                @Override
                public Void apply(Object o) throws Exception {
                    _rejectableCount[0]++;

                    return null;
                }
            });

        iou.resolve(DUMMY);

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

        IOUAndroid<String> iou = new IOUAndroid<>(ApplicationTest.this.getActivity());
        iou.getPromise().then(new AndroidThenCallable<String, Void>() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Void apply(String o) throws Exception {
                _fulfillableCount[0]++;

                return null;
            }
        }, new AndroidThenCallable<Object, Void>() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Void apply(Object o) throws Exception {
                _rejectableCount[0]++;

                return null;
            }
        });

        iou.reject(DUMMY);

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

        IOUAndroid<String> iou = new IOUAndroid<>(ApplicationTest.this.getActivity());
        iou.getPromise().then(new AndroidThenCallable<String, Void>() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Void apply(String o) throws Exception {
                _fulfillableCount[0]++;

                return null;
            }
        }).fail(new AndroidThenCallable<Object, Void>() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Void apply(Object o) throws Exception {
                _rejectableCount[0]++;

                return null;
            }
        });

        iou.resolve(DUMMY);

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

        IOUAndroid<String> iou = new IOUAndroid<>(ApplicationTest.this.getActivity());
        iou.getPromise()
            .then(new AndroidThenCallable<String, Void>() {
                @Override
                public AndroidPromise.ExecutionScope getExecutionScope() {
                    return AndroidPromise.ExecutionScope.BACKGROUND;
                }

                @Override
                public Void apply(String o) throws Exception {
                    _fulfillableCount[0]++;

                    return null;
                }
            }).fail(new AndroidThenCallable<Object, Void>() {
                @Override
                public AndroidPromise.ExecutionScope getExecutionScope() {
                    return AndroidPromise.ExecutionScope.BACKGROUND;
                }

                @Override
                public Void apply(Object o) throws Exception {
                    _rejectableCount[0]++;

                    return null;
                }
            });

        iou.reject(DUMMY);

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("Fulfilled should never have been called", 0, _fulfillableCount[0]);
        Assert.assertEquals("Rejected should have been called exactly once", 1, _rejectableCount[0]);
    }

    public void testWhenResolvesPromiseWithGivenValueImmediately() {
        final int[] _fulfillableCount   = new int[1];
        final int[] _rejectableCount    = new int[1];
        final Object[] _fulfillableResult = new Object[1];

        IOUAndroid<Integer> iou = new IOUAndroid<>(ApplicationTest.this.getActivity());
        iou.when(1)
            .then(new AndroidThenCallable<Integer, Integer>() {
                @Override
                public AndroidPromise.ExecutionScope getExecutionScope() {
                    return AndroidPromise.ExecutionScope.BACKGROUND;
                }

                @Override
                public Integer apply(Integer o) throws Exception {
                    _fulfillableCount[0]++;
                    _fulfillableResult[0] = o;

                    return o;
                }
            })
            .fail(new AndroidThenCallable<Object, Void>() {
                @Override
                public AndroidPromise.ExecutionScope getExecutionScope() {
                    return AndroidPromise.ExecutionScope.BACKGROUND;
                }

                @Override
                public Void apply(Object o) throws Exception {
                    _rejectableCount[0]++;

                    return null;
                }
            });

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("Fulfilled should have been called exactly once", 1, _fulfillableCount[0]);
        Assert.assertEquals("Fulfilled should have been called with value '1'", 1, _fulfillableResult[0]);
        Assert.assertEquals("Rejected should never have been called", 0, _rejectableCount[0]);
    }

    public void testRegularBackgroundScope() {
        final boolean[] _isUIThread   = new boolean[1];

        final Activity c = ApplicationTest.this.getActivity();

        IOUAndroid<Integer> iou = new IOUAndroid<>(c);
        iou.when(1)
            .then(new AndroidThenCallable<Integer, Void>() {
                @Override
                public AndroidPromise.ExecutionScope getExecutionScope() {
                    return AndroidPromise.ExecutionScope.BACKGROUND;
                }

                @Override
                public Void apply(Integer o) throws Exception {
                    _isUIThread[0] = c.getMainLooper() == Looper.myLooper();

                    return null;
                }
            });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("Fulfilled should not run on UI thread", false, _isUIThread[0]);
    }

    public void testRegularUIScope() {
        final boolean[] _isUIThread   = new boolean[1];

        final Activity c = ApplicationTest.this.getActivity();
        IOUAndroid<Integer> iou = new IOUAndroid<>(c);
        iou.when(1)
            .then(new AndroidThenCallable<Integer, Void>() {
                @Override
                public AndroidPromise.ExecutionScope getExecutionScope() {
                    return AndroidPromise.ExecutionScope.UI;
                }

                @Override
                public Void apply(Integer o) throws Exception {
                    _isUIThread[0] = c.getMainLooper() == Looper.myLooper();

                    return null;
                }
            });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("Fulfilled should run on UI thread", true, _isUIThread[0]);
    }

    public void testShortenedBackgroundScope() {
        final boolean[] _isUIThread   = new boolean[1];

        final Activity c = ApplicationTest.this.getActivity();
        IOUAndroid<Integer> iou = new IOUAndroid<>(c);
        iou.when(1)
            .then(new AndroidThenCallable<Integer, Object>(AndroidPromise.ExecutionScope.BACKGROUND) {
                @Override
                public Void apply(Integer o) throws Exception {
                    _isUIThread[0] = c.getMainLooper() == Looper.myLooper();

                    return null;
                }
            });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("Fulfilled should not run on UI thread", false, _isUIThread[0]);
    }

    public void testShortenedUIScope() {
        final boolean[] _isUIThread   = new boolean[1];

        final Activity c = ApplicationTest.this.getActivity();

        IOUAndroid<Integer> iou = new IOUAndroid<>(c);
        iou.when(1)
            .then(new AndroidThenCallable<Integer, Void>(AndroidPromise.ExecutionScope.UI) {
                @Override
                public Void apply(Integer o) throws Exception {
                    _isUIThread[0] = c.getMainLooper() == Looper.myLooper();

                    return null;
                }
            });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("Fulfilled should run on UI thread", true, _isUIThread[0]);
    }
}