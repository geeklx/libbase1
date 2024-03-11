package com.fosung.lighthouse.fosunglibs1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

import com.fosung.lighthouse.fosunglibs1.sm4.SM4Utils;
import com.lib.aliocr.widget.crop.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
//import com.app.hubert.guide.newbieguide.FirstActivity;
//import com.blankj.utilcode.util.Utils;
//import com.example.slbyanzheng.ZhiwenActtivity;
//import com.geek.libnsfw.NsfwAct;
//import com.geek.libocr.ScanAct1;
//import com.geek.libpicturecompressor.PictureCompressorActivity;
//import com.geek.libshadowlayout.ShadowMainActivity;
//import com.vincent.videocompressor.activity.VideoComPressorActivity;

/**
 * @author houjie
 * @date 2022/4/13
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button, button1, button2, button3, button4, button5, button6, button7, button8, button9,
            button10, button11, button12, button13, button14, button15, button16, button17, button18, button19, button20, button21, button22;

    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScrollView scrollView = findViewById(R.id.scrollView);
        TextView tv_content1 = findViewById(R.id.tv_content1);
        /*屏幕适配autosize算法*/
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getRealMetrics(dm);
        int widthPixels = dm.widthPixels;//单位为像素 px
        int height = dm.heightPixels;//单位为像素 px
        float density = dm.density;
        float scaledDensity = dm.scaledDensity;
        int densityDpi = dm.densityDpi;
        Log.e(String.valueOf(widthPixels));
        Log.e(String.valueOf(height));
        Log.e(String.valueOf(density));
        Log.e(String.valueOf(scaledDensity));
        Log.e(String.valueOf(densityDpi));
        Log.e((Math.sqrt(Math.pow(1920, 2) + (Math.pow(1080, 2))) / 25.4 + ""));
        Log.e((Math.sqrt(Math.pow(1920, 2) + (Math.pow(1080, 2))) / 480 + ""));


        tv_content1.setText("侯杰");
        tv_content1.setText(Html.fromHtml("<u>" + "0123456" + "</u>"));
//        tv_content1.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        /*日期处理*/
        long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        Date d1 = new Date(time);
        String t1 = format.format(d1);

        String t2 = "2017-11-12";
        String t22 = format.format(TimeUtils.string2Date(t2, "yyyy-MM-dd"));
        if (DateUtils.isDateOneBigger1(t1, t22)) {

        }
        ToastUtils.showLong(t1 + "------" + t22);

        /*加密文件*/
        String result = "http://www.abc.com?k=D34D0AQ9tcjXuxVGcsw6WBHIjvADSX+x8Zm7O1jnNcXb/opqDZi+O1rXSjm0wqfw";
        String s = result.substring(result.lastIndexOf("k=") + 2);
        SM4Utils sm4 = new SM4Utils();
        String results = sm4.decryptData_ECB(s);
        Log.e("aaaaa明文1" + results);
        //
        Utils.init(App11.get());// com.blankj:utilcode:1.17.3
        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        button10 = findViewById(R.id.button10);
        button11 = findViewById(R.id.button11);
        button12 = findViewById(R.id.button12);
        button13 = findViewById(R.id.button13);
        button14 = findViewById(R.id.button14);
        button15 = findViewById(R.id.button15);
        button16 = findViewById(R.id.button16);
        button17 = findViewById(R.id.button17);
        button18 = findViewById(R.id.button18);
        button19 = findViewById(R.id.button19);
        button20 = findViewById(R.id.button20);
        button21 = findViewById(R.id.button21);
        button22 = findViewById(R.id.button22);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button10.setOnClickListener(this);
        button11.setOnClickListener(this);
        button12.setOnClickListener(this);
        button13.setOnClickListener(this);
        button14.setOnClickListener(this);
        button15.setOnClickListener(this);
        button16.setOnClickListener(this);
        button17.setOnClickListener(this);
        button18.setOnClickListener(this);
        button19.setOnClickListener(this);
        button20.setOnClickListener(this);
        button21.setOnClickListener(this);
        button22.setOnClickListener(this);
