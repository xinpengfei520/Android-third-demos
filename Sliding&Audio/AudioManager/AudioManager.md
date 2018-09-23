1.代码

 private AudioManager audioManager;//调节声音管理类

 //实例化调节声音管理类(把声音放在里面可有声音,否则没声音)
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//当前音量
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//最大音量


 case MotionEvent.ACTION_DOWN://按下
                //1.记录按下的这个时刻的当前音量
                mVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                //2.得到startY+startX
                startY = event.getY();
                startX = event.getX();
                //3.计算屏幕的高
                touchRang = Math.min(screenHeight, screenWidth);
		
 		break;

 case MotionEvent.ACTION_MOVE://移动
                //1.endy
                float endY = event.getY();
                //2.计算偏移量 在屏幕滑动的距离=startY-endY
                float distanceY = startY - endY;
		//3.改变的声音=(在屏幕上滑动的距离/屏幕的高)*最大音量
                    delta = (distanceY / touchRang) * maxVolume;

                    //4最终的声音=原来音量+改变的声音
                    int volume = (int) Math.min(Math.max(mVol + delta, 0), maxVolume);

                    if (delta != 0) {
                        updataVolumeProgress(volume);
                    }
		break;