package com.example.taskmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;

public class navigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener {

    ArrayList <Task>tasks;
    ArrayList<Task> orderTasks;
    public final static int REQUEST_CODE_1 = 1, REQUEST_CODE_2 = 2;
    SwipeRecyclerViewAdapter listAdapter;
    TextView nullTextView;
    Spinner typeOfTaskSpinner, createdBySpinner, assignedToSpinner, taskStateSpinner;
    RecyclerView recyclerView;
    SearchView searchView;
    String email;
    DataBaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        Intent intent = getIntent();
        //tasks = getIntent().getParcelableArrayListExtra("task");
        email = intent.getStringExtra("email");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setTitle("Menu");


        if (tasks == null) {
            tasks = new ArrayList();
        }




        setTitle("Tasks");
        //search view

        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);

        //set spinner
        typeOfTaskSpinner = (Spinner) findViewById(R.id.typeOfTaskManager);
        ArrayAdapter<CharSequence> typeOfTaskAdapter = ArrayAdapter.createFromResource(
                this, R.array.type_of_task_manager, android.R.layout.simple_spinner_item);
        typeOfTaskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfTaskSpinner.setAdapter(typeOfTaskAdapter);
        typeOfTaskSpinner.setOnItemSelectedListener(this);
        typeOfTaskSpinner.setPrompt("type of task");

        assignedToSpinner = findViewById(R.id.assignedToManager);
        ArrayAdapter<CharSequence> assignedToAdapter = ArrayAdapter.createFromResource(
                this, R.array.assigned_to_manager, android.R.layout.simple_spinner_item);
        assignedToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assignedToSpinner.setAdapter(assignedToAdapter);
        assignedToSpinner.setOnItemSelectedListener(this);
        assignedToSpinner.setPrompt("Assigned to");

        //floating action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        //create Database
        db = new DataBaseHelper(this,"Tasks",1);
        // db.deleteAllData();



        Cursor res = db.getAllData();
        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                String id = res.getString(0);
                String assign = res.getString(1);
                String type = res.getString(2);
                String title = res.getString(3);
                String date = res.getString(4);
                String time = res.getString(5);
                String period = res.getString(6);
                String reminder = res.getString(7);
                String detail = res.getString(8);
                Task task = new Task(assign, type, title);
                task.setDate(date);
                task.setDetail(detail);
                task.setPeriod(period);
                task.setReminder(reminder);
                task.setTime(time);
                task.setId(id);
                tasks.add(task);
                orderTasks = orderListview(tasks);
                //new AlertDialog.Builder(this).setMessage(id + "\n" + type + "\n" + assign);
            }
        }
        //set first tasks

        orderTasks = tasks;
        recyclerView = findViewById(R.id.my_recycler_view);


        nullTextView = (TextView) findViewById(R.id.empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new SwipeRecyclerViewAdapter(this, orderTasks);




        ((SwipeRecyclerViewAdapter) listAdapter).setMode(Attributes.Mode.Single);

        recyclerView.setAdapter(listAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        if(tasks.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            nullTextView.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            nullTextView.setVisibility(View.GONE);
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Intent intent = new Intent(navigationDrawer.this, setTask.class);
                startActivityForResult(intent, REQUEST_CODE_1);
                break;



        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.sandwich) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            Intent intent = new Intent(this, Dashboard.class);
            intent.putExtra("task", tasks);
            startActivity(intent);
        } else if (id == R.id.nav_password) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_tasks) {

        }else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(navigationDrawer.this, MainActivity.class));
                            finish();
                        }
                    }).setNegativeButton("No", null).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        orderTasks = orderListview(tasks);
        listAdapter.filterList(orderTasks);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String searchString = searchView.getQuery().toString();
        orderTasks = searchListview(tasks, searchString);
        listAdapter.filterList(orderTasks);
        return true;
    }


    // This method is invoked when target activity return result data back.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);

        // The returned result data is identified by requestCode.
        // The request code is specified in startActivityForResult(intent, REQUEST_CODE_1); method.
        switch (requestCode)
        {
            // This request code is set by startActivityForResult(intent, REQUEST_CODE_1) method.
            case REQUEST_CODE_1:
                System.out.println("navi result code");
                if(resultCode == RESULT_OK)
                {
                    //String messageReturn = dataIntent.getStringExtra("message_return");
                    //textView.setText(messageReturn);

                    System.out.println("navi result ok");
                    Bundle bundle = dataIntent.getExtras();
                    if (bundle != null) {
                        System.out.println("2");
                        Task task = (Task) bundle.getParcelable("task");
                        String edit = bundle.getString("edit");
                        if (edit.equals("true")){
                            db.updateData(task);
                            for (int i = 0; i < tasks.size(); i++) {
                                if (task.getId().equals(tasks.get(i).getId())){
                                    tasks.set(i,task);
                                }
                            }

                            orderTasks = orderListview(tasks);
                            listAdapter.filterList(orderTasks);
                            System.out.println("id + " + task.getId() + " type + " + task.getTypeOfTask());


                        }else {
                            tasks.add(task);
                            orderTasks = orderListview(tasks);
                            listAdapter.filterList(orderTasks);


                            boolean flag = db.insertData(task);
                            if (flag) {
                                Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                            }


                        }
                        recyclerView.setVisibility(View.VISIBLE);
                        nullTextView.setVisibility(View.GONE);
                    }else
                        Toast.makeText(this, "error",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
    private ArrayList<Task> orderListview(ArrayList<Task> tasks){
        ArrayList<Task> result = new ArrayList();
        String typeSelected = typeOfTaskSpinner.getSelectedItem().toString();
        String assignSelected = assignedToSpinner.getSelectedItem().toString();
        for (int i = 0; i < tasks.size(); i++) {
            String type = tasks.get(i).getTypeOfTask();
            String assign = tasks.get(i).getAssign();
            if (assignSelected.equals("All") || assign.equals(assignSelected)){
                if(typeSelected.equals("All") || typeSelected.equals(type)){
                    result.add(tasks.get(i));
                }
            }
        }
        return result;
    }
    private ArrayList<Task> searchListview(ArrayList<Task> tasks, String searchString){
        ArrayList result = new ArrayList();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTitle().toLowerCase().contains(searchString.toLowerCase())){
                result.add(tasks.get(i));
            }
        }
        return result;
    }

}
