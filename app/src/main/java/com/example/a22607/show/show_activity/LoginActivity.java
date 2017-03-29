package com.example.a22607.show.show_activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a22607.show.R;
import com.example.a22607.show.httputils.UserDao;
import com.example.a22607.show.model.User;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

import static com.example.a22607.show.R.id.et_password;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(et_password)
    EditText etPassword;
    @BindView(R.id.layout_input)
    LinearLayout layoutInput;
    @BindView(R.id.tvLogin_Question)
    TextView tvLoginQuestion;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.imgWeixin)
    ImageView imgWeixin;
    @BindView(R.id.imgAlipay)
    ImageView imgAlipay;
    @BindView(R.id.layout_bottom_image)
    LinearLayout layoutBottomImage;
    @BindView(R.id.tv_weixin)
    TextView tvWeixin;
    @BindView(R.id.tv_alipay)
    TextView tvAlipay;
    @BindView(R.id.layout_bottom_tv)
    LinearLayout layoutBottomTv;
    @BindView(R.id.cb_remember)
    CheckBox cbRemember;
    @BindView(R.id.line_username)
    View lineUsername;
    @BindView(R.id.line_password)
    View linePassword;
    private boolean isEmptyName;
    private boolean isEmptyPassword;


    //网络连接，进行用户信息的认证。
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            String username=etUsername.getText().toString().trim();
            String userpasswd=etPassword.getText().toString().trim();
            String userinfo=UserDao.getUser(username,userpasswd);
            if(userinfo!=null){
                Gson gson=new Gson();
                User user=gson.fromJson(userinfo,User.class);
                Log.i("userinfo",user.getUemail()+user.getUsex());
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initListener();
        readAccount();
    }


    /**
     * 设置登录按钮是否激活
     */
    private void setBtnLoginEnable() {
        if (!isEmptyName && !isEmptyPassword) {
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setEnabled(false);
        }
    }


    @OnClick({R.id.tv_register, R.id.tvLogin_Question, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                // 这里跳转到注册界面
                startActivityForResult(new Intent("activity.register"), 0x001);
                break;
            case R.id.tvLogin_Question:
                // 这里跳转到登录遇到问题界面
                Toast.makeText(this, "暂时无法为您解决这个问题，请仔细查看用户名和密码是否正确，或者与管理员联系。", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_login:
                // 这里进行登录请求认证
                Thread thread=new Thread(runnable);
                thread.start();

                break;
        }
    }

    @OnTouch(value = {R.id.et_username, et_password})
    public boolean onTouch(View v) {
        switch (v.getId()) {
            case R.id.et_username:
                lineUsername.setBackgroundColor(0xFF1F81F8);
                linePassword.setBackgroundColor(0xffaba7a7);
                break;
            case et_password:
                linePassword.setBackgroundColor(0xFF1F81F8);
                lineUsername.setBackgroundColor(0xffaba7a7);
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("是否退出登录")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.this.finish();
                    }
                }).setNegativeButton("取消", null)
                .show();
    }
    //读取用户账户信息

    //读取保存在本地的用户名和密码
    public void readAccount() {

        //创建SharedPreferences对象
        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);

        //获得保存在SharedPredPreferences中的用户名和密码
        String username = sp.getString("username", "");
        String password = sp.getString("password", "");

        //在用户名和密码的输入框中显示用户名和密码
        etUsername.setText(username);
        etPassword.setText(password);
    }


    /**
     * 初始化用户名和密码输入框的监听，当两个输入框都不为空时，才激活登录按钮
     */
    private void initListener() {
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()))
                    isEmptyName = true;
                isEmptyName = false;
                setBtnLoginEnable();
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()))
                    isEmptyPassword = true;
                isEmptyPassword = false;
                setBtnLoginEnable();
            }
        });
    }


}
