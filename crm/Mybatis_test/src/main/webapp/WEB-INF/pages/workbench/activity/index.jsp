<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String base = request.getScheme()+"://"+request.getServerName()+":"
			+request.getServerPort()+request.getContextPath()+"/";
	System.out.println("index.jspd的base ： " + base);
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=base%>">
	<meta charset="UTF-8">

<%---引入bootstrap的css代码--%>
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<%---引入bootstrap中日历的css代码--%>
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<%--引入bootstrap中分页的css代码--%>
<link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet" />

<%---引入jquery的js代码--%>
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<%---引入bootstrap的js代码--%>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<%---引入bootstrap中日历的js代码--%>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<%--引入bootstrap中分页的js代码--%>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">
	//入口函数，当页面中所有元素都加载完成后执行
	$(function(){

		//创建市场活动
		//1. 给创建按钮添加弹出模态窗口的事件
		$("#createActivityBtn").click(function () {

			//弹出模态窗口前重置表单（情空表单数据）
			//使用get(0)将jQuery对象转变为document对象
			$("#createActivityForm").get(0).reset();

			//弹出创建市场活动的模态窗口
			$("#createActivityModal").modal("show");
		})
		//2. 给保存按钮添加单机事件
		$("#saveActivityBtn").click(function () {
			//收集创建市场的表单数据
			let owner = $("#create-marketActivityOwner").val();
			let name = $.trim($("#create-marketActivityName").val());
			let start_date = $("#create-startTime").val();
			let end_date = $("#create-endTime").val();
			let cost = $.trim($("#create-cost").val());
			let description = $.trim($("#create-describe").val());

			//表单数据校验
			//表单数据错误返回0，数据正确返回1
			let returnInt = formDataVerification(owner, name, start_date, end_date, cost);

			//返回1，才发送异步请求
			if(returnInt == 1){
				//异步请求将表单数据返回到Controller
				$.ajax({
					url: "workbench/activity/saveCreateActivity",
					data: {
						//此处返回后台的字符名必须与Activity的属性名一致，否则后台不能接收到数据
						owner: owner,
						name: name,
						start_date: start_date,
						end_date: end_date,
						cost: cost,
						description: description
					},
					type: "post",
					success: function (data){
						if(data.code == "1"){
							//关闭模态窗口
							//$("#createActivityModal").modal("hide");
							$("#closeBtn").click();

							//刷新市场活动列，显示第一页数据，保持每页显示条数不变
							selectActivityByConditionForPage(1,$("#pagination_Div").bs_pagination("getOption", "rowsPerPage"));
						}else{
							alert(data.message);
						}
					}
				});
			}
		})

		//当页面加载完成时，对开始日期调用日历的工具函数
		//使用class属性的选择器一下选择两个标签，开始标签，和结束标签
		$(".dateTime").datetimepicker({
			// container: "#createActivityForm",
			language: "zh-CN",	//语言
			format: "yyyy-mm-dd",	//日历的日期格式
			minView: "month",	//可以选择的最小视图
			initialDate: new Date(),	//初始化显示的日期
			autoclose: 1,		//设置选择完日期或时间之后，日历是否自动关闭
			todayBtn: 1,		//设置是否显示”今天“的日期按钮
			todayHighlight: 1,	//今天日期按钮显示高亮
			clearBtn: 1		//是否有情况日期的按钮
		});

		//市场活动主页面加载完成之后，展示所有的数据
		selectActivityByConditionForPage(1,10);

		//单击查询按钮后刷新展示的数据
		//$("#pagination_Div").bs_pagination("getOption", "rowsPerPage")     获取上次查询时，页面显示的条数信息
		$("#selectConditionBtn").click(function () {
			selectActivityByConditionForPage(1,$("#pagination_Div").bs_pagination("getOption", "rowsPerPage"));
		});

		//全选按钮单击事件
		$("#checkAll").click(function (){
			//this代表当前按钮
			// if(this.checked){
			// 	//获取tBoby下的所有input中类型为checkbox的标签。prop是设置checked的选中状态
			// 	$("#tBody input[type='checkedbox']").prop("checked",true);
			// }else{
			// 	$("#tBody input[type='checkedbox']").prop("checked",true);
			// }

			//设置tBoby下的所有input中类型为checkbox的标签的选中状态与全选按钮的状态一致
			$("#tBody input[type='checkbox']").prop("checked",this.checked);
		})

		/*如果列表中的所有checkbox选中，则全选按钮也选中，列表中只要有一个checkbox没被选中，则全选按钮不选中
		使用jQuery对象：“选择器.xxxx()”来给元素添加事件，这种添加方式存在局限性，只能给固有元素添加事件，不能给动态元素添加事件
		$("#tBody input[type='checkbox']").click(function () {
			//获取列表中所有checkbox的个数
			let allCheckBoxSize = $("#tBody input[type='checkbox']").size();
			//获取列表中所有被选中checkbox的个数
			let allCheckBoxCheckedSize =  $("#tBody input[type='checkbox']:checked").size();
			//当所有checkbox的个数 等于 所有被选中checkbox的个数时，表示所有的checkbox都被选中了，全选按钮为选中状态
			if(allCheckBoxSize == allCheckBoxCheckedSize){
				//将全选按钮设置为选中状态
				$("#checkAll").prop("checked", true)
			}else{
				//否则将全选按钮设置为没选中状态
				$("#checkAll").prop("checked", false)
			}
		})*/

		//使用jQuery的on函数：父选择器.on("事件类型", 子选择器, function(){})。这种可以给固有元素添加事件，也可以给动态元素添加事件。
		$("#tBody").on("click", $("input[type='checkbox']"), function () {
			//获取列表中所有checkbox的个数
			let allCheckBoxSize = $("#tBody input[type='checkbox']").size();
			//获取列表中所有被选中checkbox的个数
			let allCheckBoxCheckedSize =  $("#tBody input[type='checkbox']:checked").size();
			//当所有checkbox的个数 等于 所有被选中checkbox的个数时，表示所有的checkbox都被选中了，全选按钮为选中状态
			if(allCheckBoxSize == allCheckBoxCheckedSize){
				//将全选按钮设置为选中状态
				$("#checkAll").prop("checked", true)
			}else{
				//否则将全选按钮设置为没选中状态
				$("#checkAll").prop("checked", false)
			}
		})

		//给删除按钮添加事件
		$("#deleteBtn").click(function () {
			//判断是否选中数据
			//如果列表所有被选中的单选框的长度为0的话，代表没有数据被选中
			if($("tBody input[type='checkbox']:checked").size() == 0){
				//没有数据被选中弹出提示
				alert("亲，你还没有选中要删除的数据哦!");
				return;
			}
			//获取所有被选中的单选框，形成一个单选框数组
			let checkedBox = $("tBody input[type='checkbox']:checked");
			//创建一个string
			let ids = "";
			//遍历单选框数组，取出其中的id
			$.each(checkedBox, function (index, obj) {
				ids += "id=" + obj.value + "&";
			})
			ids = ids.substr(0,ids.length-1)
			alert(ids)

			//有元素被选中，发异步请求
			$.ajax({
				url: "workbench/activity/deleteActivity",
				data: ids,
				type: "post",
				//删除成功，重新获取数据，刷新页面
				success: function (data){
					alert("返回到前端的数据：" + data);
					if(data.code == "1"){
						selectActivityByConditionForPage(1,$("#pagination_Div").bs_pagination("getOption", "rowsPerPage"));
					}else{
						alert(data.message);
					}
				}
			})
		})

		//修改市场活动
		//1. 点击修改按钮，触发事件
		$("#modifyActivityBtn").click(function () {
			//判断是否选择有修改的数据
			let checkedBox = $("tBody input[type = 'checkbox']:checked");
			if(checkedBox.size() == 0){
				alert("亲，你还没有选中要修改的数据哦!");
				return;
			}else if(checkedBox.size() > 1) {
				alert("亲，一次只能选中一条要修改的数据哦!");
				return;
			}

			//根据id查询发送异步请求，将查询出来的数据展示到修改表单
			let id = checkedBox[0].value;
			$.ajax({
				url: "workbench/activity/selectActivityById",
				data: {
					id: id,
				},
				type: "post",
				success: function (data) {
					$("#edit-marketActivityOwner").val(data.owner)
					$("#edit-marketActivityName").val(data.name);
					$("#edit-startTime").val(data.start_date);
					$("#edit-endTime").val(data.end_date);
					$("#edit-cost").val(data.cost);
					$("#edit-describe").html(data.description);
				}
			})

			//弹出模态窗口
			$("#editActivityModal").modal("show");
		})
		//2. 点击更新按钮，触发事件
		$("#updateActivityBtn").click(function (){
			//收集更新后的数据
			let checkedBoxId = $("tBody input[type = 'checkbox']:checked")[0].value;
			let owner = $("#edit-marketActivityOwner").val();
			let name = $.trim($("#edit-marketActivityName").val());
			let start_date = $("#edit-startTime").val();
			let end_date = $("#edit-endTime").val();
			let cost = $.trim($("#edit-cost").val());
			let description = $.trim($("#edit-describe").val());

			//表单数据校验
			let returnInt = formDataVerification(owner, name, start_date, end_date, cost);

			//数据校验成功，发送异步请求
			if(returnInt == 1){
				$.ajax({
					url: "workbench/activity/updateActivity",
					data: {
						//此处返回后台的字符名必须与Activity的属性名一致，否则后台不能接收到数据
						id: checkedBoxId,
						owner: owner,
						name: name,
						start_date: start_date,
						end_date: end_date,
						cost: cost,
						description: description
					},
					type: "post",
					success: function (data){
						if(data.code == "1"){
							//关闭模态窗口
							$("#createActivityModal").modal("hide");

							//刷新市场活动列，显示第一页数据，保持每页显示条数不变
							selectActivityByConditionForPage(1,$("#pagination_Div").bs_pagination("getOption", "rowsPerPage"));
						}else{
							alert(data.message);
						}
					}
				})
			}
		})

		//批量导出市场活动
		$("#exportActivityAllBtn").click(function () {
			//文件的下载只能发送同步请求
			window.location.href = "workbench/activity/exportFindAllActivity";
		})

	});

	//封装函数
	//表单数据校验函数
	function formDataVerification(owner, name, start_date, end_date, cost){
		//判断创建市场的表单中的所有者和名称是否填写
		if(owner == ""){
			alert("所有者不能为空")
			return 0;
		}
		if(name == ""){
			alert("名称不能为空")
			return 0;
		}
		if(start_date != "" && end_date != ""){
			//使用字符串的大小代替日期的大小
			if(end_date < start_date){
				alert("结束日期必须大于开始日期");
				return 0;
			}
		}
		//使用正则表达式验证cost是否是非负整数
		/*语法通则：
            1. //：在js中定义一个正则表达式。
            2. ^：匹配字符串的开头位置
               $: 匹配字符串的结尾
            3. []：匹配指定字符集中的一位字符
        */
		let regExp = /^(([1-9]\d*)|0)$/;
		if(!regExp.test(cost)){
			alert("成本只能是非负整数");
			return 0;
		}
		return 1;
	}
	//分页函数
	function selectActivityByConditionForPage(pageNo, pageSize) {
		//收集参数
		let name = $("#query_name").val();
		let owner = $("#query_owner").val();
		let start_date = $("#query_startTime").val();
		let end_date = $("#query_endTime").val();
		// let pageNo = 1;			//分页查询，要查询的页数
		// let pageSize = 10;	 	//分页查询，每页查询的条数

		//发送异步请求
		$.ajax({
			url: "workbench/activity/selectActivityByConditionForPage",
			data: {
				name: name,
				owner: owner,
				start_date: start_date,
				end_date : end_date,
				pageNo : pageNo,
				pageSize : pageSize
			},
			type: "post",
			success: function (data){
				//分页插件的开始函数
				$("#pagination_Div").bs_pagination({
					currentPage: pageNo,	//当前页号
					rowsPerPage: pageSize,	//每页显示的条数
					totalPages: Math.ceil(data.conditionTotalRows/pageSize),	//总页数,必填参数。向上取整
					totalRows: data.conditionTotalRows,		//总条数

					visiblePageLinks: 5,	//最多可以显示的卡片数

					showGoToPage: true,		//是否显示“跳转到”部分
					showRowsPerPage: true,	//是否显示“记录每页显示条数”部分
					showRowsInfo: true,	//是否显示记录的信息

					//当页号，每页显示条数发生改变就会调用该方法
					onChangePage: function (event, pageObj){
						selectActivityByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
					}
				})

				//将后台查询到的数据回显到jsp页面中
				// $("#totalRows").text("共" + data.conditionTotalRows + "条记录");
				let htmlStr = "";
				$.each(data.queryActivityList, function (index, obj) {
					htmlStr+='<tr class="active">'
					htmlStr+='	<td><input type="checkbox" value='+ obj.id +'></td>'
					htmlStr+='	<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'detail.html\';">'+ obj.name +'</a></td>'
					htmlStr+='	<td>'+ obj.owner +'</td>'
					htmlStr+='	<td>'+ obj.start_date+'</td>'
					htmlStr+='	<td>'+ obj.end_date +'</td>'
					htmlStr+='</tr>'
				})
				$("#tBody").html(htmlStr);

				//每次页面发送异步请请都将全选框设置为不选中状态
				$("#checkAll").prop("checked", false)
			}
		})
	}

