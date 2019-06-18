package com.example.taskmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText email, password, firstName, lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.emailCreate);
        password = findViewById(R.id.passwordCreate);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);

        Button create = findViewById(R.id.createButton);
        create.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createButton:
                String firstName = this.firstName.getText().toString();
                String lastName = this.lastName.getText().toString();
                String email = this.email.getText().toString();
                String password = this.password.getText().toString();
                if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    Toast.makeText(this,"You should fill the boxes", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent();
                    Person person = new Person(firstName, lastName, password, email);
                    intent.putExtra("person", person);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;

        }
    }
}
