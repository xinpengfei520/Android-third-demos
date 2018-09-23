1.创建服务的子类---里面全是要实现接口的方法,在清单列表 注册,action注册.

2.创建aidl类,里面全都是接口,重新build一下,自动生成一个类(build/generated/source/aidl/debug中)

3.在服务类中写此代码

public IMusicPlayerService.Stub stub=new IMusicPlayerService.Stub(){
里面需要实现的方法
}

4.绑定和开启服务
  private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayService.class);
        intent.setAction("com.lijingxin.OPENAUDIO");
        bindService(intent, con, Context.BIND_AUTO_CREATE);
        startService(intent);//开启服务
    }
-----实现此接口
 private ServiceConnection con = new ServiceConnection() {//实现此接口
        @Override
        //连接服务(要得到服务的代理类)
        public void onServiceConnected(ComponentName name, IBinder service) {
            IMusicPlayerService service1 = IMusicPlayerService.Stub.asInterface(service);
            if (service1 != null) {//如果不为空
                try {
                    service1.openAudio(position);//根据位置得到相对应的音频
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }


        }

        @Override
        //断开服务
        public void onServiceDisconnected(ComponentName name) {

        }
    };

        @Override
        //断开服务
        public void onServiceDisconnected(ComponentName name) {

        }
    };

