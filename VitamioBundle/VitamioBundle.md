1.���ɿ�

2.��ʼ��

        Vitamio.isInitialized(this);

3.���Ȩ��


    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

  <!-- Don't forgot InitActivity -->
        <activity
            android:name="io.vov.vitamio.activity.InitActivity"
            android:configChanges="orientation|screenSize|smallestScreenSize|keyboard|keyboardHidden|navigation"
            android:launchMode="singleTop"
            android:theme="@android:style/Theme.NoTitleBar"
            android:windowSoftInputMode="stateAlwaysHidden" />