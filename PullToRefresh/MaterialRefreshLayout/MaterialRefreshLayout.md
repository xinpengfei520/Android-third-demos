1.集成库

2.布局代码(此布局是非侵入式布局,还有更多的布局)
 <com.cjj.MaterialRefreshLayout xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/refresh_hot"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:isLoadMore="true"
        app:overlay="true"
        app:progress_colors="@array/material_colors"
        app:progress_size_type="normal"
        app:wave_color="#90ffffff"
        app:wave_height_type="normal"
        app:wave_show="true">


        <android.support.v7.widget.RecyclerView----------其中可以包裹任何布局,例:RecyclerView
            android:id="@+id/recyclerview_hot"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />


    </com.cjj.MaterialRefreshLayout>

注:使用此布局得监听刷新的状态-----setMaterialRefreshListener
例:
 refresh_hot.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override//下拉刷新
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                state = STATE_REFRESH;//下拉刷新状态
                curpag = 1;//默认是第一页
                mHotUrl = Constans.SHOP_HOT_URL + "=" + pagerSize + "&curPage=" + curpag;
                //重新 联网请求
                getDataFromNetOkhttp();

            }

            @Override//上拉刷新
           public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                if (curpag < totalpager) {//如果当前页面小于总页面
                    state = STATE_LOADMORE;
                    curpag += 1;
                    mHotUrl = Constans.SHOP_HOT_URL + "=" + pagerSize + "&curPage=" + curpag;
                    //重新 联网请求
                    getDataFromNetOkhttp();

                } else {
                    Toast.makeText(context, "已经到底了!", Toast.LENGTH_SHORT).show();
                    //把加载更多的UI还原
                    refresh_hot.finishRefreshLoadMore();
                }
            }
        });

    }
	