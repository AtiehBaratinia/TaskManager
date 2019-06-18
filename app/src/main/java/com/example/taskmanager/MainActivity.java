package com.example.taskmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginButton;
    ArrayList<Person> people;

    private final static int REQUEST_CODE_1 = 1;
    TextView email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        Button create = findViewById(R.id.signIn);
        create.setOnClickListener(this);
        people = new ArrayList<Person>();
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
            case R.id.signIn:
                Intent intent1 = new Intent(this, Login.class);
                startActivityForResult(intent1, REQUEST_CODE_1);
                break;
        }
    }

    //this method is invoked after it gets data from login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);

        // The returned result data is identified by requestCode.
        // The request code is specified in startActivityForResult(intent, REQUEST_CODE_1); method.
        switch (requestCode)
        {
            // This request code is set by startActivityForResult(intent, REQUEST_CODE_1) method.
            case REQUEST_CODE_1:

                if(resultCode == RESULT_OK)
                {
                    //String messageReturn = dataIntent.getStringExtra("message_return");
                    //textView.setText(messageReturn);

                    Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
                    Bundle bundle = dataIntent.getExtras();
                    if (bundle != null) {
                        Person person = (Person) bundle.getParcelable("person");
                        people.add(person);

                    }else
                        Toast.makeText(this, "error",Toast.LENGTH_SHORT).show();
                }
        }
    }
}
