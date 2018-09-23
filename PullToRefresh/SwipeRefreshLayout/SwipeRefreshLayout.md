1.自带的布局

2.布局可以包ListView等布局

    <android.support.v4.widget.SwipeRefreshLayout
        android:id="@+id/swiperefreshlayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
    </android.support.v4.widget.SwipeRefreshLayout>

3.属性

  //设置刷新控件圈圈颜色
        swiperefreshlayout.setColorSchemeColors(Color.BLUE, Color.CYAN, Color.RED, Color.YELLOW, Color.GREEN);
        //设置刷新的背景色
        swiperefreshlayout.setProgressBackgroundColorSchemeColor(Color.CYAN);
        //设置滑动距离(越小越灵活)
        swiperefreshlayout.setDistanceToTriggerSync(100);
        //设置大小模式
        swiperefreshlayout.setSize(SwipeRefreshLayout.DEFAULT);//默认

4.监听设置
        //设置下拉刷新的监听
        swiperefreshlayout.setOnRefreshListener(new MyOnRefreshListener());

