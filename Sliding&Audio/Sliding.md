package com.shang.mobileplay.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shang.mobileplay.R;
import com.shang.mobileplay.bean.MediaItem;
import com.shang.mobileplay.utils.Utils;
import com.shang.mobileplay.view.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 作者：尚志军 on 2016/9/6 19:34
 * 作用：系统播放器
 */
public class SystemPlayerActivity extends Activity implements View.OnClickListener {

    /**
     * 视频进度的更新
     */
    private static final int PROGRESS = 1;
    /**
     * 隐藏控制面板的消息
     */
    private static final int HIDE_MEDIACONTROLLER = 2;
    /**
     * 默认屏幕
     */
    private static final int SCREEN_DEFAULT = 3;
    /**
     * 屏幕全屏
     */
    private static final int SCREEN_FULL = 4;
    private static final int SHOW_NETSPEED = 5;
    private VideoView videoview;

    private Uri uri;

    private LinearLayout llTop;
    private LinearLayout llBottom;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitchPlayer;

    private TextView tvCurrenttime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnPre;
    private Button btnPlayPause;
    private Button btnNext;
    private Button btnSwitchScreen;
    private LinearLayout video_loading;
    private TextView tv_loading;
    private LinearLayout video_buffer;
    private TextView tv_buffer;

    private Utils utils;
    private MyBroadcastReceiver receiver;
    /**
     * 视频列表
     */
    private ArrayList<MediaItem> mediaItems;
    /**
     * 点击视频在列表中的位置
     */
    private int position;
    /**
     * 手势识别器
     */
    private GestureDetector detector;
    /**
     * 是否全屏
     */
    private boolean isFullScreen = false;

    private int screenWidth;
    private int screenHeight;
    private int videoWidth;
    private int videoHeight;

    /**
     * 调节声音管理类
     */
    private AudioManager audioManager;
    /**
     * 当前音量
     */
    private int currentVolume;
    /**
     * 最大音量
     */
    private int maxVolume;
    private int duration;
    private int currentPosition;
    private int prePosition = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case SHOW_NETSPEED:
                    //显示网络速度
                    String netspeed = utils.getNetSpeed(SystemPlayerActivity.this);
                    tv_buffer.setText(netspeed);
                    tv_loading.setText(netspeed);

                    removeMessages(SHOW_NETSPEED);
                    sendEmptyMessageDelayed(SHOW_NETSPEED,1000);
                    break;

                case PROGRESS://跟新进度

                    currentPosition = videoview.getCurrentPosition();//1000,2000,0
                    seekbarVideo.setProgress(currentPosition);


                    tvCurrenttime.setText(utils.stringForTime(currentPosition));


                    //更新系统时间
                    tvSystemTime.setText(getSystemTime());


                    if(isNetUri){
                        int buffer = videoview.getBufferPercentage();//0~100;

                        int bufferTotal = buffer*seekbarVideo.getMax();
                        //设置缓存进度
                        int secondaryProgress = bufferTotal/100;
                        seekbarVideo.setSecondaryProgress(secondaryProgress);
                    }else{
                        seekbarVideo.setSecondaryProgress(0);
                    }



                    if(videoview.isPlaying()){
                        //视频播放中
                        //自定义监听卡
                        int buffer =  currentPosition - prePosition;
                        if(buffer < 500){
                            //播放卡了
                            video_buffer.setVisibility(View.VISIBLE);
                        }else{
                            //正常播放
                            video_buffer.setVisibility(View.GONE);
                        }

                    }else{
                        //视频暂停
                        video_buffer.setVisibility(View.GONE);
                    }

                    prePosition = currentPosition;


