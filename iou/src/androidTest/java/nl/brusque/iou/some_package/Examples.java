package nl.brusque.iou.some_package;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import nl.brusque.iou.IOUAndroid;
import nl.brusque.iou.AndroidPromise;
import nl.brusque.iou.AndroidThenCallable;
import nl.brusque.iou.TestMainActivity;

public class Examples extends ActivityInstrumentationTestCase2<TestMainActivity> {

    private static final String TAG = "IOUExamples";

    public Examples() {
        super(TestMainActivity.class);
    }

    public void testCallWithSingleThen() {
        IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

        iou.getPromise()
                .then(new AndroidThenCallable<Integer, Void>(AndroidPromise.ExecutionScope.BACKGROUND) {
                    @Override
                    public Void apply(Integer input) throws Exception {
                        Log.i(TAG, input.toString());

                        return null;
                    }
                });

        iou.resolve(42); // prints "42"

        allowTestToFinish();
    }


    public void testCallWithSingleThenVerbose() {
        IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

        iou.getPromise()
                .then(new AndroidThenCallable<Integer, Void>() {
                    public AndroidPromise.ExecutionScope getExecutionScope() {
                        return AndroidPromise.ExecutionScope.BACKGROUND;
                    }

                    @Override
                    public Void apply(Integer input) throws Exception {
                        Log.i(TAG, input.toString());
                        return null;
                    }
                });

        iou.resolve(42); // prints "42"

        allowTestToFinish();
    }

    public void testCallWithPipedPromise() {
        IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

        iou.getPromise()
                .then(new AndroidThenCallable<Integer, Integer>(AndroidPromise.ExecutionScope.BACKGROUND) {
                    @Override
                    public Integer apply(Integer input) throws Exception {
                        return input * 10;
                    }
                })
                .then(new AndroidThenCallable<Integer, String>(AndroidPromise.ExecutionScope.BACKGROUND) {
                    @Override
                    public String apply(Integer input) throws Exception {
                        return String.format("The result: %d", input);
                    }
                })
                .then(new AndroidThenCallable<String, Void>(AndroidPromise.ExecutionScope.UI) {
                    @Override
                    public Void apply(String input) throws Exception {
                        Log.i(TAG, input);

                        return null;
                    }
                });

        iou.resolve(42); // prints "The result: 420"

        allowTestToFinish();
    }

    public void testCallReject() {
        IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

        iou.getPromise()
                .then(new AndroidThenCallable<Integer, Integer>(AndroidPromise.ExecutionScope.UI) {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer * 42;
                    }
                })
                .fail(new AndroidThenCallable<Object, Void>(AndroidPromise.ExecutionScope.BACKGROUND) {
                    @Override
                    public Void apply(Object input) throws Exception {
                        Log.i(TAG, String.format("%s I can't do that.", input));

                        return null;
                    }
                });

        iou.reject("I'm sorry, Dave."); // prints "I'm sorry, Dave. I can't do that."

        allowTestToFinish();
    }

    public void testCallRejectAPlus() {
        IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

        iou.getPromise()
                .then(new AndroidThenCallable<Integer, Integer>(AndroidPromise.ExecutionScope.UI) {
                    @Override
                    public Integer apply(Integer input) throws Exception {
                        return input * 42;
                    }
                }, new AndroidThenCallable<Object, Integer>(AndroidPromise.ExecutionScope.BACKGROUND) {
                    @Override
                    public Integer apply(Object input) throws Exception {
                        Log.i(TAG, String.format("%s I can't do that.", input));

                        return null;
                    }
                });

        iou.reject("I'm sorry, A+."); // prints "I'm sorry, A+. I can't do that."

        allowTestToFinish();
    }

    public void testCallFail() {
        IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

        iou.getPromise()
                .then(new AndroidThenCallable<Integer, Integer>(AndroidPromise.ExecutionScope.BACKGROUND) {
                    @Override
                    public Integer apply(Integer input) throws Exception {
                        throw new Exception("I just don't care.");
                    }
                })
                .then(new AndroidThenCallable<Integer, Void>(AndroidPromise.ExecutionScope.UI) {
                    @Override
                    public Void apply(Integer input) throws Exception {
                        Log.i(TAG, "What would you say you do here?");

                        return null;
                    }
                })
                .fail(new AndroidThenCallable<Object, Void>() {
                    @Override
                    public Void apply(Object reason) throws Exception {
                        Log.i(TAG,
                            String.format("It's not that I'm lazy, it's that %s",
                                    ((Exception)reason).getMessage()));

                        return null;
                    }
                });

        iou.resolve(42); // prints "It's not that I'm lazy, it's that I just don't care."

        allowTestToFinish();
    }

    private void allowTestToFinish() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}