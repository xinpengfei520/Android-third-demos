1.�������������---����ȫ��Ҫʵ�ֽӿڵķ���,���嵥�б� ע��,actionע��.

2.����aidl��,����ȫ���ǽӿ�,����buildһ��,�Զ�����һ����(build/generated/source/aidl/debug��)

3.�ڷ�������д�˴���

public IMusicPlayerService.Stub stub=new IMusicPlayerService.Stub(){
������Ҫʵ�ֵķ���
}

4.�󶨺Ϳ�������
  private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayService.class);
        intent.setAction("com.lijingxin.OPENAUDIO");
        bindService(intent, con, Context.BIND_AUTO_CREATE);
        startService(intent);//��������
    }
-----ʵ�ִ˽ӿ�
 private ServiceConnection con = new ServiceConnection() {//ʵ�ִ˽ӿ�
        @Override
        //���ӷ���(Ҫ�õ�����Ĵ�����)
        public void onServiceConnected(ComponentName name, IBinder service) {
            IMusicPlayerService service1 = IMusicPlayerService.Stub.asInterface(service);
            if (service1 != null) {//�����Ϊ��
                try {
                    service1.openAudio(position);//����λ�õõ����Ӧ����Ƶ
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }


        }

        @Override
        //�Ͽ�����
        public void onServiceDisconnected(ComponentName name) {

        }
    };

        @Override
        //�Ͽ�����
        public void onServiceDisconnected(ComponentName name) {

        }
    };

