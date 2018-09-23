1.拷贝BaseVisualizerView.java类

2.布局

<com.auguigu.lijingxin.mobileplayer.YinPin.BaseVisualizerView
            android:id="@+id/base_view"
            android:layout_width="120dp"
            android:layout_height="60dp" />

3.方法

private Visualizer mVisualizer;

 /**
     * 生成一个VisualizerView对象，使音频频谱的波段能够反映到 VisualizerView上
     */
    private void setupVisualizerFxAndUi()
    {

        int audioSessionid = 0;
        try {
            audioSessionid = service1.getAudioSessionId();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("audioSessionid==" + audioSessionid);
        mVisualizer = new Visualizer(audioSessionid);
        // 参数内必须是2的位数
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        // 设置允许波形表示，并且捕获它
        base_view.setVisualizer(mVisualizer);
        mVisualizer.setEnabled(true);
    }



4.释放资源

 @Override
    protected void onPause()
    {
        super.onPause();
        if (isFinishing())
        {
            mVisualizer.release();
        }
    }


5.加权限

 <uses-permission android:name="android.permission.RECORD_AUDIO" ></uses-permission>

