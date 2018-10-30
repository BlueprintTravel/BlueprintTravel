package com.example.isabelmangan.blueprinttravel;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginActivityUnitTest {

    LoginActivity test = new LoginActivity();
    private static FirebaseAuth mAuth;

        @Test
        public void login_isAuthenticated() {
            mAuth = FirebaseAuth.getInstance();
            //mAuth = FirebaseHandler.getAuth();
            test.signInUser("example@example.com", "1234567");
            assertTrue(mAuth != null);
        }

}
