package com.geek.videodlnascreen;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.geek.libscreen.callback.ControlCallback;
import com.geek.libscreen.entity.AVTransportInfo;
import com.geek.libscreen.entity.ProjectionState;
import com.geek.libscreen.entity.RemoteItem1;
import com.geek.libscreen.entity.RenderingControlInfo;
import com.geek.libscreen.event.ControlEvent;
import com.geek.libscreen.manager.ClingManager;
import com.geek.libscreen.manager.ControlManager;
import com.geek.libscreen.mmkv.MmkvUtils;
import com.geek.libscreen.utils.VMDate;

import org.fourthline.cling.support.model.item.Item;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 描述：
 *
 * @author Yanbo
 * @date 2018/11/6
 */
public class MediaPlayActivity1 extends AppCompatActivity implements View.OnClickListener {

    TextView contentTitleView;
    TextView contentUrlView;
    SeekBar progressSeekbar;
    TextView playTimeView;
    TextView playMaxTimeView;
    TextView stopView;
    ImageView previousView;
    ImageView playView;//播放按钮
    ImageView nextView;

    ImageView volume_plus;//音量加
    ImageView volume_reduce;//音量减
    ImageView fast_left;//左快进
    ImageView fast_right;//右快进

    RadioGroup radiogroup1;//视频源
    RadioButton rb_gq;//高清
    RadioButton rb_zgq;//准高清
    RadioButton rb_bq;//标清


    public Item localItem;
    public RemoteItem1 remoteItem;

    private int defaultVolume = 10;
    private int currVolume = defaultVolume;
    private boolean isMute = false;
    private int currProgress = 0;
    private String urlname = "gq";

