1.--------------------------------RadioGroup-----------------------------------布局

	<RadioGroup
        android:id="@+id/rg_mian"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@drawable/bottom_tab_bg"
        android:orientation="horizontal">

        <RadioButton
            android:id="@+id/rb_home_main"
            style="@style/textrg"
            android:drawableTop="@drawable/rb_home_select"
            android:text="主页" />

        <RadioButton
            android:id="@+id/rb_news_main"
            style="@style/textrg"
            android:drawableTop="@drawable/rb_news_select"
            android:text="新闻" />

        <RadioButton
            android:id="@+id/rb_hot_main"
            style="@style/textrg"
            android:drawableTop="@drawable/rb_smart_select"
            android:text="商城热卖" />

        <RadioButton
            android:id="@+id/rb_cart_main"
            style="@style/textrg"
            android:drawableTop="@drawable/rb_gove_select"
            android:text="购物车" />

    </RadioGroup>

2.------------------------------- style="@style/textrg"-------------------------------

		 <style name="textrg">

        <item name="android:layout_width">0dp</item>
        <item name="android:layout_height">match_parent</item>
        <item name="android:layout_weight">1</item>
        <item name="android:button">@null</item>
        <item name="android:gravity">center</item>
        <item name="android:textColor">@color/textcolor</item>
        <item name="android:textSize">18sp</item>
    </style>

3------------------------android:drawableTop="@drawable/rb_smart_select"----------------------------------------

<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/govaffair" android:state_checked="false"></item>
    <item android:drawable="@drawable/govaffair_press" android:state_checked="true"></item>
</selector>