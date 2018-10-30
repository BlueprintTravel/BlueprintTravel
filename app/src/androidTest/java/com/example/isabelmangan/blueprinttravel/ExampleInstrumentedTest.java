package com.example.isabelmangan.blueprinttravel;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static FirebaseAuth mAuth;
    @Test
    public void useAppContext() {
        LoginActivity test = new LoginActivity();
        test.signInUser("example@example.com", "1234567");
        assertTrue(mAuth != null);
    }
}
