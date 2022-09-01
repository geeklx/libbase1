package com.geek.libfacedetect.activity;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.geek.libfacedetect.R;
import com.geek.libfacedetect.db.DatabaseHelper;
import com.geek.libfacedetect.db.UserInfo;
import com.geek.libfacedetect.util.PermissionHelper;
import com.geek.libfacedetect.util.ToastUtil;

import me.jessyan.autosize.AutoSizeCompat;

/**
 * @author fosung
 * 人脸识别
 * com.github.geeklx.libbase1:libfacedetect:2.0.6
 */
public class MainActivityfdt extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainfacedetector);
        Button registerButton1 = findViewById(R.id.register1);
        Button verifyButton1 = findViewById(R.id.verify1);
        Button registerButton2 = findViewById(R.id.register2);
        Button verifyButton2 = findViewById(R.id.verify2);
        Button registerButton3 = findViewById(R.id.register3);
        Button verifyButton3 = findViewById(R.id.verify3);
        Button viewDataButton = findViewById(R.id.view_data);

        registerButton1.setOnClickListener(this);
        registerButton2.setOnClickListener(this);
        registerButton3.setOnClickListener(this);
        viewDataButton.setOnClickListener(this);
        verifyButton1.setOnClickListener(this);
        verifyButton2.setOnClickListener(this);
        verifyButton3.setOnClickListener(this);
        initDatabase();

        //


    }

    @Override
    public Resources getResources() {
        //需要升级到 v1.1.2 及以上版本才能使用 AutoSizeCompat
        if (Looper.myLooper() == Looper.getMainLooper()) {
            AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));//如果没有自定义需求用这个方法
            AutoSizeCompat.autoConvertDensity((super.getResources()), 667, false);//如果有自定义需求就用这个方法
        }
        return super.getResources();
    }

    // 初始化数据库
    private void initDatabase() {
        DatabaseHelper helper = new DatabaseHelper(this);
//        if (helper.query().size() == 0) {
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//                    R.drawable.user_defaut);
//            String path = helper.saveBitmapToLocal(bitmap);
//            UserInfo user = new UserInfo("默认用户", "男", 25, path);
//            helper.insert(user);
//        }
        helper.close();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.register1) {
            requestCameraPermission(new PermissionHelper.RequestListener() {
                @Override
                public void onGranted() {
                    Intent intent = new Intent(MainActivityfdt.this,
                            DetectActivity11.class);
                    intent.putExtra("flag", DetectActivity11.FLAG_REGISTER);
                    startActivityForResult(intent,
                            DetectActivity11.FLAG_REGISTER);
                }

                @Override
                public void onDenied() {
                    ToastUtil.showToast(MainActivityfdt.this, "权限拒绝", 0);
                }
            });
        } else if (id == R.id.verify1) {
            requestCameraPermission(new PermissionHelper.RequestListener() {
                @Override
                public void onGranted() {
                    Intent intent = new Intent(MainActivityfdt.this,
                            DetectActivity12.class);
                    intent.putExtra("flag", DetectActivity12.FLAG_VERIFY);
                    startActivityForResult(intent,
                            DetectActivity12.FLAG_VERIFY);
                }

                @Override
                public void onDenied() {
                    ToastUtil.showToast(MainActivityfdt.this, "权限拒绝", 0);
                }
            });
        } else if (id == R.id.register2) {
            requestCameraPermission(new PermissionHelper.RequestListener() {
                @Override
                public void onGranted() {
                    Intent intent = new Intent(MainActivityfdt.this,
                            DetectActivity21.class);
                    intent.putExtra("flag", DetectActivity21.FLAG_REGISTER);
                    startActivityForResult(intent,
                            DetectActivity21.FLAG_REGISTER);
                }

                @Override
                public void onDenied() {
                    ToastUtil.showToast(MainActivityfdt.this, "权限拒绝", 0);
                }
            });
        } else if (id == R.id.verify2) {
            requestCameraPermission(new PermissionHelper.RequestListener() {
                @Override
                public void onGranted() {
                    Intent intent = new Intent(MainActivityfdt.this,
                            DetectActivity22.class);
                    intent.putExtra("flag", DetectActivity22.FLAG_VERIFY);
                    startActivityForResult(intent,
                            DetectActivity22.FLAG_VERIFY);
                }

                @Override
                public void onDenied() {
                    ToastUtil.showToast(MainActivityfdt.this, "权限拒绝", 0);
                }
            });
        } else if (id == R.id.register3) {
            requestCameraPermission(new PermissionHelper.RequestListener() {
                @Override
                public void onGranted() {
                    Intent intent = new Intent(MainActivityfdt.this,
                            DetectActivity31.class);
                    intent.putExtra("flag", DetectActivity31.FLAG_REGISTER);
                    startActivityForResult(intent,
                            DetectActivity31.FLAG_REGISTER);
                }

                @Override
                public void onDenied() {
                    ToastUtil.showToast(MainActivityfdt.this, "权限拒绝", 0);
                }
            });
        } else if (id == R.id.verify3) {
            requestCameraPermission(new PermissionHelper.RequestListener() {
                @Override
                public void onGranted() {
                    Intent intent = new Intent(MainActivityfdt.this,
                            DetectActivity32.class);
                    intent.putExtra("flag", DetectActivity32.FLAG_VERIFY);
                    startActivityForResult(intent,
                            DetectActivity32.FLAG_VERIFY);
                }

                @Override
                public void onDenied() {
                    ToastUtil.showToast(MainActivityfdt.this, "权限拒绝", 0);
                }
            });
        } else if (id == R.id.view_data) {
            startActivity(new Intent(MainActivityfdt.this, ViewDataActivityfdt.class));
        }
    }

    private void requestCameraPermission(PermissionHelper.RequestListener listener) {
        PermissionHelper.with(MainActivityfdt.this)
                .requestPermission(Manifest.permission.CAMERA)
                .requestCode(1)
                .setListener(listener)
                .request();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DetectActivity.FLAG_REGISTER:
                if (resultCode == RESULT_OK) {
                    ToastUtil.showToast(this, "已注册过", 1);
                }
                break;
            case DetectActivity.FLAG_VERIFY:
                if (resultCode == RESULT_OK) {
                    int index = data.getIntExtra("USER_ID", -1);
                    DatabaseHelper helper = new DatabaseHelper(this);
                    UserInfo user = helper.query().get(index);
                    helper.close();
                    ToastUtil.showToast(this, "验证通过: " + user.getName(), 1);
                } else {
                    ToastUtil.showToast(this, "验证失败", 1);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.requestPermissionResult(requestCode, grantResults);
    }
}