//        new PgyerSDKManager.Init()
//                .setContext(getApplicationContext()) //设置上下问对象
//                .start();
//        startActivity(new Intent(MainActivity.this, ScanAct2.class));
//        String strBase64 = Base64.encodeToString("梁肖".getBytes(), Base64.DEFAULT);// 编码
////        tv1.setText(EncodeUtils.base64Encode2String("梁肖".getBytes()));
//        String str2 = new String(Base64.decode(strBase64.getBytes(), Base64.DEFAULT));// 解码
//        tv1.setText(strBase64 + "," + str2);


//        scrollView.setDrawingCacheEnabled(true);
//        scrollView.buildDrawingCache();
//        Bitmap bitmap = scrollView.getDrawingCache();
//        getSmallBitmap(bitmapToString(bitmap));
//        saveBitmap(this,getSmallBitmap(bitmapToString(bitmap)));
//        qrCode();
    }

    public void qrCode() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null, false);
        Bitmap saveBitmap = createBitmapByView(view);
        saveBitmapToAlbum(saveBitmap);
    }

    /**
     * view转bitmap
     *
     * @param view view
     * @return Bitmap
     */
    private Bitmap createBitmapByView(View view) {
        //计算设备分辨率
        WindowManager manager = this.getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        //测量使得view指定大小
        int measureWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measureHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);

        view.measure(measureWidth, measureHeight);
        //调用layout方法布局后，可以得到view的尺寸
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 保存bitmap到相册
     */
    private void saveBitmapToAlbum(Bitmap bitmap) {
        final File appDir = new File(this.getExternalCacheDir(), "image");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        final String fileName = System.currentTimeMillis() + ".jpg";
        final File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
            } finally {
                fos.close();
            }
            //把文件插入到系统相册
            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), fileName, null);
            //通知图库更新
            //activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                /*黄图识别*/
                Intent intent = new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.NsfwAct");
//                Intent intent = new Intent(this, NsfwAct.class);
                startActivity(intent);
                //
//                String reviseBpmnFile = "http://cdn2.cdn.haier-jiuzhidao.com/tensorflowso/version.xml";
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String fileString = "";
//                        try {
//                            //读取远程文件URl
//                            InputStream in = LoadFile.downLoadFile(reviseBpmnFile);
//                            //
////                            List<XmlBean> mlist = LoadFile.getNodeList(in);
//                            List<XmlBean> mlist = DOMService.getPersons(in);
//
//                            //读取 xml 文件
////                            File fileinput = LoadFile.copyInputStreamToFile(in);
////
////                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
////                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
////                            Document doc = dBuilder.parse(fileinput);
////                            //将xml文件转化为String
////                            StringWriter sw = new StringWriter();
////                            TransformerFactory tf = TransformerFactory.newInstance();
////                            Transformer transformer = tf.newTransformer();
////                            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
////                            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
////                            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
////                            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
////                            transformer.transform(new DOMSource(doc), new StreamResult(sw));
////                            fileString = sw.toString();
//
//                        } catch (Exception e) {
//                            LogUtils.e("getReviseBpmnFile 读取文件异常，url：" + reviseBpmnFile);
//                            try {
//                                throw new Exception("读取文件异常！");
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                        //
//                        String pg_name = AppUtils.getAppPackageName();
//
//
//                    }
//                }).start();
                break;
            case R.id.button1:
                /*ocr识别*/
                Intent intent1 = new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.ScanAct1");
                startActivity(intent1);
                break;
            case R.id.button2:
                /*新手引导页*/
                Intent intent2 = new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.FirstActivity");
                startActivity(intent2);
                break;
            case R.id.button3:
                /*视频压缩*/