</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">

					<form id="createActivityForm" class="form-horizontal" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
									<c:forEach items="${requestScope.Users}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control dateTime" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control dateTime" id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" id="saveActivityBtn" class="btn btn-primary">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form">

						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<c:forEach items="${requestScope.Users}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control dateTime" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control dateTime" id="edit-endTime" value="2020-10-20">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateActivityBtn" data-dismiss="modal">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>


	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query_name">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query_owner">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="query_startTime" />
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="query_endTime">
				    </div>
				  </div>

				  <button type="button" id="selectConditionBtn" class="btn btn-default">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn" data-target="#createActivityModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="modifyActivityBtn" data-target="#editActivityModal"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
<%--						<c:forEach items="${Activities}" var="activity">--%>
<%--							<tr class="active">--%>
<%--								<td><input type="checkbox" value="${activity.id}"/></td>--%>
<%--								<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>--%>
<%--								&lt;%&ndash;所有者，此时tbl_activity表中存储的是所有者的id&ndash;%&gt;--%>
<%--								<td>${activity.owner}</td>--%>
<%--								<td>${activity.start_date}</td>--%>
<%--								<td>${activity.end_date}</td>--%>
<%--							</tr>--%>
<%--						</c:forEach>--%>
					</tbody>
				</table>
			</div>


			<div id="pagination_Div" style="margin-top: 20px">
