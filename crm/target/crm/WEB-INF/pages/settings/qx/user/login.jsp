<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+
		request.getServerPort()+request.getContextPath()+"/";
%>
<html lang="en">
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">
	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>>
	<script type="text/javascript">
		// $(function () {
		// 	$("#loginBtn").click(function (){
		// 		var loginAct = $("#Act").val();
		// 		var loginPwd = $("#Pwd").val();
		// 		var isRemPwd = $("#Pwd").prop("checked");
		// 		if(loginAct == ""){
		// 			alert("用户名不能为空");
		// 			return;
		// 		}
		// 		if(loginPwd == ""){
		// 			alert("密码不能为空");
		// 			return;
		// 		}
		// 		$.ajax({
		// 			url: 'settings/qx/user/toIndex',
		// 			data: {
		// 				loginAct: loginAct,
		// 				loginPwd: loginPwd,
		// 				isRemPwd: isRemPwd
		// 			},
		// 			type: "post",
		// 		});
 		// 	})
		// })
		function btnClick(){
			var loginAct = $("#Act").val();
			var loginPwd = $("#Pwd").val();
			var isRemPwd = $("#RemPwd").prop("checked");
			if(loginAct == ""){
				alert("用户名不能为空");
				return;
			}
			if(loginPwd == ""){
				alert("密码不能为空");
				return;
			}
			$.ajax({
				url: 'settings/qx/user/toIndex',
				data: {
					loginAct: loginAct,
					loginPwd: loginPwd,
					isRemPwd: isRemPwd
				},
				type: "post",
			});
		}
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2019&nbsp;动力节点</span></div>
	</div>

	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input id="Act" class="form-control" type="text" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input id="Pwd" class="form-control" type="password" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<input id="RemPwd" type="checkbox"> 十天内免登录
						</label>
						&nbsp;&nbsp;
						<span id="msg"></span>
					</div>
					<button type="button" onclick="btnClick()" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>