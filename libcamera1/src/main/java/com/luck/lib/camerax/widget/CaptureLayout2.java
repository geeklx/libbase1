package com.luck.lib.camerax.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geek.libcamera1.R;
import com.luck.lib.camerax.listener.CaptureListener;
import com.luck.lib.camerax.listener.ClickListener;
import com.luck.lib.camerax.listener.ImgClickListener;
import com.luck.lib.camerax.listener.LightClickListener;
import com.luck.lib.camerax.listener.TextClickListener1;
import com.luck.lib.camerax.listener.TypeListener;

/**
 * @author：luck
 * @date：2019-01-04 13:41
 * @describe：CaptureLayout
 */
public class CaptureLayout2 extends FrameLayout {

    private TextView tv1;//标题
    private ImageView iv1;// 关闭
    private RelativeLayout rl1;
    private ImageView iv11;//相册
    private ImageView iv12;//拍照
    private ImageView iv1212233;//闪光灯

    public ImageView getIv1212233() {
        return iv1212233;
    }

    public void setIv1212233(ImageView iv1212233) {
        this.iv1212233 = iv1212233;
    }

    private CaptureListener captureListener;    //拍照按钮监听
    private TypeListener typeListener;          //拍照或录制后接结果按钮监听
    private ClickListener leftClickListener;    //左边按钮监听
    private ClickListener rightClickListener;   //右边按钮监听
    private ImgClickListener imgClickListener;   //相册按钮监听
    private TextClickListener1 textClickListener1;   //标题按钮监听
    private LightClickListener lightClickListener;   //闪光灯按钮监听

    public void setTypeListener(TypeListener typeListener) {
        this.typeListener = typeListener;
    }

    public void setCaptureListener(CaptureListener captureListener) {
        this.captureListener = captureListener;
    }


    private TextView txt_tip;               //提示文本


    private int iconLeft = 0;
    private int iconRight = 0;

    public CaptureLayout2(Context context) {
        this(context, null);
    }

    public CaptureLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptureLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(layout_width, layout_height);

    }


    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.activity_camera_1, this, true);
        tv1 = findViewById(R.id.tv1);
        iv1 = findViewById(R.id.iv1);
        rl1 = findViewById(R.id.rl1);
        iv11 = findViewById(R.id.iv11);
        iv12 = findViewById(R.id.iv12);
        iv1212233 = findViewById(R.id.iv1212233);
        // 标题
        tv1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textClickListener1 != null) {
                    textClickListener1.onClick();
                }
            }
        });
        // 关闭页面
        iv1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leftClickListener != null) {
                    leftClickListener.onClick();
                }
//                if (typeListener != null) {
//                    typeListener.cancel();
//                }
            }
        });
        // 相册
        iv11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgClickListener != null) {
                    imgClickListener.onClick();
                }
            }
        });
        // 拍摄按钮
        iv12.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                if (captureListener != null) {
                    captureListener.takePictures();
                }
            }
        });
        // 闪光灯按钮
        iv1212233.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //
//                if (!typeFlash1) {
//                    typeFlash1 = true;
//                    iv13.setImageResource(R.drawable.camera_img4);
//                } else {
//                    typeFlash1 = false;
//                    iv13.setImageResource(R.drawable.camera_img3);
//                }
                //
                if (lightClickListener != null) {
                    lightClickListener.onClick();
                }
            }
        });


    }

    private boolean typeFlash1 = false;


    public void setLeftClickListener(ClickListener leftClickListener) {
        this.leftClickListener = leftClickListener;
    }

    public void setRightClickListener(ClickListener rightClickListener) {
        this.rightClickListener = rightClickListener;
    }

    public void setImgClickListener(ImgClickListener imgClickListener) {
        this.imgClickListener = imgClickListener;
    }

    public void setTextClickListener1(TextClickListener1 textClickListener1) {
        this.textClickListener1 = textClickListener1;
    }

    public void setLightClickListener(LightClickListener lightClickListener) {
        this.lightClickListener = lightClickListener;
    }
}