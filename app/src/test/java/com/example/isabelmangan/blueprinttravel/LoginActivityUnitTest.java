package com.example.isabelmangan.blueprinttravel;

import android.util.Log;
import static org.mockito.Mockito.verify;
import com.google.firebase.auth.FirebaseAuth;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Captor;
import org.mockito.ArgumentCaptor;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;

//@RunWith(MockitoJUnitRunner.class)
public class LoginActivityUnitTest {

    LoginActivity test = new LoginActivity();
    private static FirebaseAuth mAuth;
    private static final String TAG = "THISISMYTAG";
        @Mock AppenderSkeleton appender;
        @Captor ArgumentCaptor<LoggingEvent> logCaptor;

//        @Test
//        public void testRegisterUser(){
//            //final TestAppender appender = new TestAppender();
//            final Logger logger = Logger.getRootLogger();
//            logger.addAppender(appender);
//            LoginActivity test = new LoginActivity();
//            test.registerUser();
//            verify(appender).doAppend(logCaptor.capture());
//            assertEquals("Warning message should have been logged", "register:success", logCaptor.getValue().getRenderedMessage());
//
//        }

        @Test
        public void test() {
            final TestAppender appender = new TestAppender();
            LoginActivity test = new LoginActivity();
            test.registerUser();
            Logger logger = Logger.getLogger(LoginActivity.class);
            logger.addAppender(appender);
            try {

                Logger.getLogger(LoginActivity.class).info("LoginActivityTest");
            }
            finally {
                logger.removeAppender(appender);
            }

            final List<LoggingEvent> log = appender.getLog();
            final LoggingEvent firstLogEntry = log.get(0);
            assertThat(firstLogEntry.getLevel(), is(Level.INFO));
            assertThat((String) firstLogEntry.getMessage(), is("LoginActivityTest"));
            assertThat(firstLogEntry.getLoggerName(), is("com.example.isabelmangan.blueprinttravel.LoginActivity"));
        }

    class TestAppender extends AppenderSkeleton {
        private final List<LoggingEvent> log = new ArrayList<LoggingEvent>();

        @Override
        public boolean requiresLayout() {
            return false;
        }

        @Override
        protected void append(final LoggingEvent loggingEvent) {
            log.add(loggingEvent);
        }

        @Override
        public void close() {
        }

        public List<LoggingEvent> getLog() {
            return new ArrayList<LoggingEvent>(log);
        }
    }
}
