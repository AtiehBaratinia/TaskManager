package com.example.taskmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginButton;
    TextView email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                String email = this.email.getText().toString();
                String password = this.password.getText().toString();

                Intent intent = new Intent(MainActivity.this, navigationDrawer.class);
                intent.putExtra("email",email);
                startActivity(intent);
                finish();
                break;
        }
    }
}
