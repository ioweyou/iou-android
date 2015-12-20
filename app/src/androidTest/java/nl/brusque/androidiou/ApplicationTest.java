package nl.brusque.androidiou;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import junit.framework.Assert;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testJustATest() {
        Assert.assertEquals(true, true);
    }

    public void testTestOne() {
        AndroidIOU iou = new AndroidIOU(ApplicationTest.this.getContext());
        iou.getPromise().then(new AndroidFulfillable() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Object fulfill(Object o) throws Exception {
                Log.i("JUST", "TEST");

                return o;
            }
        }, new AndroidRejectable() {
            @Override
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Object reject(Object o) throws Exception {
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