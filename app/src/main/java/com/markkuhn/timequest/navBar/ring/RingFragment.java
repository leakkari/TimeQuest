package com.markkuhn.timequest.navBar.ring;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.markkuhn.timequest.MainActivity;
import com.markkuhn.timequest.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.app.usage.UsageStatsManager.INTERVAL_DAILY;
import static android.app.usage.UsageStatsManager.INTERVAL_WEEKLY;
import static android.os.Process.myUid;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static androidx.core.content.ContextCompat.getSystemService;

public class RingFragment extends Fragment {
    // Line Chart
    LineChart mpLineChart;

    private RingViewModel ringViewModel;
    private TextView txtProgress;
    private ProgressBar progressBar;
    private int pStatus = 0;
    float sum = 0;
    private Handler handler = new Handler();
    UsageStatsManager mUsageStatsManager;
    private PackageManager packageManager;
    private static final int flags = PackageManager.GET_META_DATA |
            PackageManager.GET_SHARED_LIBRARY_FILES |
            PackageManager.GET_UNINSTALLED_PACKAGES;

    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ringViewModel =
                ViewModelProviders.of(this).get(RingViewModel.class);
        View root = inflater.inflate(R.layout.activity_ring, container, false);

        final TextView usagetxt = root.findViewById(R.id.almostThere_text2);
        TextView txtU = root.findViewById(R.id.almostThere_text2);


        // LINECHART TEST
//        mpLineChart = root.findViewById(R.id.lineChart);
//        ArrayList<Entry> dataVals = new ArrayList<>();
//        dataVals.add(new Entry(0,20));
//        dataVals.add(new Entry(1,24));
//        dataVals.add(new Entry(2,2));
//        dataVals.add(new Entry(3,10));
//        LineDataSet lineDataSet1 = new LineDataSet(dataVals, "Data set 1");
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(lineDataSet1);
//        LineData data = new LineData(dataSets);
//        mpLineChart.setData(data);
//        mpLineChart.invalidate();

        // Get package name list
        ArrayList<String> appsList = new ArrayList<>();
        RingFragment.DownloadTask task = new RingFragment.DownloadTask();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.markkuhn.timequest", Context.MODE_PRIVATE);
        String userKey = sharedPreferences.getString("userKey", "");
        try {
            String result = task.execute("https://timequestapi.herokuapp.com/apps?key="+userKey).get();
            if(result.equals("-1")){
                Toast.makeText(getContext(), "Bad request", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "App list import successful", Toast.LENGTH_SHORT).show();

                // Populate list of apps
                appsList = new ArrayList<>(Arrays.asList(result.split(",")));
            }
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR with clickLogin:" + e);
        }

        final TextView txtProgress = root.findViewById(R.id.txtProgress);
        TextView txt = root.findViewById(R.id.txt);

        long usage6 = getTimeForPeriod(168, appsList);
        long usage5 = getTimeForPeriod(144, appsList);
        long usage4 = getTimeForPeriod(120, appsList);
        long usage3 = getTimeForPeriod(96, appsList);
        long usage2 = getTimeForPeriod(72, appsList);
        long usage1 = getTimeForPeriod(48, appsList);
        long usage0 = getTimeForPeriod(24, appsList);
        GraphView graph = root.findViewById(R.id.bar_graph);
        mUsageStatsManager = (UsageStatsManager) getActivity().getSystemService("usagestats");
        List<UsageStats> usage = getUsageStatistics(INTERVAL_WEEKLY);

        sum = ((float)usage0+(float)usage1+(float)usage2+(float)usage3+(float)usage4+(float)usage5+(float)usage6)/(float)7;
        txtU.setText("Total usage per week: " + sum + "h");

        graph.getViewport().setMaxY(100.0);
        graph.getViewport().setYAxisBoundsManual(true);
        String timeGoal = sharedPreferences.getString("timeGoal", "123456789");
        System.out.println("TIME GOAL IS:"+timeGoal);
        final int tGoal = Integer.parseInt(timeGoal);
        final int avgTime =  420; //getTimeForPeriodHours(168, appsList);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {

                new DataPoint(0, usage6),
                new DataPoint(1, usage5),
                new DataPoint(2, usage4),
                new DataPoint(3, usage3),
                new DataPoint(4, usage2),
                new DataPoint(5, usage1),
                new DataPoint(6, usage0)
        });
        graph.addSeries(series);

        series.setSpacing(10);

        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().reloadStyles();
        graph.setTitle("Daily average usage (last 7 days)");
        graph.setTitleColor(Color.WHITE);
        series.setColor(Color.CYAN);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"6d", "5d", "4d", "3d", "2d", "1d", "Today"});
        staticLabelsFormatter.setVerticalLabels(new String[] {"0", "4h", "8h"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        final ProgressBar progressBar = root.findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pStatus <= ((float)tGoal/(float)avgTime)*100) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(pStatus);
                            txtProgress.setText(pStatus + " %");
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pStatus++;
                }
            }
        }).start();

        return root;
    }

    public List<UsageStats> getUsageStatistics(int intervalType) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getFirstDayOfWeek(),
                        System.currentTimeMillis());

        return queryUsageStats;
    }
    // HTTP GET request from url
    public static class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            }
            catch(Exception e) {
                System.out.println("ERROR:"+e);
                return "Failed";
            }
        }
    }

    public int getTimeForPeriodHours(long hours, ArrayList<String> appsList){
        UsageStatsManager usm = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
        Map<String, UsageStats> aUsageStatsMap = usm.queryAndAggregateUsageStats(System.currentTimeMillis()-hours*3600000, System.currentTimeMillis()-(hours-24)*3600000);
        int lastTime = 0;
        for(String packageName : appsList){
            try {
                lastTime += (aUsageStatsMap.get(packageName).getTotalTimeInForeground() / 3600000);
            } catch (Exception e){
                continue;
            }
        }
        return lastTime;
    }

    public long getTimeForPeriod(long hours, ArrayList<String> appsList){
        UsageStatsManager usm = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
        Map<String, UsageStats> aUsageStatsMap = usm.queryAndAggregateUsageStats(System.currentTimeMillis()-hours*3600000, System.currentTimeMillis()-(hours-24)*3600000);
        long lastTime = 0;
        for(String packageName : appsList){
            try {
//                Toast.makeText(getContext(), "USAGE for ["+packageName+"] ["+aUsageStatsMap.get(packageName).getTotalTimeInForeground()+"] ["+((aUsageStatsMap.get(packageName).getTotalTimeInForeground()  / 3600000) * 100) / 8+"]", Toast.LENGTH_SHORT).show();
//                System.out.println("USAGE FOR "+packageName+" - "+aUsageStatsMap.get(packageName).getTotalTimeInForeground());
                lastTime += ((aUsageStatsMap.get(packageName).getTotalTimeInForeground() / 3600000) * 100) / 8;
            } catch (Exception e){
                continue;
            }
        }
        return lastTime;
    }
}