                    //重复的发消息
                    removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1000);

                    break;
                case HIDE_MEDIACONTROLLER:
                    hideMediaController();
                    break;
            }
        }
    };

    private boolean isNetUri;


    private void findViews() {
        setContentView(R.layout.activity_system_player);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tvSystemTime = (TextView) findViewById(R.id.tv_system_time);
        btnVoice = (Button) findViewById(R.id.btn_voice);
        seekbarVoice = (SeekBar) findViewById(R.id.seekbar_voice);
        btnSwitchPlayer = (Button) findViewById(R.id.btn_switch_player);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrenttime = (TextView) findViewById(R.id.tv_currenttime);
        seekbarVideo = (SeekBar) findViewById(R.id.seekbar_video);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnPre = (Button) findViewById(R.id.btn_pre);
        btnPlayPause = (Button) findViewById(R.id.btn_play_pause);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnSwitchScreen = (Button) findViewById(R.id.btn_switch_screen);
        videoview = (VideoView) findViewById(R.id.video_view);
        video_loading = (LinearLayout) findViewById(R.id.video_loading);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
        video_buffer = (LinearLayout) findViewById(R.id.video_buffer);
        tv_buffer = (TextView) findViewById(R.id.tv_buffer);

        btnVoice.setOnClickListener(this);
        btnSwitchPlayer.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnPlayPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnSwitchScreen.setOnClickListener(this);

        //最大声音和SeekBar关联
        seekbarVoice.setMax(maxVolume);
        seekbarVoice.setProgress(currentVolume);

        //开始更新网速
        handler.sendEmptyMessage(SHOW_NETSPEED);
    }

    private boolean isMute= false;
    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-09-07 11:00:01 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnVoice) {
            // Handle clicks for btnVoice
            isMute = !isMute;
            updataVolume(currentVolume, isMute);

        } else if (v == btnSwitchPlayer) {
            // Handle clicks for btnSwitchPlayer
            showSwitchPlayer();
        } else if (v == btnExit) {
            // Handle clicks for btnExit
            finish();
        } else if (v == btnPre) {
            // Handle clicks for btnPre
            playPreVideo();
        } else if (v == btnPlayPause) {
            startAndPause();
            // Handle clicks for btnPlayPause
        } else if (v == btnNext) {
            // Handle clicks for btnNext
            //播放下一个
            playNextVideo();
        } else if (v == btnSwitchScreen) {
            // Handle clicks for btnSwitchScreen
            if (isFullScreen) {
                //默认
                setVideoType(SCREEN_DEFAULT);
            } else {
                //全屏
                setVideoType(SCREEN_FULL);
            }
        }

        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 5000);
    }

    private void showSwitchPlayer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前是系统播放器播放，是否切换到万能播放器播放");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startViatamioPlayer();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void startViatamioPlayer() {
        if(videoview != null){
            videoview.stopPlayback();
        }
        //3.传递视频列表
        Intent intent = new Intent(this,VitamioPlayerActivity.class);
        if(mediaItems!= null && mediaItems.size()>0){

            //传递列表
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);

            //传递位置
            intent.putExtra("position", position);

        }else if(uri != null){
            intent.setData(uri) ;
        }

        startActivity(intent);

        //关闭系统播放器
        finish();

    }


    private void startAndPause() {
        if (videoview.isPlaying()) {
            //暂停
            videoview.pause();
            //按钮设置-播放状态
            btnPlayPause.setBackgroundResource(R.drawable.btn_start_selector);
        } else {
            //播放
            videoview.start();
            //按钮设置-暂停状态
            btnPlayPause.setBackgroundResource(R.drawable.btn_pause_selector);
        }
    }

    private void playPreVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {

            position--;
            if (position >= 0) {
                video_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                isNetUri = utils.isNetUri(mediaItem.getData());
                videoview.setVideoPath(mediaItem.getData());
                tvName.setText(mediaItem.getName());

                setButtonStatus();
            }
        }
    }

    private void playNextVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {

            position++;
            if (position < mediaItems.size()) {
                video_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                isNetUri = utils.isNetUri(mediaItem.getData());
                videoview.setVideoPath(mediaItem.getData());
                tvName.setText(mediaItem.getName());

                setButtonStatus();

                if (position == mediaItems.size() - 1) {
                    Toast.makeText(SystemPlayerActivity.this, "播放最后一个视频", Toast.LENGTH_SHORT).show();
                }
            } else {
                finish();
            }
        } else if (uri != null) {
            finish();
        }
    }

    private boolean isShowMediaController = false;

    //显示控件面板
    private void showMediaController() {
        isShowMediaController = true;
        llBottom.setVisibility(View.VISIBLE);
        llTop.setVisibility(View.VISIBLE);
    }

    //隐藏控件面板
    private void hideMediaController() {
        isShowMediaController = false;
        llBottom.setVisibility(View.GONE);
        llTop.setVisibility(View.GONE);
    }

    private void setButtonStatus() {
        if (mediaItems != null && mediaItems.size() > 0) {

            setEnable(true);

            if(position ==0){
                btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
                btnPre.setEnabled(false);
            }

            if(position ==(mediaItems.size()-1)){
                btnNext.setBackgroundResource(R.drawable.btn_next_gray);
                btnNext.setEnabled(false);
            }


        } else if (uri != null) {
            //播放一个地址
            setEnable(false);
        }
    }

    private void setEnable(boolean isEnable) {
        if (isEnable) {
            btnNext.setBackgroundResource(R.drawable.btn_next_selector);
            btnPre.setBackgroundResource(R.drawable.btn_pre_selector);
        } else {
            btnNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
        }
        btnNext.setEnabled(isEnable);
        btnPre.setEnabled(isEnable);
    }


    //得到系统时间
    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        findViews();

        getData();
        setListener();
        setData();
        //设置控制面板
