package com.geek.lib_screen.demo;

import android.app.Activity;
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

import com.geek.lib_screen.R;
import com.geek.lib_screen.callback.ControlCallback;
import com.geek.lib_screen.entity.AVTransportInfo;
import com.geek.lib_screen.entity.RemoteItem;
import com.geek.lib_screen.entity.RenderingControlInfo;
import com.geek.lib_screen.event.ControlEvent;
import com.geek.lib_screen.manager.ClingManager;
import com.geek.lib_screen.manager.ControlManager;
import com.geek.lib_screen.utils.LogUtils;
import com.geek.lib_screen.utils.VMDate;

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
    TextView volumeView;
    SeekBar volumeSeekbar;
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
    public RemoteItem remoteItem;

    private int defaultVolume = 10;
    private int currVolume = defaultVolume;
    private boolean isMute = false;
    private int currProgress = 0;

    public static void startSelf(Activity context) {
        Intent intent = new Intent(context, MediaPlayActivity1.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_media_play1);
        contentTitleView = findViewById(R.id.text_content_title);
        contentUrlView = findViewById(R.id.text_content_url);
        volumeView = findViewById(R.id.img_volume);
        volumeSeekbar = findViewById(R.id.seek_bar_volume);
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
        rb_gq.setChecked(true);
        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_gq) {
                    Toast.makeText(MediaPlayActivity1.this, "点击高清", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.rb_zgq) {
                    Toast.makeText(MediaPlayActivity1.this, "点击准高清", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.rb_bq) {
                    Toast.makeText(MediaPlayActivity1.this, "点击标清", Toast.LENGTH_SHORT).show();
                }
            }
        });
        init();
    }


    private void init() {

        localItem = ClingManager.getInstance().getLocalItem();
        remoteItem = ClingManager.getInstance().getRemoteItem();
        String title = "";
        String url = "";
        String duration = "";
        if (localItem != null) {
            url = localItem.getFirstResource().getValue();
            duration = localItem.getFirstResource().getDuration();
        }
        if (remoteItem != null) {
            url = remoteItem.getUrl();
            title = remoteItem.getTitle();
            duration = remoteItem.getDuration();
        }

        contentTitleView.setText(title);
        contentUrlView.setText(url);

        if (!TextUtils.isEmpty(duration)) {
            playMaxTimeView.setText(duration);
            progressSeekbar.setMax((int) VMDate.fromTimeString(duration));
        }

        setVolumeSeekListener();
        setProgressSeekListener();
        if (url != null) {
            play();
        }
    }


    /**
     * 设置音量拖动监听
     */
    private void setVolumeSeekListener() {
        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtils.d("音量动态大小: %d", progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setVolume(seekBar.getProgress());
            }
        });
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
            play();
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
                    volumeView.setText("声音");
                } else {
                    volumeView.setText("静音");
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
     * 播放开关
     */
    private void play() {
        if (ControlManager.getInstance().getState() == ControlManager.CastState.STOPED) {
            if (localItem != null) {
                newPlayCastLocalContent();
            } else {
                newPlayCastRemoteContent();
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

    private void newPlayCastLocalContent() {
        ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
        ControlManager.getInstance().newPlayCast(localItem, new ControlCallback() {
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
                showToast(String.format("新建播放本地内容失败 %s", msg));
            }
        });
    }

    private void newPlayCastRemoteContent() {
        ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
        ControlManager.getInstance().newPlayCast(remoteItem, new ControlCallback() {
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
                showToast(String.format("新建播放远程内容失败 %s", msg));
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
                showToast(String.format("播放失败 %s", msg));
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
                showToast(String.format("暂停播放失败 %s", msg));
            }
        });
    }

    private void stopCast() {
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
                showToast(String.format("停止播放失败 %s", msg));
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
                showToast(String.format("投屏进度播放失败 %s", msg));
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
//
//                    finish();
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
        if (rcInfo != null && ControlManager.getInstance().getState() != ControlManager.CastState.STOPED) {
            if (rcInfo.isMute() || rcInfo.getVolume() == 0) {
                volumeView.setText("静音");
                ControlManager.getInstance().setMute(true);
            } else {
                volumeView.setText("声音");
                ControlManager.getInstance().setMute(false);
            }
            volumeSeekbar.setProgress(rcInfo.getVolume());
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
