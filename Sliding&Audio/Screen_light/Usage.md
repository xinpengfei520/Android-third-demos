1.代码

实例化

 try {
            //如果手机是自动的调整的亮度,就屏蔽
            if (android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE) == android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                android.provider.Settings.System.putInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                //获取当前的亮度
                currentBrightness = android.provider.Settings.System.getInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        255);

            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

		 case MotionEvent.ACTION_MOVE://移动

		//1.endy
                float endY = event.getY();
                //2.计算偏移量 在屏幕滑动的距离=startY-endY
                float distanceY = startY - endY;
		 delta = (distanceY / touchRang) * 50;

                    int bright = (int) Math.min(Math.max(currentBrightness + delta, 0), 255);
                    if (delta != 0) {
                        setScreenBritness(bright);
                    }
		 break;

//---------------------最关键的代码
 //调整亮度核心方法
    private void setScreenBritness(int brightness) {
        //不让屏幕全暗
        if (brightness <= 1) {
            brightness = 1;
        }
        //设置当前activity的屏幕亮度
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        //0到1,调整亮度暗到全亮
        lp.screenBrightness = Float.valueOf(brightness / 255f);
        this.getWindow().setAttributes(lp);

        //保存为系统亮度方法1
        android.provider.Settings.System.putInt(getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS,
                brightness);

        //保存为系统亮度方法2
//        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
//        android.provider.Settings.System.putInt(getContentResolver(), "screen_brightness", brightness);
//        // resolver.registerContentObserver(uri, true, myContentObserver);
//        getContentResolver().notifyChange(uri, null);
        currentBrightness = brightness;

    }

-----------添加权限
    <uses-permission android:name="android.permission.WRITE_SETTINGS"/>
