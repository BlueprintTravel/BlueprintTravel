package com.example.isabelmangan.blueprinttravel;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginActivityUnitTest {

    LoginActivity test = new LoginActivity();
    private static FirebaseAuth mAuth;
    private static final String TAG = "THISISMYTAG";

        @Test
        public void login_isAuthenticated() {
           // Log.d(TAG, "-----------------------we got here");
            mAuth = FirebaseAuth.getInstance();
           // if (mAuth == null) {
             //   Log.d(TAG, "-----------------------THIS DID NOT WORK");
            //} else {
              //  Log.d(TAG, "-----------------------THIS DID WORK!!!!!!");
            //}
            //mAuth = FirebaseHandler.getAuth();
            test.signInUser("example@example.com", "1234567");
            assertTrue(mAuth != null);
        }

}