    public static void startSelf(Activity context) {
        Intent intent = new Intent(context, MediaPlayActivity1.class);
        context.startActivity(intent);
    }
    public  void finalSelf(Activity context) {
        stop();
    }
    private ProjectionState projectionState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_media_play1);
        contentTitleView = findViewById(R.id.text_content_title);
        contentUrlView = findViewById(R.id.text_content_url);
        volume_plus = findViewById(R.id.volume_plus);
        volume_reduce = findViewById(R.id.volume_reduce);
        radiogroup1 = findViewById(R.id.radiogroup1);
        rb_gq = findViewById(R.id.rb_gq);
        rb_zgq = findViewById(R.id.rb_zgq);
        rb_bq = findViewById(R.id.rb_bq);
        fast_left = findViewById(R.id.fast_left);
        fast_right = findViewById(R.id.fast_right);
        progressSeekbar = findViewById(R.id.seek_bar_progress);
        playTimeView = findViewById(R.id.text_play_time);
        playMaxTimeView = findViewById(R.id.text_play_max_time);
        stopView = findViewById(R.id.img_stop);
        previousView = findViewById(R.id.img_previous);
        playView = findViewById(R.id.img_play);
        nextView = findViewById(R.id.img_next);
        playView.setOnClickListener(this);
        nextView.setOnClickListener(this);
        stopView.setOnClickListener(this);
        playTimeView.setOnClickListener(this);
        volume_plus.setOnClickListener(this);
        volume_reduce.setOnClickListener(this);
        fast_left.setOnClickListener(this);
        fast_right.setOnClickListener(this);
        projectionState = MmkvUtils.getInstance().get_common_json("projectionState", ProjectionState.class);

        if (projectionState != null) {
            switch (projectionState.getDefinition()) {
                case "gq":
                    rb_gq.setChecked(true);
                    urlname = projectionState.getDefinition();
                    url = projectionState.getPlayback();
                    break;
                case "zgq":
                    rb_zgq.setChecked(true);
                    urlname = projectionState.getDefinition();
                    url = projectionState.getPlayback();
                    break;
                case "bq":
                    rb_bq.setChecked(true);
                    urlname = projectionState.getDefinition();
                    url = projectionState.getPlayback();
                    break;

            }
        }

        init();
        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_gq) {//高清
                    urlname = "gq";
                    currProgress = 0;
                    play1(urlname);
                } else if (checkedId == R.id.rb_zgq) {//准高清
                    urlname = "zgq";
                    currProgress = 0;
                    play1(urlname);
                } else if (checkedId == R.id.rb_bq) {//标清
                    urlname = "bq";
                    currProgress = 0;
                    play1(urlname);
                }
            }
        });
    }

    String title = "";
    String url = "";
    String duration = "";

    private void init() {

        localItem = ClingManager.getInstance().getLocalItem();
        remoteItem = ClingManager.getInstance().getRemoteItem();
        if (localItem != null) {
            url = localItem.getFirstResource().getValue();
            duration = localItem.getFirstResource().getDuration();
        }
        if (remoteItem != null) {
            if (urlname.equals("gq")) {
                url = remoteItem.getHighurl();
            } else if (urlname.equals("zgq")) {
                url = remoteItem.getQuasihighurl();
            } else if (urlname.equals("bq")) {
                url = remoteItem.getStandardurl();
            }
            title = remoteItem.getTitle();
            duration = remoteItem.getDuration();
        }

        contentTitleView.setText(title);
        contentUrlView.setText(url);

        if (!TextUtils.isEmpty(duration)) {
            playMaxTimeView.setText(duration);
            progressSeekbar.setMax((int) VMDate.fromTimeString(duration));
        }

//        setVolumeSeekListener();
        setProgressSeekListener();
        if (url != null) {
            if (projectionState!=null){
                if (projectionState.getScreenstatus()) {
                    play2(urlname);
                } else {
                    play(urlname);
                }
            }else{
                play(urlname);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 设置播放进度拖动监听
     */
    private void setProgressSeekListener() {
        progressSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currProgress = seekBar.getProgress();
                playTimeView.setText(VMDate.toTimeString(currProgress));
                seekCast(currProgress);
            }
        });
    }


    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.img_volume) {
            mute();
        } else if (id == R.id.img_stop) {
            stop();
        } else if (id == R.id.img_previous) {
        } else if (id == R.id.img_play) {
            play(urlname);
        } else if (id == R.id.img_next) {
        } else if (id == R.id.volume_plus) {
            currVolume = currVolume + 10;
            setVolume(currVolume);
        } else if (id == R.id.volume_reduce) {
            currVolume = currVolume - 10;
            setVolume(currVolume);
        } else if (id == R.id.fast_left) {
            currProgress = currProgress - 10;
            seekCast(currProgress);
        } else if (id == R.id.fast_right) {
            currProgress = currProgress + 10;
            seekCast(currProgress);
        }
    }

    /**
     * 静音开关
     */
    private void mute() {
        // 先获取当前是否静音
        isMute = ControlManager.getInstance().isMute();
        ControlManager.getInstance().muteCast(!isMute, new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setMute(!isMute);
                if (isMute) {
                    if (currVolume == 0) {
                        currVolume = defaultVolume;
                    }
                    setVolume(currVolume);
                }
                // 这里是根据之前的状态判断的
                if (isMute) {
                    //有声音
//                    volumeView.setText("声音");
                } else {
                    //静音
//                    volumeView.setText("静音");
                }
            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("静音失败 %s", msg));
            }
        });
    }

    /**
     * 设置音量大小
     */
    private void setVolume(int volume) {
        currVolume = volume;
        ControlManager.getInstance().setVolumeCast(volume, new ControlCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("设置音量大小失败 %s", msg));
            }
        });
    }

    /**
     * 切换视频源开关
     */
    private void play1(String definitiontype) {
        if (definitiontype.equals("gq") || definitiontype.equals("zgq") || definitiontype.equals("bq")) {
            if (localItem != null) {
                newPlayCastLocalContent(definitiontype);
            } else {
                newPlayCastRemoteContent(definitiontype);
            }
        } else if (ControlManager.getInstance().getState() == ControlManager.CastState.PAUSED) {
            playCast();
        } else if (ControlManager.getInstance().getState() == ControlManager.CastState.PLAYING) {
            pauseCast();
        } else {
            Toast.makeText(getBaseContext(), "正在连接设备，稍后操作", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 播放开关
     */
    private void play2(String definitiontype) {
        if (ControlManager.getInstance().getState() == ControlManager.CastState.STOPED) {
            if (localItem != null) {
                newPlayCastLocalContent(definitiontype);
            } else {
                newPlayCastRemoteContent(definitiontype);
            }
        } else if (ControlManager.getInstance().getState() == ControlManager.CastState.PAUSED) {
            playCast();
        } else if (ControlManager.getInstance().getState() == ControlManager.CastState.PLAYING) {
            if (projectionState.getScreenstatus()) {
                playCast();
            } else {
                pauseCast();
            }
        } else {
            Toast.makeText(getBaseContext(), "正在连接设备，稍后操作", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 播放开关
     */
    private void play(String definitiontype) {
        if (ControlManager.getInstance().getState() == ControlManager.CastState.STOPED) {
            if (localItem != null) {
                newPlayCastLocalContent(definitiontype);
            } else {
                newPlayCastRemoteContent(definitiontype);
            }
        } else if (ControlManager.getInstance().getState() == ControlManager.CastState.PAUSED) {
            playCast();
        } else if (ControlManager.getInstance().getState() == ControlManager.CastState.PLAYING) {
            pauseCast();
        } else {
            Toast.makeText(getBaseContext(), "正在连接设备，稍后操作", Toast.LENGTH_SHORT).show();
        }
    }

    private void stop() {
        ControlManager.getInstance().unInitScreenCastCallback();
        stopCast();
    }

    private void newPlayCastLocalContent(String definitiontype) {
        ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
        ControlManager.getInstance().newPlayCast(localItem, definitiontype, new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                ControlManager.getInstance().initScreenCastCallback();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playView.setBackgroundResource(R.drawable.icon_zanting);
//                        playView.setText("暂停");
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
//                showToast(String.format("新建播放本地内容失败 %s", msg));
            }
        });
    }

    private void newPlayCastRemoteContent(String definitiontype) {
        ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
        ControlManager.getInstance().newPlayCast(remoteItem, definitiontype, new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                ControlManager.getInstance().initScreenCastCallback();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        playView.setBackgroundResource(R.drawable.icon_zanting);
//                        playView.setText("暂停");
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
//                showToast(String.format("新建播放远程内容失败 %s", msg));
            }
        });
    }

    private void playCast() {
        ControlManager.getInstance().playCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playView.setBackgroundResource(R.drawable.icon_zanting);
//                        playView.setText("暂停");
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
//                showToast(String.format("播放失败 %s", msg));
            }
        });
    }

    private void pauseCast() {
        ControlManager.getInstance().pauseCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PAUSED);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playView.setBackgroundResource(R.drawable.icon_bofang);
