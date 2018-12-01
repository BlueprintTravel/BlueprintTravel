package com.example.isabelmangan.blueprinttravel;

import android.arch.lifecycle.ReportFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.menu.MenuItemImpl;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;



public class RouteMapActivityUnitTest {

    private ArrayList<Integer> viewAttrImagesList = new ArrayList<Integer>();
    private GoogleMap mMap;

    RouteMapActivity route = new RouteMapActivity();

    private static FirebaseAuth mAuth;
    private static final String TAG = "THISISMYTAG";
    @Mock AppenderSkeleton appender;
    @Captor ArgumentCaptor<LoggingEvent> logCaptor;

    @Test
    public void test() {
        final TestAppender appender = new TestAppender();

        // test method onRequestPermissionsResult
        RouteMapActivity test = new RouteMapActivity();
        String[] myStringArray = {"a"};
        int[] myIntArray = {1};
        test.onRequestPermissionsResult(0, myStringArray, myIntArray);

        test.changeUIToEditTrip();

        Logger logger = Logger.getLogger(RouteMapActivity.class);
        logger.addAppender(appender);
        try {

            Logger.getLogger(RouteMapActivity.class).info("RouteMapActivityUnitTest");
        }
        finally {
            logger.removeAppender(appender);
        }

        final List<LoggingEvent> log = appender.getLog();
        final LoggingEvent firstLogEntry = log.get(0);
        assertThat(firstLogEntry.getLevel(), is(Level.INFO));
        assertThat((String) firstLogEntry.getMessage(), is("RouteMapActivityUnitTest"));
        assertThat(firstLogEntry.getLoggerName(), is("com.example.isabelmangan.blueprinttravel.RouteMapActivity"));
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
