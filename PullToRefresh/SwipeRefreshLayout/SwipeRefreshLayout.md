1.�Դ��Ĳ���

2.���ֿ��԰�ListView�Ȳ���

    <android.support.v4.widget.SwipeRefreshLayout
        android:id="@+id/swiperefreshlayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
    </android.support.v4.widget.SwipeRefreshLayout>

3.����

  //����ˢ�¿ؼ�ȦȦ��ɫ
        swiperefreshlayout.setColorSchemeColors(Color.BLUE, Color.CYAN, Color.RED, Color.YELLOW, Color.GREEN);
        //����ˢ�µı���ɫ
        swiperefreshlayout.setProgressBackgroundColorSchemeColor(Color.CYAN);
        //���û�������(ԽСԽ���)
        swiperefreshlayout.setDistanceToTriggerSync(100);
        //���ô�Сģʽ
        swiperefreshlayout.setSize(SwipeRefreshLayout.DEFAULT);//Ĭ��

4.��������
        //��������ˢ�µļ���
        swiperefreshlayout.setOnRefreshListener(new MyOnRefreshListener());

