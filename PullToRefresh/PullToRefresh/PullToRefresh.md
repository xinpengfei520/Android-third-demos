1.���ɿ�

2.���ִ���
 <com.handmark.pulltorefresh.library.PullToRefreshListView
        android:id="@+id/pull_refresh_list"
        android:layout_width="fill_parent"
        android:layout_height="fill_parent"
        android:gravity="fill"
        android:horizontalSpacing="1dp"
        ptr:ptrMode="both" />

3.���Listview�Ĵ���
private ListView listView;
 listView = pull_refresh_list.getRefreshableView();

4.����ˢ���������
  //���ˢ��������������
        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(mContext);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        pull_refresh_list.setOnPullEventListener(soundListener);

