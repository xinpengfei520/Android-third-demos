1.����BaseVisualizerView.java��

2.����

<com.auguigu.lijingxin.mobileplayer.YinPin.BaseVisualizerView
            android:id="@+id/base_view"
            android:layout_width="120dp"
            android:layout_height="60dp" />

3.����

private Visualizer mVisualizer;

 /**
     * ����һ��VisualizerView����ʹ��ƵƵ�׵Ĳ����ܹ���ӳ�� VisualizerView��
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
        // �����ڱ�����2��λ��
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        // ���������α�ʾ�����Ҳ�����
        base_view.setVisualizer(mVisualizer);
        mVisualizer.setEnabled(true);
    }



4.�ͷ���Դ

 @Override
    protected void onPause()
    {
        super.onPause();
        if (isFinishing())
        {
            mVisualizer.release();
        }
    }


5.��Ȩ��

 <uses-permission android:name="android.permission.RECORD_AUDIO" ></uses-permission>

