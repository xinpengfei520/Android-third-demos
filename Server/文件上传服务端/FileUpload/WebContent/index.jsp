<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>上传文件</h1>
	<p style="color: red">${msg }</p>
	<form action="FileUploadServlet" method="post" enctype="multipart/form-data">
		用户名 <input type="text" name="name" /> <br /> <br />
		照片 <input type="file" name="photo1" /> <br /> <br />
		照片 <input type="file" name="photo2" /> <br /> <br />
		照片 <input type="file" name="photo3" /> <br /> <br />
		<input type="submit" value="提交" />
	</form>
</body>
</html>