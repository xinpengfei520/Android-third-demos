1.����

ʵ����

 try {
            //����ֻ����Զ��ĵ���������,������
            if (android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE) == android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                android.provider.Settings.System.putInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                //��ȡ��ǰ������
                currentBrightness = android.provider.Settings.System.getInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        255);

            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

		 case MotionEvent.ACTION_MOVE://�ƶ�

		//1.endy
                float endY = event.getY();
                //2.����ƫ���� ����Ļ�����ľ���=startY-endY
                float distanceY = startY - endY;
		 delta = (distanceY / touchRang) * 50;

                    int bright = (int) Math.min(Math.max(currentBrightness + delta, 0), 255);
                    if (delta != 0) {
                        setScreenBritness(bright);
                    }
		 break;

//---------------------��ؼ��Ĵ���
 //�������Ⱥ��ķ���
    private void setScreenBritness(int brightness) {
        //������Ļȫ��
        if (brightness <= 1) {
            brightness = 1;
        }
        //���õ�ǰactivity����Ļ����
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        //0��1,�������Ȱ���ȫ��
        lp.screenBrightness = Float.valueOf(brightness / 255f);
        this.getWindow().setAttributes(lp);

        //����Ϊϵͳ���ȷ���1
        android.provider.Settings.System.putInt(getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS,
                brightness);

        //����Ϊϵͳ���ȷ���2
//        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
//        android.provider.Settings.System.putInt(getContentResolver(), "screen_brightness", brightness);
//        // resolver.registerContentObserver(uri, true, myContentObserver);
//        getContentResolver().notifyChange(uri, null);
        currentBrightness = brightness;

    }

-----------���Ȩ��
    <uses-permission android:name="android.permission.WRITE_SETTINGS"/>
