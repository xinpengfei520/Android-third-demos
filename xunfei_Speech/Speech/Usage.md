1.去讯飞开放平台注册开发者账号

1109711383@qq.com

lijingxin0520

2.创建应用申请key

57de8d91


3.下载申请应用对应的案例

4.集成到项目中

    ①拷入2个jar包
		Msc.jar,   Sunflower.jar
  
    ②在main里面创建jniLibs目录,添加assets目录

    ③创建类  JsonParser.java

    ④初始化

	//初始化科大讯飞
        // 将“12345678”替换成您申请的 APPID，申请地址： http://www.xfyun.cn
        // 请勿在“ =” 与 appid 之间添加任务空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=57de8d91");


    ⑤代码  showDialog()

 private void showDialog() {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener());
        //2.设置accent、 language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//中文
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");//普通话
        //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解
        //结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener(new MyRecognizerDialogListener());
        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener {

        /**
         * @param recognizerResult
         * @param b                是否说话结束
         */
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String result = recognizerResult.getResultString();
//            Log.e("MainActivity", "result ==" + result);
            String text = JsonParser.parseIatResult(result);
            //解析好的
//            Log.e("MainActivity", "text ==" + text);

            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(recognizerResult.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();//拼成一句
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }

            String res = resultBuffer.toString();
            res.replace("。",",");//把句号替换成逗号
            etInput.setText(res);
            etInput.setSelection(etInput.length());

        }

        /**
         * 出错了
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
                Toast.makeText(ShouSuoActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
		

	⑥权限
		
	<!--连接网络权限，用于执行云端语音能力 -->
    <uses-permission android:name="android.permission.INTERNET"/>
    <!--获取手机录音机使用权限，听写、识别、语义理解需要用到此权限 -->
    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
    <!--读取网络信息状态 -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <!--获取当前wifi状态 -->
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <!--允许程序改变网络连接状态 -->
    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
    <!--读取手机信息权限 -->
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    <!--读取联系人权限，上传联系人需要用到此权限 -->
    <uses-permission android:name="android.permission.READ_CONTACTS"/>
    <!--外存储写权限， 构建语法需要用到此权限 -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <!--外存储读权限，构建语法需要用到此权限 -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <!--配置权限，用来记录应用配置信息 -->
    <uses-permission android:name="android.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="android.permission.VIBRATE" />