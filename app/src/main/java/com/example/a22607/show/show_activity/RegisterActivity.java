package com.example.a22607.show.show_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.a22607.show.R;
import com.example.a22607.show.httputils.UserDao;
import com.example.a22607.show.model.User;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    @BindView(R.id.et_phone)
    @NotEmpty(message = "号码不能为空", trim = true)
    EditText etPhone;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.et_username)
    @NotEmpty(message = "用户名不能为空")
    EditText etUsername;
    @BindView(R.id.et_password)
    @Password(message = "密码不能为空，且不小于6位")
    EditText etPassword;
    @BindView(R.id.et_email)
    @Email(message = "邮箱格式不正确")
    EditText etEmail;


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            User user = new User();
            user.setUemail(etEmail.getText().toString().trim());
            user.setUname(etUsername.getText().toString().trim());
            user.setUpasswd(etPassword.getText().toString().trim());
            String status = UserDao.saveUser(user);
            Log.i("registerinfo", status);
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    btnClear.setVisibility(View.GONE);
                    btnNext.setEnabled(false);
                } else {
                    btnClear.setVisibility(View.VISIBLE);
                    btnNext.setEnabled(true);
                }
            }
        });
    }


    @OnClick(R.id.btn_next)
    public void onViewClicked() {
     Thread thread=new Thread(runnable);
        thread.start();
    }
}