//        videoview.setMediaController(new MediaController(this));


    }

    class MyOnInfoListener implements MediaPlayer.OnInfoListener {

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what){
                //视频卡，拖动卡
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    video_buffer.setVisibility(View.VISIBLE);
                    break;
                //视频卡结束，拖动卡结束
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    video_buffer.setVisibility(View.GONE);
                    break;
            }
            return true;
        }
    }


    private void setData() {

        if (mediaItems != null && mediaItems.size() > 0) {
            //从列表中播放
            MediaItem mediaItem = mediaItems.get(position);
            isNetUri = utils.isNetUri(mediaItem.getData());
            videoview.setVideoPath(mediaItem.getData());
            tvName.setText(mediaItem.getName());
        } else if (uri != null) {
            //设置播放地址
            isNetUri = utils.isNetUri(uri.toString());
            videoview.setVideoURI(uri);//文件夹，QQ控件
            tvName.setText(uri.toString());
        }

        setButtonStatus();


    }

    private void getData() {
        uri = getIntent().getData();//播放本地文件，
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);
    }

    private void initData() {
        utils = new Utils();

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //0~15
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //得到屏幕的宽和高
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        //注册监听电量变化的广播
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);

        //2.创建手势识别器
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                if (isFullScreen) {
                    //默认
                    setVideoType(SCREEN_DEFAULT);
                } else {
                    //全屏
                    setVideoType(SCREEN_FULL);
                }
//                Toast.makeText(SystemPlayerActivity.this, "我被双击了", Toast.LENGTH_SHORT).show();

                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isShowMediaController) {
                    //隐藏
                    hideMediaController();
                    //把消息移除
                    handler.removeMessages(HIDE_MEDIACONTROLLER);
                } else {
                    //显示
                    showMediaController();
                    //发消息，延迟隐藏
                    handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 5000);
                }
