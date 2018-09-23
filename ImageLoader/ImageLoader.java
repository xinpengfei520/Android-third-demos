package com.example.day13;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;


/**
 * �Զ���ʹ����������ʵ��ͼƬ���ص���
 * 
 * @author liyuting
 * 
 * ��֪ ImageView��path
 * 
 * ��ͼƬ��ʾ��Imageview�ϣ�
 * 
 * 
 * ��ȥ�ж��ڴ������޴���ͼƬ�����û�У�������һ��
 * �ж��ļ������޴���ͼƬ�����û�У�������һ��
 * �������ͼƬ
 *
 * 
 * 
 * 
 *
 */
public class ImageLoader {
	
	HashMap<String,Bitmap> map = new HashMap<String,Bitmap>();
	File file;
	Context context;
	ImageView view;
	String path;
	public ImageLoader(Context context,ImageView view, String path) {
		super();
		this.view = view;
		this.path = path;
		
		this.context=context;
	}
	
	
	/*
	 * ����ͼƬ
	 */
	public void loadImage(){
		
//		����һ����ȥ�ж��ڴ������޴���ͼƬ�����û�У�������һ��
		
		Bitmap bitmap = fromFirstCache();
		
		if (bitmap!=null) {
			
			System.out.println("�ӵ�һ�������õ�ͼƬ");
			view.setImageBitmap(bitmap);
			return;
			
		} 
//		���������ȥ�ж��ļ������޴���ͼƬ�����û�У�������һ��
		
		 bitmap = fromSecondCache();
		
		if (bitmap!=null) {
			
			System.out.println("�ӵڶ��������õ�ͼƬ");
			view.setImageBitmap(bitmap);
			return;
			
		} 
//		��������ֱ�Ӵ��������ͼƬ����������
		
		fromThirdCache();
		
	}


	//ʹ��������أ���ʾͼƬ
	private void fromThirdCache() {
		AsyncTask<String,Void,Bitmap> task = new AsyncTask<String,Void,Bitmap>(){

			@Override
			protected Bitmap doInBackground(String... params) {
				
				try {
					//1.����url�������
					URL url = new URL(params[0]);

					//2.�����Ӳ���ȡ���Ӷ���
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();

					//3.�������Ӳ���
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);

					//4.��ȡ��Ӧ��
					int code = conn.getResponseCode();

					if (code == 200) {//����ɹ�

						//5.��ȡ��Ӧ��������
						InputStream is = conn.getInputStream();
						
						Bitmap bitmap = BitmapFactory.decodeStream(is);
						
						return bitmap;
						
						

					} 
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				
				return null;
			}
			
			
			protected void onPostExecute(Bitmap result) {
				if (result==null) {
				} else {
					
					//��ͼƬ��ʾ��imageview
					System.out.println("�����������õ���ͼƬ");
					view.setImageBitmap(result);
					
					//���浽�������桪���ļ���
					
					FileOutputStream fos;
					
					
					try {
						if (Environment.getExternalStorageState().equals("mounted")) {//sd������
							file=new File(Environment.getExternalStorageDirectory(),path.substring(path.lastIndexOf("/")));
							
							
							
							
						} else {//sd���ǹ���
							
							file=new File(context.getFilesDir(),path.substring(path.lastIndexOf("/")));
						}
						
						fos=new FileOutputStream(file);
						
						//��ͼƬд�뵽ָ��������
						
						result.compress(CompressFormat.JPEG, 100, fos);
						
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					
					
					
					
					
					//���浽һ�����桪���ڴ���
					
					map.put(path, result);

				}
				
				
			};
			
			
		}.execute(path);
		
		
		
	}


	private Bitmap fromSecondCache() {
		try {
			
			if (Environment.getExternalStorageState().equals("mounted")) {//sd������
				file=new File(Environment.getExternalStorageDirectory(),path.substring(path.lastIndexOf("/")));
				
				
				
				
			} else {//sd���ǹ���
				
				file=new File(context.getFilesDir(),path.substring(path.lastIndexOf("/")));
			}
			FileInputStream fis= new FileInputStream(file);
			
			Bitmap bitmap=BitmapFactory.decodeStream(fis);
			
			
			//���浽һ��
			
			
			map.put(path, bitmap);
			return bitmap;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		return null;
	}


	private Bitmap fromFirstCache() {
		return map.get(path);
	}
	
	
	
	
	

}
