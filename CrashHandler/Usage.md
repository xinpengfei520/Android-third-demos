1.把代码拷贝到工程中

2.在Application子类中初始化

//监听程序异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
