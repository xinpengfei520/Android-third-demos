1.�Դ��Ŀ�,��ӿ�

 compile 'com.android.support:design:23.4.0'

2.����

  <android.support.design.widget.TabLayout
        android:id="@+id/tabLayout"
        android:layout_width="match_parent"
        style="@style/MyCustomTabLayout"
        android:layout_height="wrap_content"

        />

3.��ʽ

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


4.TabLayout��viewpager����

  tabLayout.setupWithViewPager(viewpager_news);


 5.ΪTabLayout���û���

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


6.��Ҫ��ʾͷ�����������viewpager����д�˷���

 //����Ҫ����ʾͷ�������ı���
        @Override
        public CharSequence getPageTitle(int position) {
            return mChildren.get(position).getTitle();
        }