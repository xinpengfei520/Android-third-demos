1.自带的库,添加库

 compile 'com.android.support:design:23.4.0'

2.布局

  <android.support.design.widget.TabLayout
        android:id="@+id/tabLayout"
        android:layout_width="match_parent"
        style="@style/MyCustomTabLayout"
        android:layout_height="wrap_content"

        />

3.样式

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


4.TabLayout与viewpager关联

  tabLayout.setupWithViewPager(viewpager_news);


 5.为TabLayout设置滑动

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


6.想要显示头部标题的需在viewpager中重写此方法

 //最重要的显示头部滑动的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return mChildren.get(position).getTitle();
        }