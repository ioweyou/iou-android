package nl.brusque.androidiou;

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

    public void testTestOne() {
        AndroidIOU iou = new AndroidIOU(ApplicationTest.this.getContext());
        iou.getPromise().then(new AndroidThenCallable() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Object call(Object o) throws Exception {
                Log.i("JUST", "TEST");

                return o;
            }
        }, new AndroidThenCallable() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Object call(Object o) throws Exception {
                Log.i("JUST", "REJECT");
                return o;
            }
        });

        iou.resolve(1);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}