package com.example.taskmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
    this activity is for setting a new task or edit one
 */
public class setTask extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    Button saveTaskButton;
    String title, assign, stringTypeOfTask;
    ArrayAdapter<CharSequence> adapterTypeOfTask;
    Spinner typeOfTask;
    Task task;
    TimePickerDialog timePickerDialog;
    TextView time, date;

    Date c = Calendar.getInstance().getTime();
    //System.out.println("Current time => " + c);
    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
    String today = df.format(c);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_task);


        Intent intent = getIntent();
        task = intent.getParcelableExtra("Task");

        getSupportActionBar().setTitle("new Task");
        typeOfTask = findViewById(R.id.type_of_task);
        adapterTypeOfTask = new CustomSpinner(this, R.layout.spinner,
                getResources().getStringArray(R.array.type_of_task));
        adapterTypeOfTask.setDropDownViewResource(R.layout.spinner);
        typeOfTask.setAdapter(adapterTypeOfTask);


        Spinner period = findViewById(R.id.period);
        ArrayAdapter<CharSequence> adapterPeriod = new CustomSpinner(this,
                R.layout.spinner,getResources().getStringArray(R.array.period));
        adapterPeriod.setDropDownViewResource(R.layout.spinner);
        period.setAdapter(adapterPeriod);


        Spinner reminder = findViewById(R.id.reminder);
        ArrayAdapter<CharSequence> adapterReminder = new CustomSpinner(this,
                R.layout.spinner, getResources().getStringArray(R.array.reminder));
        adapterReminder.setDropDownViewResource(R.layout.spinner);
        reminder.setAdapter(adapterReminder);

        Spinner assignedTo = findViewById(R.id.asignedTo);
        ArrayAdapter<CharSequence> adapterAssignedTO = new CustomSpinner(this,
                R.layout.spinner, getResources().getStringArray(R.array.assigned_to));
        adapterAssignedTO.setDropDownViewResource(R.layout.spinner);
        assignedTo.setAdapter(adapterAssignedTO);


        saveTaskButton = findViewById(R.id.saveTask);
        saveTaskButton.setOnClickListener(this);

        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        date.setOnClickListener(this);
        time.setOnClickListener(this);


        if (task != null){

            EditText detail = findViewById(R.id.detail);
            detail.setText(task.getDetail());
            TextView date = findViewById(R.id.date);
            date.setText(task.getDate());
            TextView time = findViewById(R.id.time);
            time.setText(task.getTime());
            EditText title = findViewById(R.id.title);
            title.setText(task.getTitle());
            time.setText(task.getTime());
            date.setText(task.getDate());
            String[] periodArray = getResources().getStringArray(R.array.period);
            for (int i = 0; i < periodArray.length; i++) {
                if (periodArray[i].equals(task.getPeriod())){
                    period.setSelection(i);
                    break;
                }
            }

            String[] reminderArray = getResources().getStringArray(R.array.reminder);
            for (int i = 0; i < reminderArray.length; i++) {
                if (reminderArray[i].equals(task.getReminder())){
                    reminder.setSelection(i);
                    break;
                }
            }
            String[] AssignedArray = getResources().getStringArray(R.array.assigned_to);
            for (int i = 0; i < AssignedArray.length; i++) {
                if (AssignedArray[i].equals(task.getAssign())){
                    assignedTo.setSelection(i);
                    break;
                }
            }
            String[] typeOfTask = getResources().getStringArray(R.array.type_of_task);
            for (int i = 0; i < typeOfTask.length; i++) {
                if (typeOfTask[i].equals(task.getTypeOfTask())){
                    this.typeOfTask.setSelection(i);
                    break;
                }

            }


        }
        else{


            time.setText("09:30");
            this.date.setText(today);
        }


    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.type_of_task:
                break;
            default:
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveTask:

                EditText editTextTitle = (EditText) findViewById(R.id.title);
                title = editTextTitle.getText().toString();
                Spinner spinnerAssign = findViewById(R.id.asignedTo);
                assign = spinnerAssign.getSelectedItem().toString();
                Spinner spinner = findViewById(R.id.type_of_task);
                String typeOfTask = spinner.getSelectedItem().toString();

                //pass data back to the navigationDrawer
                Spinner period = findViewById(R.id.period);
                Spinner reminder = findViewById(R.id.reminder);
                if (assign.equals("Assigned to") || typeOfTask.equals("Type of task")
                        || reminder.getSelectedItem().toString().equals("Reminder") ||
                        period.getSelectedItem().toString().equals("Period")){
                    Toast.makeText(this, "you should select items in spinners",Toast.LENGTH_LONG).show();
                }

                else{

                    Intent intent = new Intent();
                    Task task = new Task(assign, typeOfTask, title);
                    task.setDate(date.getText().toString());
                    task.setTime(time.getText().toString());
                    EditText detail = findViewById(R.id.detail);
                    task.setDetail(detail.getText().toString());
                    task.setPeriod(period.getSelectedItem().toString());
                    task.setReminder(reminder.getSelectedItem().toString());
                    intent.putExtra("task", task);


                    setResult(RESULT_OK, intent);

                    Toast.makeText(this, "save succeed", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.time:
                timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay + ":" + minute);
                    }
                }, 9 ,30, true);
                timePickerDialog.show();
                break;
            case R.id.date:
                String[] day = today.split("/");
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(year + "/" +month+"/"+dayOfMonth);
                    }
                }, Integer.parseInt(day[0]), Integer.parseInt(day[1]),Integer.parseInt(day[2])).show();
                break;
            default:
                Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();

        }
    }


}
