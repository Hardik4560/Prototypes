
package com.hd.snscoins.test.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.view.View;

import com.hd.snscoins.R;
import com.hd.snscoins.application.SnSCoreSystem;
import com.hd.snscoins.ui.SplashScreenActivity;

@RunWith(RobolectricTestRunner.class)
public class FirstTest extends SnSCoreSystem {

    private Activity splashScreenActivity;
    private SnSCoreSystem mAppContext;

    @Before
    public void setup() {

        mAppContext = new SnSCoreSystem();

        Robolectric.application = mAppContext;

        splashScreenActivity = Robolectric.buildActivity(SplashScreenActivity.class).create().get();
    }

    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(splashScreenActivity);
    }

    @Test
    public void testInitialStage() throws Exception {

        assertEquals(View.VISIBLE, equals(splashScreenActivity.findViewById(R.id.progress).getVisibility()));
        assertEquals(View.VISIBLE, equals(splashScreenActivity.findViewById(R.id.text).getVisibility()));
    }

}
