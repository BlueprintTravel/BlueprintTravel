package com.example.isabelmangan.blueprinttravel;

import android.util.Log;
import static org.mockito.Mockito.verify;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
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

public class EditTripActivityUnitTest {
    @Test
    public void test1() {
        final EditTripActivityUnitTest.TestAppender appender = new EditTripActivityUnitTest.TestAppender();
        EditTripActivity test = new EditTripActivity();
        test.addLocationUpdateUI(Boolean.TRUE);
        test.addLocationUpdateUI(Boolean.FALSE);
        Logger logger = Logger.getLogger(EditTripActivity.class);
        logger.addAppender(appender);
        try {

            Logger.getLogger(EditTripActivity.class).info("EditTripActivityUnitTest");
        }
        finally {
            logger.removeAppender(appender);
        }

        final List<LoggingEvent> log = appender.getLog();
        final LoggingEvent firstLogEntry = log.get(0);
        assertThat(firstLogEntry.getLevel(), is(Level.INFO));
        assertThat((String) firstLogEntry.getMessage(), is("EditTripActivityUnitTest"));
        assertThat(firstLogEntry.getLoggerName(), is("com.example.isabelmangan.blueprinttravel.EditTripActivity"));
    }

//    @Test
//    public void test2(){
//        EditTripActivity test = new EditTripActivity();
//        test.latlng = new LatLng(5,5);
//        test.generateRoute();
//
//    }

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
