1.���ɿ�

2.���ִ���(�˲����Ƿ�����ʽ����,���и���Ĳ���)
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


        <android.support.v7.widget.RecyclerView----------���п��԰����κβ���,��:RecyclerView
            android:id="@+id/recyclerview_hot"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />


    </com.cjj.MaterialRefreshLayout>

ע:ʹ�ô˲��ֵü���ˢ�µ�״̬-----setMaterialRefreshListener
��:
 refresh_hot.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override//����ˢ��
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                state = STATE_REFRESH;//����ˢ��״̬
                curpag = 1;//Ĭ���ǵ�һҳ
                mHotUrl = Constans.SHOP_HOT_URL + "=" + pagerSize + "&curPage=" + curpag;
                //���� ��������
                getDataFromNetOkhttp();

            }

            @Override//����ˢ��
           public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                if (curpag < totalpager) {//�����ǰҳ��С����ҳ��
                    state = STATE_LOADMORE;
                    curpag += 1;
                    mHotUrl = Constans.SHOP_HOT_URL + "=" + pagerSize + "&curPage=" + curpag;
                    //���� ��������
                    getDataFromNetOkhttp();

                } else {
                    Toast.makeText(context, "�Ѿ�������!", Toast.LENGTH_SHORT).show();
                    //�Ѽ��ظ����UI��ԭ
                    refresh_hot.finishRefreshLoadMore();
                }
            }
        });

    }
	