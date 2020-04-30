package com.pamudi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.chamathka.medicinereminder.R;

public class account extends AppCompatActivity {
    DatabaseHelper db;
    EditText mTextUsername;
    EditText mTextname;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    Button mButtonUpdate;
    TextView mTextViewLogOut,mTextusername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        String userDetails = getIntent().getStringExtra("username");

        db = new DatabaseHelper(this);
        mTextUsername = (EditText)findViewById(R.id.edittext_username);
        mTextname = (EditText)findViewById(R.id.txt_name);

        mButtonUpdate = (Button)findViewById(R.id.button_register);
        mTextViewLogOut = (TextView)findViewById(R.id.textview_login);
        mTextusername = (TextView)findViewById(R.id.userName);
        mTextPassword = (EditText)findViewById(R.id.edittext_password);
        mTextCnfPassword = (EditText)findViewById(R.id.edittext_cnf_password);
        mTextusername.setText(getIntent().getStringExtra("userDetails"));
        mTextUsername.setText(getIntent().getStringExtra("userDetails"));

        mTextViewLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(account.this,MainActivity.class);
                startActivity(registerIntent);
            }
        });

        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mTextUsername.getText().toString().trim();
                String name = mTextname.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                String cnf_pwd = mTextCnfPassword.getText().toString().trim();
                if(pwd.equals(cnf_pwd)){
                    boolean val = db.update(user,name,pwd);
                    if(val == true){
                        Toast.makeText(account.this,"Updated Success", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(account.this,"Update Error", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });
    }
}
