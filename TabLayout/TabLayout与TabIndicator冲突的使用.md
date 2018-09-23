#TabLayout的使用(与ViewPagerIndiaCator类似,头部布局显示)

#TabLayout原生的用法

参照网址：
  https://segmentfault.com/a/1190000003500271 

1.关联库

 compile 'com.android.support:design:23.3.0'(自带库去添加)


2.布局写上TabLayout

     <android.support.design.widget.TabLayout
      android:id="@+id/tabLayout"
      android:layout_width="0dp"
      android:layout_height="wrap_content"
      android:layout_gravity="center_vertical"
      android:layout_weight="1" />
       


3.实例化

    @ViewInject(R.id.tablayout)
    private TabLayout tabLayout;


4.TabLayout和ViewPager关联

       tabLayout.setupWithViewPager(viewPager);
        //注意以后监听页面的变化 ，TabPageIndicator监听页面的变化
//      tabPagerIndicator.setOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


5.适配器中重写getPageTitle方法

 class MyNewsMenuDetailPagerAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).getTitle();
        }

       .....
    }


6.解决TabLayout和ViewPagerIndicator的兼容问题

   6.1  <activity
            android:name=".activity.MainActivity"
            android:theme="@style/Theme.PageIndicatorDefaults" />


   
  6.2 ViewPagerIndicator库修改成如下


    <style name="Theme.PageIndicatorDefaults" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="vpiIconPageIndicatorStyle">@style/Widget.IconPageIndicator</item>
        <item name="vpiTabPageIndicatorStyle">@style/Widget.TabPageIndicator</item>
    </style>

 6.3添加V7包
     compile 'com.android.support:appcompat-v7:23.3.0'

 
#TabLayout设置指针颜色和高度&文字颜色


1.添加样式

      <android.support.design.widget.TabLayout
            android:id="@+id/tabLayout"
            android:layout_width="wrap_content"
            style="@style/MyCustomTabLayout"
            android:layout_height="wrap_content"
            android:layout_weight="1" />





2. values目录下styles.xml里面
    MyCustomTabLayout样式

  <style name="MyCustomTabLayout" parent="Widget.Design.TabLayout">

        <item name="tabMaxWidth">72dp</item>
        <item name="tabMinWidth">72dp</item>
        <item name="tabIndicatorColor">#ff0000</item>
        <item name="tabIndicatorHeight">2dp</item>
        <item name="tabTextAppearance">@style/MyCustomTabTextAppearance</item>
        <item name="tabSelectedTextColor">@android:color/holo_red_light</item>

    </style>

    <style name="MyCustomTabTextAppearance" parent="TextAppearance.Design.Tab">
        <item name="android:textSize">16sp</item>
        <item name="android:textColor">@android:color/black</item>
        <item name="textAllCaps">false</item>
    </style>

   




#TabLayout自定义样式

1.设置的样式

 

   

  
        vp_news_menu_detailpager.setAdapter(adapter);


        //6.关联ViewPager,TabPageIndicator才可以显示

        tabLayout.setupWithViewPager(vp_news_menu_detailpager);

        //设置滚动模式

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置页面的监听需要用TabPageIndicator

        
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }



        注意了setupWithViewPager必须在ViewPager.setAdapter()之后调用

//自定义Tab

2.在适配器中添加getTabView()方法
      public View getTabView(int position){
        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        TextView tv= (TextView) view.findViewById(R.id.textView);
        tv.setText(childrenDatas.get(position).getTitle());
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setImageResource(R.drawable.dot_focus);
        return view;
      }



     布局文件tab_item.xml文件

	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="wrap_content"
	    android:layout_height="wrap_content"
	    android:gravity="center_horizontal"
	    android:orientation="vertical">
	
	
	    <TextView
	        android:text="text"
	        android:id="@+id/textView"
	        android:textColor="#000000"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_marginLeft="3dp" />
	
	    <ImageView
	        android:src="@drawable/dot_focus"
	        android:id="@+id/imageView"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_centerVertical="true" />
	
	</LinearLayout>
