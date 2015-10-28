<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page isELIgnored ="true"%><%--添加对EL表达式的支持--%>
<%@ taglib prefix="s" uri="/struts-tags"%> <%--添加对struts标签的支持 --%>
<!DOCTYPE html>
<html>
    <head>
        <title>湖北省社会科学优秀成果奖申报评审系统</title>
        <%@ include file="/jsp/base.jsp"%>
    </head>
    <body>
   		<%@ include file="/jsp/innerNav.jsp"%>
   		<s:hidden id="entityId" name="entityId" value="%{entityId}" />
			<div class="row mySlide">
			<ol class="breadcrumb mybreadcrumb">当前位置：
			  <li><a href="toIndex.action">首页</a></li>
			  <li><a href="#"></a></li>
			  <li class="active">查看</li>
			</ol>
			    <div class="col-xs-3 sidebar1 view-nav-bar no-padding-right">
		        	<div><a href="portal/news/toList.action?type=news&update=1"><i class="fa fa-newspaper-o fa-2"></i>&nbsp;社科动态&nbsp;>></a></div>
					<div><a href="portal/news/toList.action?type=notice&update=1"><i class="fa fa-book fa-2"></i>&nbsp;通知公告&nbsp;>></a></div>
					<div><a href="portal/news/toList.action?type=status&update=1"><i class="fa fa-file-text fa-2"></i>&nbsp;政策文件&nbsp;>></a></div>
					<div><a href="portal/news/toList.action?type=rules&update=1"><i class="fa fa-question-circle fa-2"></i>&nbsp;注意事项&nbsp;>></a></div>
			    </div>
			    
			    <div class="col-xs-9 sidebar2 no-padding-left">
		            <div class="view-content">
						<div class="col-xs-12 current-position no-padding">
							<div class="btn-group pull-right view-controler" role="group" aria-label="...">
					  			<button type="button" class="btn btn-sm btn-default" id = "view_add">添加</button>
					  			<button type="button" class="btn btn-sm btn-default" id = "view_mod">修改</button>
					  			<button type="button" class="btn btn-sm btn-default" id = "view_del">删除</button>
					  			<button type="button" class="btn btn-sm btn-default" id = "view_prev">上一条</button>
					  			<button type="button" class="btn btn-sm btn-default" id = "view_next">下一条</button>
					  			<button type="button" class="btn btn-sm btn-default" id = "view_back">返回</button>
							</div>
							<span class="clearfix"></span><!-- 重要!! 用于清除按键组浮动 -->
						</div>
						<textarea id = "view_template" style = "display:none"><!-- 前台模版 -->
							<div class="col-xs-12 news-content">
								<div class="col-xs-12 news-content-title">
									${title}
								</div>
								<div class="col-xs-12 news-content-subtitle" >
									<i class="fa fa-cube"></i>&nbsp;来源: <span class="news-source">${source }</span>&nbsp;&nbsp;
									<i class="fa fa-eye"></i>&nbsp;点击次数：<span class="click-times">${viewCount }</span>次&nbsp;&nbsp;
									<i class="fa fa-calendar"></i>&nbsp;发布时间：<span class="release-time">${createDate }</span>&nbsp;&nbsp;
									<i class="fa fa-user"></i>&nbsp;编辑：<span class="editor">${editor}</span>&nbsp;&nbsp;
								</div>
								<div class="col-xs-12 news-content-para" id="itemContainer">
									${content}
								</div>
								<div class="col-xs-12 attachment">
								{if attachment != undefined}
									<i class="fa fa-paperclip fa-2"></i>&nbsp;附件：
										<a href = "portal/download/download.action?entityId=${id }">${attachmentName }</a>
								{/if}
								</div>
							</div>
						</textarea>
						<div id = "view_container" style = "display:none"></div><!-- 模版解析后的容器 -->
					</div>
			    </div>
			</div>
			<div class="row">
   		<%@ include file="/jsp/footer.jsp"%>
		<script>
		    seajs.use("js/portal/news/view.js", function(view) {
		         view.init(); 
		    });
		</script>
	</body>
</html>