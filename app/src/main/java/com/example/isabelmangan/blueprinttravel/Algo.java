package com.example.isabelmangan.blueprinttravel;

import android.util.Log;

import java.util.*;

class Algo {



    public static ArrayList<Integer> generateOptimizedRoute(int curr, int[] open, int[] close,
                                                            long[][] walking_time, int[] time_spent)
    {
//      int[] visited = {0,0,0,0};
        int visited[] = new int[open.length];
        for (int i = 0; i < visited.length - 1; i++)
        {
            visited[i] = 0;
        }
        // Calculate the location with lea}st H, which is also open
        // H = curr_close * (walking time + time spent)

        ArrayList<Integer> path = new ArrayList<>();

        long x, H, minH;
        int start = 0;
        boolean changed = false;

        while(true)
        {
            int prev = start;
            minH = Integer.MAX_VALUE;

            for (int i = 0; i < open.length; i++)
            {
                if(visited[i] == 0 && open[i] < curr)
                {
                    if(curr + walking_time[start][i] + time_spent[i] <= close[i] || start == 0)
                    {
                        x = close[i] - curr;
                        H = x * (walking_time[start][i] + time_spent[i]);

                        if(minH > H)
                        {
                            minH = H;
                            start = i;
                            changed = true;
                        }
                    }
                }
            }
            if (changed)
            {
                path.add(start);
                visited[start] = 1;
                changed = false;
            }
            else
                break;

            curr += (walking_time[prev][start] + time_spent[start]);
        }

        if(path.size() == 1)
        {
            System.out.println("NO PATH FOUND");
            return null;
        }

        for (int i = 0; i < path.size(); i++)
        {
            if (i == path.size() -1)
                Log.d("emailpassword", "path size is " + path.get(i));
            else
                Log.d("emailpassword", "path size is " + path.get(i)+" --> ");

        }

        return path;
    }
}