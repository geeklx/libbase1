package com.geek.libfacedetect.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.geek.libfacedetect.R;
import com.geek.libfacedetect.db.DatabaseHelper;
import com.geek.libfacedetect.db.UserInfo;
import com.geek.libfacedetect.util.ToastUtil;

/**
 * 注册页
 * Created by Luke on 2017/8/21.
 */

public class RegisterActivityfdt extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final String FEMALE = "男", MALE = "女";
    private EditText userName;
    private RadioGroup userSex;
    private EditText userAge;
    private Button register;
    private ImageView imageView;
    private UserInfo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerfacedetector);
        imageView = (ImageView) findViewById(R.id.image_view);
        userName = (EditText) findViewById(R.id.user_name);
        userSex = (RadioGroup) findViewById(R.id.user_sex);
        userAge = (EditText) findViewById(R.id.user_age);
        register = (Button) findViewById(R.id.register);

        init();
    }

    private void init() {
        user = new UserInfo();
        Bitmap face = getIntent().getParcelableExtra("Face");
        imageView.setImageBitmap(face);
        userSex.check(R.id.female);
        user.setSex(FEMALE);
        userSex.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            RadioGroup group, int checkedId) {
                        if (checkedId == R.id.female) {
                            user.setSex(FEMALE);
                        } else {
                            user.setSex(MALE);
                        }
                    }
                });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitUserInfo();
            }
        });
    }

    public void submitUserInfo() {
        if (!TextUtils.isEmpty(userName.getText()) &&
                !TextUtils.isEmpty(userAge.getText())) {
            user.setName(userName.getText().toString());
            user.setAge(Integer.parseInt(userAge.getText().toString()));
            DatabaseHelper helper = new DatabaseHelper(RegisterActivityfdt.this);
            Bitmap bitmap = getIntent().getParcelableExtra("Face");
            String path = helper.saveBitmapToLocal(bitmap);
            user.setPath(path);
            Log.d(TAG, "submitUserInfo: " + user.toString());
            helper.insert(user);
            helper.close();
            ToastUtil.showToast(RegisterActivityfdt.this, "注册成功", 0);
            finish();
        } else {
            ToastUtil.showToast(RegisterActivityfdt.this, "注册信息不完整，无法注册", 0);
        }
    }
}