//                Intent intent3 = new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.VideoComPressorActivity");
//                Intent intent3 = new Intent(this, VideoComPressorActivity.class);
//                startActivity(intent3);
                break;
            case R.id.button4:
                /*shadow阴影的各项使用*/
                Intent intent4 = new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.ShadowMainActivity");
//                Intent intent4 = new Intent(this, ShadowMainActivity.class);
                startActivity(intent4);
                break;
            case R.id.button5:
                /*手势指纹验证库*/
                Intent intent5 = new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.ZhiwenActtivity");
//                Intent intent5 = new Intent(this, ZhiwenActtivity.class);
                startActivity(intent5);
                break;
            case R.id.button6:
                /*图片压缩库*/
                Intent intent6 = new Intent(AppUtils.getAppPackageName() + ".hs.act.slbapp.PictureCompressorActivity");
//                Intent intent6 = new Intent(MainActivity.this, PictureCompressorActivity.class);
                startActivity(intent6);
                break;
            case R.id.button7:
                /*通用自定义textview*/
//                Intent intent7 = new Intent(this, SuperTextviewActivity.class);
//                startActivity(intent7);
                break;
            case R.id.button8:
                /*Android点赞+1效果*/
//                Intent intent8 = new Intent(this, GoodViewActivity.class);
//                startActivity(intent8);
                break;
            case R.id.button9:
                /*Android仿头条点赞效果*/
//                Intent intent9 = new Intent(this, BlastActivity.class);
//                startActivity(intent9);
                break;
            case R.id.button10:
                /*直播点赞效果*/
//                Intent intent10 = new Intent(this, LiveActivity.class);
//                startActivity(intent10);
            case R.id.button11:
                /*人脸识别*/
                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.MainActivityfdt"));
                break;
            case R.id.button12:
                /*Viewpager+Bottomsheet功能*/
                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.MainActivitySheet"));
                break;
            case R.id.button13:
                /*多种进度条样式功能*/
                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.ProgressMainActivity"));
                break;
            case R.id.button14:
                /*图片识别文字
                 * 源库地址 'com.rmtheis:tess-two:9.1.0'*/
                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.PicturecognitionTextMainActivity"));
                break;
            case R.id.button15:
                /*扫描库
                 *项目地址: https://github.com/jenly1314/ZXingLite/tree/androidx*/
                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.ZxingMainActivity"));
                break;
            case R.id.button16:
                /*加密解密工具类
                 *项目地址: https://github.com/shouzhong/EncryptionUtils*/
                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.SampleActivity"));
                break;
            case R.id.button17:
                /*mlkit扫码新库
                 *项目地址: https://github.com/maning0303/MNMLKitScanner*/
                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.MlkitMainActivity"));
                break;
            case R.id.button18:
                /*布局预加载占位效果
                 *项目地址: https://github.com/zhang779/Preloader*/
                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.BroccoliMainActivity"));
                break;
            case R.id.button19:
                /*人脸识别库
                 *项目地址: https://github.com/FaceOnLive/Mask-Aware-Face-Recognition-SDK-Android*/
//                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.FaceMainActivity"));
                ToastUtils.showLong("研发中......");
                break;
            case R.id.button20:
                /*投屏库
                 *项目地址: https://github.com/yanbo469/VideoDlnaScreen*/
//                startActivity(new Intent(this, DlnaMainActivity.class));
                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.DlnaMainActivity"));
//                startActivity(new Intent(this, DlnaMainActivity.class));
                break;
            case R.id.button21:
                /*日历库
                 *项目地址: https://github.com/yannecer/NCalendar*/
//                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.NcalendarActivity"));
//                startActivity(new Intent(this, NcalendarActivity.class));
                qrCode();
                break;
            case R.id.button22:
                /*扫描库
                 *项目地址: https://github.com/LuckSiege/PictureSelector*/
//                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.NcalendarActivity"));
                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.SmCameraAct1"));
//                startActivity(new Intent(this, NcalendarActivity.class));

                break;
            default:
                break;
        }
    }


}