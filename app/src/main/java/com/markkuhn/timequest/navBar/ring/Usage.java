package com.markkuhn.timequest.navBar.ring;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
     * Created by User on 3/2/15.
     */
    public class Usage {
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
        public static final String TAG = Usage.class.getSimpleName();

        @SuppressWarnings("ResourceType")
        public static void getStats(Context context){
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

            // Map that stores all packages by name
            // key is package name - UsageStats represents usage stats for the package name
            Map<String, UsageStats> aUsageStatsMap = usm.queryAndAggregateUsageStats(System.currentTimeMillis()-86400000, System.currentTimeMillis());

            long hoursAmount = 24; // amount of hours to look back on
            long endTime = System.currentTimeMillis(); // get current time
            long startTime = endTime - hoursAmount * 3600000; // convert from hours to milliseconds

            // TEST: print usage for TimeQuest
            long timeQuestUsage = aUsageStatsMap.get("com.markkuhn.timequest").getTotalTimeInForeground();
            System.out.println("TOTAL USAGE TIME FOR TIMEQUEST ["+timeQuestUsage+"]");

            Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
            Log.d(TAG, "Range end:" + dateFormat.format(endTime));

            UsageEvents uEvents = usm.queryEvents(startTime,endTime);
            while (uEvents.hasNextEvent()){
                UsageEvents.Event e = new UsageEvents.Event();
                uEvents.getNextEvent(e);

                if (e != null){
                    Log.d(TAG, "Event: " + e.getPackageName() + "\t" +  e.getTimeStamp());
                }
            }
        }

        public static List<UsageStats> getUsageStatsList(Context context){
            UsageStatsManager usm = getUsageStatsManager(context);
            long hoursAmount = 24; // amount of hours to look back on
            long endTime = System.currentTimeMillis(); // get current time
            long startTime = endTime - hoursAmount * 3600000; // convert from hours to milliseconds

//            Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
//            Log.d(TAG, "Range end:" + dateFormat.format(endTime));

            return usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
        }

        public static void printUsageStats(List<UsageStats> usageStatsList){
            for (UsageStats u : usageStatsList){
                Log.d(TAG, "Pkg: " + u.getPackageName() +  "\t" + "ForegroundTime: "
                        + u.getTotalTimeInForeground()) ;
            }

        }

        public static void printCurrentUsageStatus(Context context){
            printUsageStats(getUsageStatsList(context));
        }
        @SuppressWarnings("ResourceType")
        private static UsageStatsManager getUsageStatsManager(Context context){
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
            return usm;
        }
    }
