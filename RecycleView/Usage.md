1.自带导包

compile 'com.android.support:recyclerview-v7:23.4.0'



2.可以定义ListView和GridView的方向

代码:adapter = new MyGoveRecyclerViewAdapter(context, datas, ck_gove, tv_price_gove);
            recyclerview_gove.setAdapter(adapter);
            //设置显示方向(上下文,方向,是否倒序)
            recyclerview_gove.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

3.设置recyclerview的Adapter

public class MyRvAdapter extends RecyclerView.Adapter<MyRvAdapter.ViewHolder>{

 @Override
//用来定义布局,返回布局
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

 例:ViewHolder  viewHolder = new ViewHolder(View.inflate(mContext, R.layout.all_video_item, null), viewType);
     return viewHolder;
}


@Override
//绑定数据
    public void onBindViewHolder(ViewHolder holder, int position) {
例:	//1.根据位置得到数据,绑定数据
        final NetAudioBean.ListEntity mediaItem = lists.get(position);
	//2.绑定数据
		//第一个参数是视频播放地址，第二个参数是显示封面的地址，第三参数是标题
                viewHolder.jcv_videoplayer.setUp(mediaItem.getVideo().getVideo().get(0), mediaItem.getVideo().getThumbnail().get(0), null);//节操播放器
                viewHolder.tv_play_nums.setText(mediaItem.getVideo().getPlaycount() + "次播放");//文本
                viewHolder.tv_video_duration.setText(utils.stringForTime((long) (mediaItem.getVideo().getDuration() * 1000)) + "");//分秒时间
	Glide.with(mContext).load(mediaItem.getGif().getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.iv_image_gif);//gif动态图
      //图片
      viewHolder.iv_image_icon.setImageResource(R.drawable.bg_item);
                int height = mediaItem.getImage().getHeight() <= DensityUtil.getScreenHeight() * 0.75 ? mediaItem.getImage().getHeight() : (int) (DensityUtil.getScreenHeight() * 0.75);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.getScreenWidth(), height);
                viewHolder.iv_image_icon.setLayoutParams(params);
                if (mediaItem.getImage() != null && mediaItem.getImage().getBig() != null && mediaItem.getImage().getBig().size() > 0) {
//                    x.image().bind(viewHolder.iv_image_icon, mediaItem.getImage().getBig().get(0));
                    Glide.with(mContext).load(mediaItem.getImage().getBig().get(0)).placeholder(R.drawable.bg_item).error(R.drawable.bg_item).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.iv_image_icon);
                }

}



 @Override
//用来得到数据
    public int getItemCount() {
       return lists.size();
    }



//定义ViewHolder内部类--(用来初始化视图)
class ViewHolder extends RecyclerView.ViewHolder {

例:TextView tv_name;

 public ViewHolder(View itemView) {
            super(itemView);

 例:tv_name = (TextView) itemView.findViewById(R.id.tv_name);

}

}


}