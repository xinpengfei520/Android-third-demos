1.�Դ�����

compile 'com.android.support:recyclerview-v7:23.4.0'



2.���Զ���ListView��GridView�ķ���

����:adapter = new MyGoveRecyclerViewAdapter(context, datas, ck_gove, tv_price_gove);
            recyclerview_gove.setAdapter(adapter);
            //������ʾ����(������,����,�Ƿ���)
            recyclerview_gove.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

3.����recyclerview��Adapter

public class MyRvAdapter extends RecyclerView.Adapter<MyRvAdapter.ViewHolder>{

 @Override
//�������岼��,���ز���
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

 ��:ViewHolder  viewHolder = new ViewHolder(View.inflate(mContext, R.layout.all_video_item, null), viewType);
     return viewHolder;
}


@Override
//������
    public void onBindViewHolder(ViewHolder holder, int position) {
��:	//1.����λ�õõ�����,������
        final NetAudioBean.ListEntity mediaItem = lists.get(position);
	//2.������
		//��һ����������Ƶ���ŵ�ַ���ڶ�����������ʾ����ĵ�ַ�����������Ǳ���
                viewHolder.jcv_videoplayer.setUp(mediaItem.getVideo().getVideo().get(0), mediaItem.getVideo().getThumbnail().get(0), null);//�ڲٲ�����
                viewHolder.tv_play_nums.setText(mediaItem.getVideo().getPlaycount() + "�β���");//�ı�
                viewHolder.tv_video_duration.setText(utils.stringForTime((long) (mediaItem.getVideo().getDuration() * 1000)) + "");//����ʱ��
	Glide.with(mContext).load(mediaItem.getGif().getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.iv_image_gif);//gif��̬ͼ
      //ͼƬ
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
//�����õ�����
    public int getItemCount() {
       return lists.size();
    }



//����ViewHolder�ڲ���--(������ʼ����ͼ)
class ViewHolder extends RecyclerView.ViewHolder {

��:TextView tv_name;

 public ViewHolder(View itemView) {
            super(itemView);

 ��:tv_name = (TextView) itemView.findViewById(R.id.tv_name);

}

}


}