<%--			<div style="height: 50px; position: relative;top: 30px;">--%>
<%--				<div>--%>
<%--					<button type="button" id="totalRows" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>--%>
<%--				</div>--%>
<%--				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
<%--					<div class="btn-group">--%>
<%--						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
<%--							10--%>
<%--							<span class="caret"></span>--%>
<%--						</button>--%>
<%--						<ul class="dropdown-menu" role="menu">--%>
<%--							<li><a href="#">20</a></li>--%>
<%--							<li><a href="#">30</a></li>--%>
<%--						</ul>--%>
<%--					</div>--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
<%--				</div>--%>
<%--				<div style="position: relative;top: -88px; left: 285px;">--%>
<%--					<nav>--%>
<%--						<ul class="pagination">--%>
<%--							<li class="disabled"><a href="#">首页</a></li>--%>
<%--							<li class="disabled"><a href="#">上一页</a></li>--%>
<%--							<li class="active"><a href="#">1</a></li>--%>
<%--							<li><a href="#">2</a></li>--%>
<%--							<li><a href="#">3</a></li>--%>
<%--							<li><a href="#">4</a></li>--%>
<%--							<li><a href="#">5</a></li>--%>
<%--							<li><a href="#">下一页</a></li>--%>
<%--							<li class="disabled"><a href="#">末页</a></li>--%>
<%--						</ul>--%>
<%--					</nav>--%>
<%--				</div>--%>
			</div>

		</div>

	</div>
</body>
</html>