//                Toast.makeText(SystemPlayerActivity.this, "我被单击", Toast.LENGTH_SHORT).show();
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
//                Toast.makeText(SystemPlayerActivity.this, "我被长按", Toast.LENGTH_SHORT).show();
                startAndPause();
                super.onLongPress(e);
            }
        });
    }

    /**
     * 设置屏幕的类型：默认和全屏
     *
     * @param screenDefault
     */
    private void setVideoType(int screenDefault) {
        switch (screenDefault) {
            case SCREEN_DEFAULT://默认
                isFullScreen = false;


                /**
                 * 真实视频的宽和高
                 */
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;

                //计算后视频宽和高的结果，默认和屏幕宽和高一样
                int width = screenWidth;
                int height = screenHeight;
                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }

                videoview.setVideoSize(width, height);

                //设置按钮为全屏
                btnSwitchScreen.setBackgroundResource(R.drawable.btn_switch_screen_full_selector);
                break;
            case SCREEN_FULL://全屏
                isFullScreen = true;
                //设置全屏
                videoview.setVideoSize(screenWidth, screenHeight);
                //设置按钮为默认
                btnSwitchScreen.setBackgroundResource(R.drawable.btn_switch_screen_default_selector);

                break;
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);//0~100电量值
            //主线程
            setBattery(level);
        }
    }

    private void setBattery(int level) {
        if (level <= 0) {
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        } else if (level <= 10) {
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        } else if (level <= 20) {
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (level <= 40) {
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        } else if (level <= 60) {
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        } else if (level <= 80) {
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (level <= 100) {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    private void setListener() {
        //设置三个监听，准备好了，播放出错，播放完成
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                videoWidth = mp.getVideoWidth();
                videoHeight = mp.getVideoHeight();
                videoview.start();//开始播放
                //得到视频的总时长
                duration = videoview.getDuration();
                seekbarVideo.setMax(duration);
                //设置总时长
                tvDuration.setText(utils.stringForTime(duration));
                //发消息更新
                handler.sendEmptyMessage(PROGRESS);
                hideMediaController();//隐藏控制面板
                setVideoType(SCREEN_DEFAULT);//默认屏幕播放

                //校验状态
                if(videoview.isPlaying()){
                    btnPlayPause.setBackgroundResource(R.drawable.btn_pause_selector);
                }else{
                    btnPlayPause.setBackgroundResource(R.drawable.btn_start_selector);
                }

                //把加载页面消掉
                video_loading.setVisibility(View.GONE);
            }
        });
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextVideo();//自动播放下一个
            }
        });
        videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //1.格式不支持---跳转到万能播放器继续播放
                startViatamioPlayer();
                //2.播放网络视频的过程中网络中断---三次重试
                //3.播放文件中间有损坏--下载文件bug修改
                return true;
            }
        });

        //设置视频的拖动
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 当手指在拖动改变进度的时候回调这个方法
             * @param fromUser 如果是用户行为true,否则为false
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {//人为拖动时
                    videoview.seekTo(progress);
                }
            }

            //当手开始触摸的时候回调这个方法
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDE_MEDIACONTROLLER);
            }

            //当手指离开的时候回调这个方法
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 3500);
            }
        });

        //设置拖动SeeKBar改变声音
        seekbarVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 当手指在拖动改变进度的时候回调这个方法
             * @param fromUser 如果是用户行为true,否则为false
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {//人为拖动时
                    updataVolumeProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDE_MEDIACONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 3500);
            }
        });

        //设置监听卡
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            videoview.setOnInfoListener(new MyOnInfoListener());
//        }
    }

    /**
     * 改变声音
     * @param progress
     */
    private void updataVolume(int progress, boolean isMute) {
        if(isMute){//无声
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);//调节声音
            seekbarVoice.setProgress(0);//设置seekBar进度
        }else{//有声
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);//调节声音
            seekbarVoice.setProgress(progress);//设置seekBar进度

            currentVolume = progress;
        }
    }

    //改变声音进度条
    private void updataVolumeProgress(int progress) {
        //调节声音
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        //设置seekBar进度
        seekbarVoice.setProgress(progress);

        currentVolume = progress;

        if(progress<=0){
            isMute = true;
        }else{
            isMute = false;
        }
    }

    //监听物理健，实现声音的调节大小
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_VOLUME_UP){
            currentVolume ++;
            updataVolumeProgress(currentVolume);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,3500);

            return true;
        }else if(keyCode ==KeyEvent.KEYCODE_VOLUME_DOWN){
            currentVolume --;
            updataVolumeProgress(currentVolume);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,3500);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {

        //把所有的消息和任务移除
        handler.removeCallbacksAndMessages(null);
        //取消注册电量广播
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();

    }

    /**
     * 屏幕亮度、声音、快进快退调节
     */
    private int screenBrightness;
    //一按下时的X、Y坐标
    private float startBrightX;
    private float startBrightY;
    //一按下时的音量
    private int mVol;
    //屏幕的高
    private float touchRang;
    //屏幕的宽
    private float touchWidth;
    //震动
    private Vibrator vibrator;

    /*
     *
     * 设置屏幕亮度 lp = 0 全暗 ，lp= -1,根据系统设置， lp = 1; 最亮
     */
    public void setBrightness(float brightness) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness += brightness / 255;
        if (lp.screenBrightness >= 1) {
            lp.screenBrightness = 1;
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...
            vibrator.vibrate(pattern, -1);
        } else if (lp.screenBrightness < 0.02) {
            lp.screenBrightness = (float) 0.02;
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...
            vibrator.vibrate(pattern, -1);
        }
        getWindow().setAttributes(lp);
    }

    private void screenBrightness_check() {
        //先关闭系统的亮度自动调节
        try {
            if (Settings.System.getInt(getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE) ==
                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        //获取当前亮度,获取失败则返回255
        screenBrightness = Settings.System.getInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 255);
        Log.e("TAG", "screenBrightness===========" + screenBrightness);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //3.把事件传递给手势识别器
        detector.onTouchEvent(event);
        screenBrightness_check();//关闭自动调节亮度

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1.按下记录值
                startBrightY = event.getY();
                startBrightX = event.getX();
                mVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                //得到宽和高
                touchRang = Math.min(screenHeight, screenWidth);
                touchWidth = Math.max(screenHeight, screenWidth);
                handler.removeMessages(HIDE_MEDIACONTROLLER);
                break;

            case MotionEvent.ACTION_MOVE://手指移动
                //2.移动的记录相关值
                float endY = event.getY();
                float endX = event.getX();
                float distanceY = startBrightY - endY;
                float distanceX = endX - startBrightX;

                //判断是滑动左边还是右边
                if (Math.abs(distanceY) > Math.abs(distanceX)) {

                    if(startBrightX < touchWidth / 4){//调节亮度
//                        float brightness = screenBrightness;
//                        float delta = (distanceY / touchRang ) * 255/3;
//                        int bright = (int) Math.min(Math.max((brightness + delta), 0), 255);
//                        if (delta != 0) {
//                            setBrightness(bright);}
                        final double FLING_MIN_DISTANCE = 0.5;
                        final double FLING_MIN_VELOCITY = 0.5;
                        if (distanceY > FLING_MIN_DISTANCE
                                && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
//                        Log.e(TAG, "up");
                            setBrightness(10);
                        }
                        if (distanceY < FLING_MIN_DISTANCE
                                && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
//                        Log.e(TAG, "down");
                            setBrightness(-10);
                        }
                    }else if(startBrightX > touchWidth * 3 / 4) {//调节声音
                        float delta = (distanceY / touchRang) * maxVolume;
                        int voice = (int) Math.min(Math.max(mVol + delta, 0), maxVolume);
                        if (delta != 0) {
                            isMute = false;
                            updataVolume(voice, isMute);
                        }
                    }
                } else{//左右滑动控制播放进度
                    float delta = distanceX / touchWidth * duration;
                    int bright = (int) Math.min(Math.max(currentPosition + delta, 0), duration);
                    videoview.seekTo(bright);
                    seekbarVideo.setProgress(bright);
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 3000);
                break;
        }
        return super.onTouchEvent(event);
    }
}