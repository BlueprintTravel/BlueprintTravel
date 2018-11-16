package com.example.isabelmangan.blueprinttravel;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class AlgoTest {

    private int curr = 540;

    @Test
    public void test1() {
        //places open later
        int[] open = {0,700,700,700};
        int[] close = {0,750,960,1200};
        int[][] walking_time = {{0, 30, 60, 120}, {30, 0, 30, 90}, {60, 30, 0, 60}, {120, 90, 60, 0}};
        int[] time_spent = {0, 60, 30, 60};

        assertNull(Algo.generateOptimizedRoute(curr,open,close,walking_time,time_spent));
    }

    public void test2(){
        //places are closed/will close
        int[] open = {0,400,400,400};
        int[] close = {0,500,650,700};
        int[][] walking_time = {{0, 30, 60, 120}, {30, 0, 30, 90}, {60, 30, 0, 60}, {120, 90, 60, 0}};
        int[] time_spent = {0, 60, 60, 60};

        assertNull(Algo.generateOptimizedRoute(curr,open,close,walking_time,time_spent));
    }

    public void test3(){
        //all places visited
        int[] open = {0,400,400,400};
        int[] close = {0,750,660,1020};
        int[][] walking_time = {{0, 30, 60, 120}, {30, 0, 30, 90}, {60, 30, 0, 60}, {120, 90, 60, 0}};
        int[] time_spent = {0, 60, 60, 60};

        assertNull(Algo.generateOptimizedRoute(curr,open,close,walking_time,time_spent));
    }
}
