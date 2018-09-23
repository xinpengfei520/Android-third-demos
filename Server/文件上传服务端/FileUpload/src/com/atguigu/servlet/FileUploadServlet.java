package com.atguigu.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class FileUploadServlet
 */
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//解决post请求的乱码
		//request.setCharacterEncoding("utf-8");
		
	/*	//获取一个输入流
		ServletInputStream in = request.getInputStream();
		
		//读取流
		String str = IOUtils.toString(in);
		
		System.out.println(str);*/
		
		
		//获取工厂类实例
		DiskFileItemFactory factory = new DiskFileItemFactory();
		
		//创建解析器类实例
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		
		//fileUpload通过该对象来限制文件的大小
		//设置当文件的大小为50KB
		//fileUpload.setFileSizeMax(1024*50);
		
		//设置多个文件的总大小为300mb
		fileUpload.setSizeMax(1024*1024*300);
		
		try {
			//解析request
			List<FileItem> fileItems = fileUpload.parseRequest(request);
			
			//遍历fileItems，读取表单的信息
			for (FileItem fileItem : fileItems) {
				
				//判断当前表单项是否是一个普通表单项
				if(fileItem.isFormField()){
					//获取属性名
					String fieldName = fileItem.getFieldName();
					
					//获取属性值
					String value = fileItem.getString("utf-8");
					
					System.out.println(fieldName+" = "+value);
				}else{
					//如果是文件表单项
					//获取文件的大小
					long size = fileItem.getSize();
					
					//判断size是否为0
					if(size==0){
						continue;
					}
					
					//获取文件的类型
					String contentType = fileItem.getContentType();
					
					//获取文件的名字
					String name = fileItem.getName();
					//判断name中是否包含有路径信息
					if(name.contains("\\")){
						//如果包含则截取字符串
						name = name.substring(name.lastIndexOf("\\")+1);
					}
					
					//生成一个UUID，作为文件名的前缀
					String prefix = UUID.randomUUID().toString().replace("-", "");
					name = prefix+"_"+name;
					
					
					//获取表单项的属性名
					String fieldName = fileItem.getFieldName();
					
					System.out.println("文件的大小: "+size);
					System.out.println("文件的类型: "+contentType);
					System.out.println("文件的名字: "+name);
					System.out.println("表单项name属性名: "+fieldName);
					
					//获取ServletContext对象
					ServletContext context = this.getServletContext();
					//获取项目的真实路径
					String path = context.getRealPath("/upload");
					
					//判断路径是否存在
					File file = new File(path);
					if(!file.exists()){
						//如果不存在该路径，则创建一个路径
						file.mkdirs();
					}
					
					//将文件写入到磁盘中
					fileItem.write(new File(path+"/"+name));
					
				}
			}
			
			//重定向到一个页面
			response.sendRedirect(request.getContextPath()+"/success.jsp");
			
		}catch(FileSizeLimitExceededException e){
			//一但捕获到该异常，则说明单个文件大小超过限制。
			//设置一个错误消息
			request.setAttribute("msg", "单个文件大小请不要超过50KB");
			//转发到index.jsp
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}catch(SizeLimitExceededException e){
			//一但捕获到该异常，则说明单个文件大小超过限制。
			//设置一个错误消息
			request.setAttribute("msg", "所有文件大小请不要超过300mb");
			//转发到index.jsp
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
		
		catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
