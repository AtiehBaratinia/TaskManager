package com.example.taskmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditTask extends AppCompatActivity implements View.OnClickListener {
    Button updateTask;
    Task task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        task = intent.getParcelableExtra("Task");
        updateTask = findViewById(R.id.update_task_button);
        updateTask.setOnClickListener(this);

        TextView assign = (TextView) findViewById(R.id.assign_edit_task);
        TextView detail = (TextView) findViewById(R.id.detail_edit_task);
        TextView close = (TextView) findViewById(R.id.close_edit_task);
        TextView date = (TextView) findViewById(R.id.date_edit_task);

        TextView title = (TextView) findViewById(R.id.title_edit_task);
        TextView typeOfTask = (TextView) findViewById(R.id.type_of_task_edit_task);
        title.setText(task.getTitle());
        typeOfTask.setText(task.getTypeOfTask());

        assign.setText(task.getAssign());
        detail.setText(task.getDetail());

        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String date1 = df.format(c);

        String[] split = date1.split("/");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        String temp = task.getDate();
        String[]  taskDate = temp.split("/");
        int yearTask = Integer.parseInt(taskDate[0]);
        int monthTask = Integer.parseInt(taskDate[1]);
        int dayTask = Integer.parseInt(taskDate[2]);

        if (yearTask == year && month == monthTask && day <= dayTask && day + 2 >= dayTask) {
            close.setVisibility(View.VISIBLE);
        }
        if (yearTask == year && month == monthTask - 1 && day >= 28 && 2 >= dayTask) {
            close.setVisibility(View.VISIBLE);
        }
        date.setText(task.getDate() +" at "+ task.getTime() + " (duration " + task.getPeriod() + " )");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_task_button:
                Intent intent = new Intent(this, setTask.class);
                intent.putExtra("Task",task);
                startActivity(intent);
                finish();
        }
    }
}
