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
 * ���ߣ���־�� on 2016/9/6 19:34
 * ���ã�ϵͳ������
 */
public class SystemPlayerActivity extends Activity implements View.OnClickListener {

    /**
     * ��Ƶ���ȵĸ���
     */
    private static final int PROGRESS = 1;
    /**
     * ���ؿ���������Ϣ
     */
    private static final int HIDE_MEDIACONTROLLER = 2;
    /**
     * Ĭ����Ļ
     */
    private static final int SCREEN_DEFAULT = 3;
    /**
     * ��Ļȫ��
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
     * ��Ƶ�б�
     */
    private ArrayList<MediaItem> mediaItems;
    /**
     * �����Ƶ���б��е�λ��
     */
    private int position;
    /**
     * ����ʶ����
     */
    private GestureDetector detector;
    /**
     * �Ƿ�ȫ��
     */
    private boolean isFullScreen = false;

    private int screenWidth;
    private int screenHeight;
    private int videoWidth;
    private int videoHeight;

    /**
     * ��������������
     */
    private AudioManager audioManager;
    /**
     * ��ǰ����
     */
    private int currentVolume;
    /**
     * �������
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
                    //��ʾ�����ٶ�
                    String netspeed = utils.getNetSpeed(SystemPlayerActivity.this);
                    tv_buffer.setText(netspeed);
                    tv_loading.setText(netspeed);

                    removeMessages(SHOW_NETSPEED);
                    sendEmptyMessageDelayed(SHOW_NETSPEED,1000);
                    break;

                case PROGRESS://���½���

                    currentPosition = videoview.getCurrentPosition();//1000,2000,0
                    seekbarVideo.setProgress(currentPosition);


                    tvCurrenttime.setText(utils.stringForTime(currentPosition));


                    //����ϵͳʱ��
                    tvSystemTime.setText(getSystemTime());


                    if(isNetUri){
                        int buffer = videoview.getBufferPercentage();//0~100;

                        int bufferTotal = buffer*seekbarVideo.getMax();
                        //���û������
                        int secondaryProgress = bufferTotal/100;
                        seekbarVideo.setSecondaryProgress(secondaryProgress);
                    }else{
                        seekbarVideo.setSecondaryProgress(0);
                    }



                    if(videoview.isPlaying()){
                        //��Ƶ������
                        //�Զ��������
                        int buffer =  currentPosition - prePosition;
                        if(buffer < 500){
                            //���ſ���
                            video_buffer.setVisibility(View.VISIBLE);
                        }else{
                            //��������
                            video_buffer.setVisibility(View.GONE);
                        }

                    }else{
                        //��Ƶ��ͣ
                        video_buffer.setVisibility(View.GONE);
                    }

                    prePosition = currentPosition;


                    //�ظ��ķ���Ϣ
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

        //���������SeekBar����
        seekbarVoice.setMax(maxVolume);
        seekbarVoice.setProgress(currentVolume);

        //��ʼ��������
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
            //������һ��
            playNextVideo();
        } else if (v == btnSwitchScreen) {
            // Handle clicks for btnSwitchScreen
            if (isFullScreen) {
                //Ĭ��
                setVideoType(SCREEN_DEFAULT);
            } else {
                //ȫ��
                setVideoType(SCREEN_FULL);
            }
        }

        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 5000);
    }

    private void showSwitchPlayer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("��ʾ");
        builder.setMessage("��ǰ��ϵͳ���������ţ��Ƿ��л������ܲ���������");
        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startViatamioPlayer();
            }
        });
        builder.setNegativeButton("ȡ��", null);
        builder.show();
    }

    private void startViatamioPlayer() {
        if(videoview != null){
            videoview.stopPlayback();
        }
        //3.������Ƶ�б�
        Intent intent = new Intent(this,VitamioPlayerActivity.class);
        if(mediaItems!= null && mediaItems.size()>0){

            //�����б�
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);

            //����λ��
            intent.putExtra("position", position);

        }else if(uri != null){
            intent.setData(uri) ;
        }

        startActivity(intent);

        //�ر�ϵͳ������
        finish();

    }


    private void startAndPause() {
        if (videoview.isPlaying()) {
            //��ͣ
            videoview.pause();
            //��ť����-����״̬
            btnPlayPause.setBackgroundResource(R.drawable.btn_start_selector);
        } else {
            //����
            videoview.start();
            //��ť����-��ͣ״̬
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
                    Toast.makeText(SystemPlayerActivity.this, "�������һ����Ƶ", Toast.LENGTH_SHORT).show();
                }
            } else {
                finish();
            }
        } else if (uri != null) {
            finish();
        }
    }

    private boolean isShowMediaController = false;

    //��ʾ�ؼ����
    private void showMediaController() {
        isShowMediaController = true;
        llBottom.setVisibility(View.VISIBLE);
        llTop.setVisibility(View.VISIBLE);
    }

    //���ؿؼ����
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
            //����һ����ַ
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


    //�õ�ϵͳʱ��
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
        //���ÿ������
//        videoview.setMediaController(new MediaController(this));


    }

    class MyOnInfoListener implements MediaPlayer.OnInfoListener {

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what){
                //��Ƶ�����϶���
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    video_buffer.setVisibility(View.VISIBLE);
                    break;
                //��Ƶ���������϶�������
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    video_buffer.setVisibility(View.GONE);
                    break;
            }
            return true;
        }
    }


    private void setData() {

        if (mediaItems != null && mediaItems.size() > 0) {
            //���б��в���
            MediaItem mediaItem = mediaItems.get(position);
            isNetUri = utils.isNetUri(mediaItem.getData());
            videoview.setVideoPath(mediaItem.getData());
            tvName.setText(mediaItem.getName());
        } else if (uri != null) {
            //���ò��ŵ�ַ
            isNetUri = utils.isNetUri(uri.toString());
            videoview.setVideoURI(uri);//�ļ��У�QQ�ؼ�
            tvName.setText(uri.toString());
        }

        setButtonStatus();


    }

    private void getData() {
        uri = getIntent().getData();//���ű����ļ���
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);
    }

    private void initData() {
        utils = new Utils();

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //0~15
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //�õ���Ļ�Ŀ�͸�
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        //ע����������仯�Ĺ㲥
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);

        //2.��������ʶ����
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                if (isFullScreen) {
                    //Ĭ��
                    setVideoType(SCREEN_DEFAULT);
                } else {
                    //ȫ��
                    setVideoType(SCREEN_FULL);
                }
//                Toast.makeText(SystemPlayerActivity.this, "�ұ�˫����", Toast.LENGTH_SHORT).show();

                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isShowMediaController) {
                    //����
                    hideMediaController();
                    //����Ϣ�Ƴ�
                    handler.removeMessages(HIDE_MEDIACONTROLLER);
                } else {
                    //��ʾ
                    showMediaController();
                    //����Ϣ���ӳ�����
                    handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 5000);
                }
//                Toast.makeText(SystemPlayerActivity.this, "�ұ�����", Toast.LENGTH_SHORT).show();
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
//                Toast.makeText(SystemPlayerActivity.this, "�ұ�����", Toast.LENGTH_SHORT).show();
                startAndPause();
                super.onLongPress(e);
            }
        });
    }

    /**
     * ������Ļ�����ͣ�Ĭ�Ϻ�ȫ��
     *
     * @param screenDefault
     */
    private void setVideoType(int screenDefault) {
        switch (screenDefault) {
            case SCREEN_DEFAULT://Ĭ��
                isFullScreen = false;


                /**
                 * ��ʵ��Ƶ�Ŀ�͸�
                 */
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;

                //�������Ƶ��͸ߵĽ����Ĭ�Ϻ���Ļ��͸�һ��
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

                //���ð�ťΪȫ��
                btnSwitchScreen.setBackgroundResource(R.drawable.btn_switch_screen_full_selector);
                break;
            case SCREEN_FULL://ȫ��
                isFullScreen = true;
                //����ȫ��
                videoview.setVideoSize(screenWidth, screenHeight);
                //���ð�ťΪĬ��
                btnSwitchScreen.setBackgroundResource(R.drawable.btn_switch_screen_default_selector);

                break;
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);//0~100����ֵ
            //���߳�
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
        //��������������׼�����ˣ����ų����������
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                videoWidth = mp.getVideoWidth();
                videoHeight = mp.getVideoHeight();
                videoview.start();//��ʼ����
                //�õ���Ƶ����ʱ��
                duration = videoview.getDuration();
                seekbarVideo.setMax(duration);
                //������ʱ��
                tvDuration.setText(utils.stringForTime(duration));
                //����Ϣ����
                handler.sendEmptyMessage(PROGRESS);
                hideMediaController();//���ؿ������
                setVideoType(SCREEN_DEFAULT);//Ĭ����Ļ����

                //У��״̬
                if(videoview.isPlaying()){
                    btnPlayPause.setBackgroundResource(R.drawable.btn_pause_selector);
                }else{
                    btnPlayPause.setBackgroundResource(R.drawable.btn_start_selector);
                }

                //�Ѽ���ҳ������
                video_loading.setVisibility(View.GONE);
            }
        });
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextVideo();//�Զ�������һ��
            }
        });
        videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //1.��ʽ��֧��---��ת�����ܲ�������������
                startViatamioPlayer();
                //2.����������Ƶ�Ĺ����������ж�---��������
                //3.�����ļ��м�����--�����ļ�bug�޸�
                return true;
            }
        });

        //������Ƶ���϶�
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * ����ָ���϶��ı���ȵ�ʱ��ص��������
             * @param fromUser ������û���Ϊtrue,����Ϊfalse
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {//��Ϊ�϶�ʱ
                    videoview.seekTo(progress);
                }
            }

            //���ֿ�ʼ������ʱ��ص��������
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDE_MEDIACONTROLLER);
            }

            //����ָ�뿪��ʱ��ص��������
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 3500);
            }
        });

        //�����϶�SeeKBar�ı�����
        seekbarVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * ����ָ���϶��ı���ȵ�ʱ��ص��������
             * @param fromUser ������û���Ϊtrue,����Ϊfalse
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {//��Ϊ�϶�ʱ
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

        //���ü�����
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            videoview.setOnInfoListener(new MyOnInfoListener());
//        }
    }

    /**
     * �ı�����
     * @param progress
     */
    private void updataVolume(int progress, boolean isMute) {
        if(isMute){//����
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);//��������
            seekbarVoice.setProgress(0);//����seekBar����
        }else{//����
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);//��������
            seekbarVoice.setProgress(progress);//����seekBar����

            currentVolume = progress;
        }
    }

    //�ı�����������
    private void updataVolumeProgress(int progress) {
        //��������
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        //����seekBar����
        seekbarVoice.setProgress(progress);

        currentVolume = progress;

        if(progress<=0){
            isMute = true;
        }else{
            isMute = false;
        }
    }

    //����������ʵ�������ĵ��ڴ�С
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

        //�����е���Ϣ�������Ƴ�
        handler.removeCallbacksAndMessages(null);
        //ȡ��ע������㲥
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();

    }

    /**
     * ��Ļ���ȡ�������������˵���
     */
    private int screenBrightness;
    //һ����ʱ��X��Y����
    private float startBrightX;
    private float startBrightY;
    //һ����ʱ������
    private int mVol;
    //��Ļ�ĸ�
    private float touchRang;
    //��Ļ�Ŀ�
    private float touchWidth;
    //��
    private Vibrator vibrator;

    /*
     *
     * ������Ļ���� lp = 0 ȫ�� ��lp= -1,����ϵͳ���ã� lp = 1; ����
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
        //�ȹر�ϵͳ�������Զ�����
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
        //��ȡ��ǰ����,��ȡʧ���򷵻�255
        screenBrightness = Settings.System.getInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 255);
        Log.e("TAG", "screenBrightness===========" + screenBrightness);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //3.���¼����ݸ�����ʶ����
        detector.onTouchEvent(event);
        screenBrightness_check();//�ر��Զ���������

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1.���¼�¼ֵ
                startBrightY = event.getY();
                startBrightX = event.getX();
                mVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                //�õ���͸�
                touchRang = Math.min(screenHeight, screenWidth);
                touchWidth = Math.max(screenHeight, screenWidth);
                handler.removeMessages(HIDE_MEDIACONTROLLER);
                break;

            case MotionEvent.ACTION_MOVE://��ָ�ƶ�
                //2.�ƶ��ļ�¼���ֵ
                float endY = event.getY();
                float endX = event.getX();
                float distanceY = startBrightY - endY;
                float distanceX = endX - startBrightX;

                //�ж��ǻ�����߻����ұ�
                if (Math.abs(distanceY) > Math.abs(distanceX)) {

                    if(startBrightX < touchWidth / 4){//��������
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
                    }else if(startBrightX > touchWidth * 3 / 4) {//��������
                        float delta = (distanceY / touchRang) * maxVolume;
                        int voice = (int) Math.min(Math.max(mVol + delta, 0), maxVolume);
                        if (delta != 0) {
                            isMute = false;
                            updataVolume(voice, isMute);
                        }
                    }
                } else{//���һ������Ʋ��Ž���
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