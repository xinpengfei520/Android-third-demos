1.ȥѶ�ɿ���ƽ̨ע�Ὺ�����˺�

1109711383@qq.com

lijingxin0520

2.����Ӧ������key

57de8d91


3.��������Ӧ�ö�Ӧ�İ���

4.���ɵ���Ŀ��

    �ٿ���2��jar��
		Msc.jar,   Sunflower.jar
  
    ����main���洴��jniLibsĿ¼,���assetsĿ¼

    �۴�����  JsonParser.java

    �ܳ�ʼ��

	//��ʼ���ƴ�Ѷ��
        // ����12345678���滻��������� APPID�������ַ�� http://www.xfyun.cn
        // �����ڡ� =�� �� appid ֮�����������ַ�����ת���
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=57de8d91");


    �ݴ���  showDialog()

 private void showDialog() {
        //1.����RecognizerDialog����
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener());
        //2.����accent�� language�Ȳ���
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//����
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");//��ͨ��
        //��Ҫ��UI�ؼ�����������⣬����������²������ã�����֮��onResult�ص����ؽ����������
        //���
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.���ûص��ӿ�
        mDialog.setListener(new MyRecognizerDialogListener());
        //4.��ʾdialog��������������
        mDialog.show();
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener {

        /**
         * @param recognizerResult
         * @param b                �Ƿ�˵������
         */
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String result = recognizerResult.getResultString();
//            Log.e("MainActivity", "result ==" + result);
            String text = JsonParser.parseIatResult(result);
            //�����õ�
//            Log.e("MainActivity", "text ==" + text);

            String sn = null;
            // ��ȡjson����е�sn�ֶ�
            try {
                JSONObject resultJson = new JSONObject(recognizerResult.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();//ƴ��һ��
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }

            String res = resultBuffer.toString();
            res.replace("��",",");//�Ѿ���滻�ɶ���
            etInput.setText(res);
            etInput.setSelection(etInput.length());

        }

        /**
         * ������
         *
         * @param speechError
         */
        @Override
        public void onError(SpeechError speechError) {
//            Log.e("MainActivity", "onError ==" + speechError.getMessage());

        }
    }


    class MyInitListener implements InitListener {

        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS) {
                Toast.makeText(ShouSuoActivity.this, "��ʼ��ʧ��", Toast.LENGTH_SHORT).show();
            }
        }
    }
		

	��Ȩ��
		
	<!--��������Ȩ�ޣ�����ִ���ƶ��������� -->
    <uses-permission android:name="android.permission.INTERNET"/>
    <!--��ȡ�ֻ�¼����ʹ��Ȩ�ޣ���д��ʶ�����������Ҫ�õ���Ȩ�� -->
    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
    <!--��ȡ������Ϣ״̬ -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <!--��ȡ��ǰwifi״̬ -->
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <!--�������ı���������״̬ -->
    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
    <!--��ȡ�ֻ���ϢȨ�� -->
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    <!--��ȡ��ϵ��Ȩ�ޣ��ϴ���ϵ����Ҫ�õ���Ȩ�� -->
    <uses-permission android:name="android.permission.READ_CONTACTS"/>
    <!--��洢дȨ�ޣ� �����﷨��Ҫ�õ���Ȩ�� -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <!--��洢��Ȩ�ޣ������﷨��Ҫ�õ���Ȩ�� -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <!--����Ȩ�ޣ�������¼Ӧ��������Ϣ -->
    <uses-permission android:name="android.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="android.permission.VIBRATE" />