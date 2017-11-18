package bmob.yellowdoing.com.bmobcommunity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtUsername, mEtPassword;
    private TextView mTvRegister;
    private Button mBtLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mTvRegister = (TextView) findViewById(R.id.tv_regitser);
        mBtLogin = (Button) findViewById(R.id.bt_login);

        mBtLogin.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login: //登陆
             //   login();
                break;
            case R.id.tv_regitser: //注册
             //   register();
                break;
        }

    }
/*
    private void login() {
        User user = new User();
        user.setUsername(mEtUsername.getText().toString());
        user.setPassword(mEtPassword.getText().toString());
        user.login(new SaveListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_register, null);
        final EditText username = (EditText) view.findViewById(R.id.et_username);
        final EditText password = (EditText) view.findViewById(R.id.et_password);

        new AlertDialog.Builder(this)
                .setView(view)
                .setNegativeButton("注册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User user = new User();
                        user.setUsername(username.getText().toString());
                        user.setPassword(password.getText().toString());
                        user.signUp(new SaveListener<Object>() {
                            @Override
                            public void done(Object o, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }*/
}