//                        playView.setText("播放");
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
//                showToast(String.format("暂停播放失败 %s", msg));
            }
        });
    }

    private  void stopCast() {
        ControlManager.getInstance().stopCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playView.setBackgroundResource(R.drawable.icon_bofang);
//                        finish();
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
//                showToast(String.format("停止播放失败 %s", msg));
            }
        });
    }

    /**
     * 改变投屏进度
     */
    private void seekCast(int progress) {
        String target = VMDate.toTimeString(progress);
        ControlManager.getInstance().seekCast(target, new ControlCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String msg) {
//                showToast(String.format("投屏进度播放失败 %s", msg));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(ControlEvent event) {
        AVTransportInfo avtInfo = event.getAvtInfo();
        if (avtInfo != null) {
            if (!TextUtils.isEmpty(avtInfo.getState())) {
                if (avtInfo.getState().equals("TRANSITIONING")) {
                    ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
                } else if (avtInfo.getState().equals("PLAYING")) {
                    ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                    playView.setBackgroundResource(R.drawable.icon_zanting);
//                    playView.setText("暂停");
                } else if (avtInfo.getState().equals("PAUSED_PLAYBACK")) {
                    ControlManager.getInstance().setState(ControlManager.CastState.PAUSED);
                    playView.setBackgroundResource(R.drawable.icon_bofang);
//                    playView.setText("播放");
                } else if (avtInfo.getState().equals("STOPPED")) {
                    ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                    playView.setBackgroundResource(R.drawable.icon_bofang);
//                    playView.setText("播放");

                    finish();
                } else {
                    ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                    playView.setBackgroundResource(R.drawable.icon_bofang);
//                    playView.setText("播放");
                    finish();
                }
            }
            if (!TextUtils.isEmpty(avtInfo.getMediaDuration())) {
                playMaxTimeView.setText(avtInfo.getMediaDuration());
            }
            if (!TextUtils.isEmpty(avtInfo.getTimePosition())) {
                long progress = VMDate.fromTimeString(avtInfo.getTimePosition());
                progressSeekbar.setProgress((int) progress);
                playTimeView.setText(avtInfo.getTimePosition());
            }
        }

        RenderingControlInfo rcInfo = event.getRcInfo();
        if (rcInfo != null && ControlManager.getInstance()
                .getState() != ControlManager.CastState.STOPED) {
            if (rcInfo.isMute() || rcInfo.getVolume() == 0) {
//                volumeView.setText("静音");
                ControlManager.getInstance().setMute(true);
            } else {
//                volumeView.setText("声音");
                ControlManager.getInstance().setMute(false);
            }
        }
    }

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
