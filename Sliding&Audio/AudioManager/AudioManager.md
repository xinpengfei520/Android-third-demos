1.����

 private AudioManager audioManager;//��������������

 //ʵ������������������(���������������������,����û����)
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//��ǰ����
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//�������


 case MotionEvent.ACTION_DOWN://����
                //1.��¼���µ����ʱ�̵ĵ�ǰ����
                mVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                //2.�õ�startY+startX
                startY = event.getY();
                startX = event.getX();
                //3.������Ļ�ĸ�
                touchRang = Math.min(screenHeight, screenWidth);
		
 		break;

 case MotionEvent.ACTION_MOVE://�ƶ�
                //1.endy
                float endY = event.getY();
                //2.����ƫ���� ����Ļ�����ľ���=startY-endY
                float distanceY = startY - endY;
		//3.�ı������=(����Ļ�ϻ����ľ���/��Ļ�ĸ�)*�������
                    delta = (distanceY / touchRang) * maxVolume;

                    //4���յ�����=ԭ������+�ı������
                    int volume = (int) Math.min(Math.max(mVol + delta, 0), maxVolume);

                    if (delta != 0) {
                        updataVolumeProgress(volume);
                    }
		break;