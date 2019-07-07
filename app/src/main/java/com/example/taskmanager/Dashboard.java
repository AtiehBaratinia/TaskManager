package com.example.taskmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Task> tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tasks = getIntent().getParcelableArrayListExtra("task");



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView number_close_activity_global = findViewById(R.id.number_close_activity_global);
        TextView number_close_activity_this_month = findViewById(R.id.number_close_activity_this_month);
        TextView number_close_activity_this_season = findViewById(R.id.number_close_activity_this_season);
        TextView number_late_activity_global = findViewById(R.id.number_late_activity_global);
        TextView number_late_activity_this_month = findViewById(R.id.number_late_activity_this_month);
        TextView number_late_activity_this_season = findViewById(R.id.number_late_activity_this_season);
        TextView number_open_activity_global = findViewById(R.id.number_open_activity_global);
        TextView number_open_activity_this_month = findViewById(R.id.number_open_activity_this_month);
        TextView number_open_activity_this_season = findViewById(R.id.number_open_activity_this_season);

        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String date = df.format(c);
        String[] split = date.split("/");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        int yearTask;
        int monthTask;
        int dayTask;
        int lateMonth = 0, lateGlobal = 0, lateSeason = 0, closeMonth = 0, closeSeason = 0, closeGlobal = 0;
        int openMonth = 0, openGlobal = 0, openSeason = 0;

        String[] people = getResources().getStringArray(R.array.assigned_to);
        int[] activityMonth = new int[people.length];
        int[] activitySeaon = new int[people.length];
        //calculate each number in dashboard
        for (int i = 0; i < tasks.size(); i++) {
            String[] dateTask = tasks.get(i).getDate().split("/");
            yearTask = Integer.parseInt(dateTask[0]);
            monthTask = Integer.parseInt(dateTask[1]);
            dayTask = Integer.parseInt(dateTask[2]);

            if (yearTask < year ){
                lateGlobal ++;
            }
            else if(yearTask == year && monthTask < month){
                if((month - 1)/3 == (monthTask - 1)/3){
                    lateSeason++;
                    lateGlobal++;
                    for (int j = 0; j < people.length; j++) {
                        if(tasks.get(i).getAssign().equals(people[j])){
                            activitySeaon[j] ++;
                        }
                    }
                }
            }
            else if(year == yearTask && month == monthTask && day > dayTask){
                lateGlobal++;
                lateMonth++;
                lateSeason++;
                for (int j = 0; j < people.length; j++) {
                    if(tasks.get(i).getAssign().equals(people[j])){
                        activityMonth[j] ++;
                        activitySeaon[j]++;
                    }
                }
            }
            else if(year == yearTask && month == monthTask && dayTask - day < 3){
                closeMonth++;
                closeGlobal++;
                closeSeason++;
                openGlobal++;
                openMonth++;
                openSeason++;
            }
            else if(year == yearTask && monthTask - month == 1 && dayTask < 3 && day > 28){
                if((monthTask - 1)/3 == (monthTask - 1) / 3 ){
                    closeGlobal++;
                    closeSeason++;
                    openGlobal++;
                    openSeason++;
                }
                else{
                    closeGlobal++;
                    openGlobal++;
                }
            }
            else if(year == yearTask){
                openGlobal++;
                if((monthTask - 1) / 3 == (month - 1)/3){
                    openSeason++;
                    if(monthTask == month)
                        openMonth++;
                }
            }
        }
        //set numbers in dashboard
        number_close_activity_global.setText(String.valueOf(closeGlobal));
        number_close_activity_this_month.setText(String.valueOf(closeMonth));
        number_close_activity_this_season.setText(String.valueOf(closeSeason));
        number_late_activity_global.setText(String.valueOf(lateGlobal));
        number_late_activity_this_month.setText(String.valueOf(lateMonth));
        number_late_activity_this_season.setText(String.valueOf(lateSeason));
        number_open_activity_global.setText(String.valueOf(openGlobal));
        number_open_activity_this_month.setText(String.valueOf(openMonth));
        number_open_activity_this_season.setText(String.valueOf(openSeason));
        //draw pieChart
        PieChart pieChartMonth = findViewById(R.id.pieChartMonth);
        TextView tx2 = findViewById(R.id.dashboard_no_pie2);
        graph(activityMonth, pieChartMonth, people, tx2);

        PieChart pieChartSeason = findViewById(R.id.pieChartSeason);
        TextView tx1 = findViewById(R.id.dashboard_no_pie1);
        graph(activitySeaon, pieChartSeason, people, tx1);


    }

    public void graph(int[] activity, PieChart pieChart, String[] people, TextView tx){
        //set Pie chart

        pieChart.setHoleRadius(70f);
        pieChart.setRotationEnabled(false);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setHoleColor(Color.WHITE);

        ArrayList<PieEntry> latePie = new ArrayList();
        ArrayList<String> peoplePie = new ArrayList<>();

        int count = 0;
        for (int i = 0; i < people.length; i++) {
            if (activity[i] != 0){
                peoplePie.add(people[i]);
                latePie.add(new PieEntry(activity[i], i));
            }
            else
                count++;
        }
        if (count == activity.length){
            pieChart.setVisibility(View.GONE);
            tx.setVisibility(View.VISIBLE);
        }
        else {
            PieDataSet pieDataSet = new PieDataSet(latePie, "users");
            pieDataSet.setSliceSpace(2);
            pieDataSet.setValueTextSize(15);

            ArrayList colors = new ArrayList();
            colors.add(Color.BLUE);
            colors.add(Color.GREEN);
            colors.add(Color.RED);
            colors.add(Color.LTGRAY);


            pieDataSet.setColors(colors);

            Legend legend = pieChart.getLegend();
            legend.setForm(Legend.LegendForm.CIRCLE);

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.invalidate();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.about_creator:
                navigationDrawer.aboutCreator(this);
                return true;
            case R.id.sound_setting:
                 new SoundListener(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {

        } else if (id == R.id.nav_password) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_tasks) {
            finish();
        }else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Dashboard.this, MainActivity.class));
                            finish();
                        }
                    }).setNegativeButton("No", null).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
