package com.example.musicplayer1_0.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musicplayer1_0.R;

import org.litepal.LitePal;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameEdit = (EditText)findViewById(R.id.username);
        passwordEdit = (EditText)findViewById(R.id.password);
        rememberPass = (CheckBox)findViewById(R.id.remember_password);
        login = (Button)findViewById(R.id.login);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember){
            String username = pref.getString("username","");
            String password = pref.getString("password","");
            usernameEdit.setText(username);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        //设置监听器
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if (username.equals("user")&&password.equals("123456")){
                    editor = pref.edit();
                    if (rememberPass.isChecked()){                                           //检查是否记住密码
                        editor.putBoolean("remember_password", true);
                        editor.putString("username",username);
                        editor.putString("password",password);
                    }else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
//                    LitePal.getDatabase();                                                   //创建音乐数据库
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

