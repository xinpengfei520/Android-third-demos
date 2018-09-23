1.���ɿ�

2.��ʼ��

initUniversalImageLoader();


 private void initUniversalImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new ColorDrawable(Color.parseColor("#f0f0f0")))
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(new FadeInBitmapDisplayer(1000)) // ����ͼƬ���Ե�ʱ��
//                .delayBeforeLoading(300)  // ����ǰ���ӳ�ʱ��
                .build();

        int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        // Use 1/8th of the available memory for this memory cache.
        int memCacheSize = 1024 * 1024 * memClass / 8;

        File cacheDir = new File(Environment.getExternalStorageDirectory().getPath() + "/jiecao/cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3) // default  �̳߳��ڼ��ص�����
                .threadPriority(Thread.NORM_PRIORITY - 2) // default ���õ�ǰ�̵߳����ȼ�
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new UsingFreqLimitedMemoryCache(memCacheSize)) // You can pass your own memory cache implementation/
                .memoryCacheSize(memCacheSize) // �ڴ滺������ֵ
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiskCache(cacheDir))//�Զ��建��·��
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)��ʱʱ��
                .defaultDisplayImageOptions(options)
//                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

3.����


        <fm.jiecao.jcvideoplayer_lib.JCVideoPlayer
            android:id="@+id/jcv_videoplayer"
            android:layout_width="match_parent"
            android:layout_height="220dp"
            android:orientation="vertical" />