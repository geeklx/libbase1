package com.geek.libpicturerecognitiontext;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.geek.libpicturerecognitiontext.cropview.CropView;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 源库地址 implementation 'com.rmtheis:tess-two:9.1.0'
 */
public class PicturecognitionTextActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Button recBtn;
    private TextView resultTv;
    private CropView cropView;

    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private List<ImageView> list;
    private int[] ids = new int[]{R.drawable.chaxun, R.drawable.fan, R.drawable.fengying,
            R.drawable.meishi, R.drawable.mj, R.drawable.mjn, R.drawable.quanbu, R.drawable.tupian, R.drawable.yingyu, R.drawable.zheng,
            R.drawable.xingming, R.drawable.minzu, R.drawable.zengzhi, R.drawable.wode};

    private Button pickBtn;
    private Button widthBtn, heightBtn, cropRecBtn;
    private ImageView resultIv;
    private ScrollView scrollView;
    private Spinner traineddataSp;

    private BitmapUtils bitmapUtils = new BitmapUtils();

    /**
     * TessBaseAPI初始化用到的第一个参数，是个目录。
     */
//    private static final String DATAPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
//    private final String DATAPATH = getSDPath(PicturecognitionTextActivity.this);//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator;
    /**
     * 在DATAPATH中新建这个目录，TessBaseAPI初始化要求必须有这个目录。
     */
    private String tessdata; //= DATAPATH + File.separator + "tessdata";
    /**
     * TessBaseAPI初始化测第二个参数，就是识别库的名字不要后缀名。
     */
    private static String DEFAULT_LANGUAGE = "chi_sim";
    /**
     * assets中的文件名
     */
    private static String DEFAULT_LANGUAGE_NAME = DEFAULT_LANGUAGE + ".traineddata";
    /**
     * 保存到SD卡中的完整文件名
     */
    private  String LANGUAGE_PATH ;//= tessdata + File.separator + DEFAULT_LANGUAGE_NAME;

    /**
     * 权限请求值
     */
    private static final int PERMISSION_REQUEST_CODE = 0;

    private static final int PICK_REQUEST_CODE = 10;

    /**
     * 截图框的高
     */
    private int cropHeight;
    /**
     * 截图框的宽
     */
    private int cropWidth;

    float lastX;

    /**
     * 判断高低版本获取跟目录
     */
    public static String getSDPath(Context context) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
        if (sdCardExist) {
            if (Build.VERSION.SDK_INT >= 29) {
                //Android10之后
                sdDir = context.getExternalFilesDir(null);
            } else {
                sdDir = Environment.getExternalStorageDirectory();// 获取SD卡根目录
            }
        } else {
            sdDir = Environment.getRootDirectory();// 获取跟目录
        }
        return sdDir.toString();
    }
    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_picturecognition_text);
        scrollView = (ScrollView) findViewById(R.id.sv);
        recBtn = (Button) findViewById(R.id.btn_rec);
        pickBtn = (Button) findViewById(R.id.btn_pick);
        widthBtn = (Button) findViewById(R.id.btn_width);
        heightBtn = (Button) findViewById(R.id.btn_height);
        cropRecBtn = (Button) findViewById(R.id.btn_crop_rec);
        resultTv = (TextView) findViewById(R.id.tv);
        cropView = (CropView) findViewById(R.id.iv);
        resultIv = (ImageView) findViewById(R.id.iv_result);
        viewPager = (ViewPager) findViewById(R.id.vp);
        traineddataSp = (Spinner) findViewById(R.id.sp_traineddata);

        recBtn.setOnClickListener(this);
        pickBtn.setOnClickListener(this);
        widthBtn.setOnClickListener(this);
        heightBtn.setOnClickListener(this);
        cropRecBtn.setOnClickListener(this);

        resultIv.setVisibility(View.INVISIBLE);
        cropView.setVisibility(View.INVISIBLE);

        initList();
        adapter = new MyPagerAdapter(list);
        viewPager.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }

        //Android6.0之前安装时就能复制，6.0之后要先请求权限，所以6.0以上的这个方法无用。
        LANGUAGE_PATH= getSDPath(PicturecognitionTextActivity.this) + File.separator + DEFAULT_LANGUAGE_NAME;
        SDUtils.assets2SD(getApplicationContext(), LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);


        //一个可以双指缩放移动的控件，解决滑动冲突
        cropView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //允许ScrollView截断点击事件，ScrollView可滑动
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    //不允许ScrollView截断点击事件，点击事件由子View处理
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        //解决viewPager的滑动冲突
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    // 记录点击到ViewPager时候，手指的X坐标
                    lastX = event.getX();
                }
                if (action == MotionEvent.ACTION_MOVE) {
                    // 超过阈值
                    if (Math.abs(event.getX() - lastX) > 30f) {
                        viewPager.setEnabled(false);
                        scrollView.requestDisallowInterceptTouchEvent(true);
                    }
                }
                if (action == MotionEvent.ACTION_UP) {
                    // 用户抬起手指，恢复父布局状态
                    scrollView.requestDisallowInterceptTouchEvent(false);
                    viewPager.setEnabled(true);
                }
                return false;
            }
        });

        traineddataSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] array = getResources().getStringArray(R.array.traineddata);
                if (position == 0) {
                    DEFAULT_LANGUAGE = array[1];
                } else {
                    DEFAULT_LANGUAGE = array[position];
                }
                DEFAULT_LANGUAGE_NAME = DEFAULT_LANGUAGE + ".traineddata";
                LANGUAGE_PATH = getSDPath(PicturecognitionTextActivity.this) + File.separator + "tessdata" + File.separator + DEFAULT_LANGUAGE_NAME;
                Toast.makeText(getApplicationContext(), array[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 识别图像
     *
     * @param bitmap
     */
    private void recognition(final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String text = "";
                if (!checkTraineddataExists()) {
                    text += LANGUAGE_PATH + "不存在，开始复制\r\n";
                    Log.i(TAG, "run: " + LANGUAGE_PATH + "不存在，开始复制\r\n");
                    SDUtils.assets2SD(getApplicationContext(), LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
                }
                text += LANGUAGE_PATH + "已经存在，开始识别\r\n";
                Log.i(TAG, "run: " + LANGUAGE_PATH + "已经存在，开始识别\r\n");
                long startTime = System.currentTimeMillis();
                Log.i(TAG, "run: kaishi " + startTime);
                TessBaseAPI tessBaseAPI = new TessBaseAPI();
                tessBaseAPI.init( getSDPath(PicturecognitionTextActivity.this), DEFAULT_LANGUAGE);
                tessBaseAPI.setImage(bitmap);
                text = text + "识别结果：" + tessBaseAPI.getUTF8Text();
                long finishTime = System.currentTimeMillis();
                Log.i(TAG, "run: jieshu " + finishTime);
                Log.i(TAG, "run: text " + text);
                text = text + "\r\n" + " 耗时" + (finishTime - startTime) + "毫秒";
                final String finalText = text;
                final Bitmap finalBitmap = bitmap;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultTv.setText(finalText);
                        cropView.setImageBitmap(finalBitmap);
                    }
                });
                tessBaseAPI.end();
            }
        }).start();
    }

    private void initList() {
        list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageResource(ids[i]);
            list.add(imageView);
        }

    }

    public boolean checkTraineddataExists() {
        File file = new File(LANGUAGE_PATH);
        return file.exists();
    }

    /**
     * 请求到权限后在这里复制识别库
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult: " + grantResults[0]);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult: copy");
                    SDUtils.assets2SD(getApplicationContext(), LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_REQUEST_CODE) {
                Uri source = data.getData();
                cropHeight = 1;
                cropWidth = 1;
                cropView.setVisibility(View.VISIBLE);
                resultIv.setVisibility(View.INVISIBLE);
                cropView.of(source).withAspect(cropWidth, cropHeight).initialize(PicturecognitionTextActivity.this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_rec) {
            final Bitmap[] bitmap = {BitmapFactory.decodeResource(getResources(), ids[viewPager.getCurrentItem()])};
            recognition(bitmap[0]);
        } else if (id == R.id.btn_pick) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivityForResult(intent, PICK_REQUEST_CODE);
        } else if (id == R.id.btn_height) {
            cropHeight++;
            cropView.setHeight(cropHeight);
        } else if (id == R.id.btn_width) {
            cropWidth++;
            cropView.setWidth(cropWidth);
        } else if (id == R.id.btn_crop_rec) {
            if (cropView.getVisibility() != View.VISIBLE) {
                Toast.makeText(getApplicationContext(), "先选一张图片", Toast.LENGTH_SHORT).show();
                return;
            }
            Bitmap bt = cropView.getOutput();
            cropView.setVisibility(View.INVISIBLE);
            resultIv.setVisibility(View.VISIBLE);
            resultIv.setImageBitmap(bt);
            recognition(bt);
        }
    }
}
