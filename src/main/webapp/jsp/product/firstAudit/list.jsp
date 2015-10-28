<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page isELIgnored ="true"%><%--添加对EL表达式的支持--%>
<%@ taglib prefix="s" uri="/struts-tags"%> <%--添加对struts标签的支持 --%>
<!DOCTYPE html>
<html>
	<head>
		<title>湖北省社会科学优秀成果奖励申报评审系统</title>
		<%@ include file="/jsp/base.jsp"%>
	</head>
	<body>
		<!-- 页头 -->
		<%@ include file="/jsp/innerNav.jsp"%>
		<a name="top" id="top"></a>
		
		<!-- 页面主体 -->
		<div class="row mySlide">
			<!-- 当前位置 -->
			<ol class="breadcrumb mybreadcrumb">当前位置：
				<li><a href="#">评审管理</a></li>
				<li class="active">成果列表</li>
			</ol>
			
			<!-- 列表 -->
			<div class="col-xs-12">
				<!-- 初级检索 -->
				<div id="simple_search" style="display: none !important;">
					<form id="search" class="form-inline" name="search" action="product/firstAudit/list.action" method="post">
						<input id="sim_sysOptKeyword" name="sim_sysOptKeyword" type="hidden" value="" >
						<table class="table_td_padding form-group pull-right" border="0" cellspacing="0" cellpadding="0" >
							<tbody>
								<tr>
									<td align="right">
										<span class="choose_bar">
										<select id="search_searchType" class="select form-control input-sm" name="searchType">
											<option value="-1" selected="selected">--不限--</option>
											<option value="0">成果名称</option>
											<option value="1">负责人</option>
											<option value="2">机构</option>
											<option value="3">提交状态</option>
											<option value="4">审核状态</option>
											<option value="5">警告</option>
										</select>
										</span>
										<input id="keyword" class="form-control input-sm keyword" name="keyword" type="text" size="10" value="">
										<input id="list_pagenumber" name="pageNumber" type="hidden" value="">
										<input id="list_sortcolumn" name="sortColumn" type="hidden" value="0">
										<input id="list_pagesize" name="pageSize" type="hidden"  value="0">
									</td>
									<td class = "btn-group" style = "display:block">
										 <input id="list_button_query" class="btn btn-default btn-sm" type="button" value="检索" >
									</td>
								</tr>
							</tbody>
						</table>
						<span class="clearfix"></span>
					</form>
				</div>
					
				<!-- 列表数据模板 -->
				<textarea id="list_template" style="display:none;">
					<table id="list_table" class="table table-striped table-bordered dataTable no-footer" width="100%" border="0" cellspacing="0" cellpadding="0">
						<thead id="list_head">
							<tr class="table_title_tr">
								<td width="15"><input id="check" name="check" type="checkbox" title="点击全选/不选本页所有项目"/></td>
								<td width="40" class="text-center">序号</td>
								<td width="80"><a id="sortcolumn0" href="" class="{if sortColumn == 0}{if sortColumnLabel == 1}up_css{else}down_css{/if}{/if}" title="点击按申报编号排序">申报编号</a></td>
								<td width="200"><a id="sortcolumn1" href="" class="{if sortColumn == 1}{if sortColumnLabel == 1}up_css{else}down_css{/if}{/if}" title="点击按成果名称排序">成果名称</a></td>
								<td width="60"><a id="sortcolumn2" href="" class="{if sortColumn == 2}{if sortColumnLabel == 1}up_css{else}down_css{/if}{/if}"  title="点击按负责人排序">负责人</a></td>
								<td width="100"><a id="sortcolumn3" href="" class="{if sortColumn == 3}{if sortColumnLabel == 1}up_css{else}down_css{/if}{/if}"  title="点击按所属机构排序">所属机构</a></td>
								<td width="100"><a id="sortcolumn4" href="" class="{if sortColumn == 4}{if sortColumnLabel == 1}up_css{else}down_css{/if}{/if}"  title="点击按提交状态排序">研究类型</a></td>
								<td width="100"><a id="sortcolumn5" href="" class="{if sortColumn == 5}{if sortColumnLabel == 1}up_css{else}down_css{/if}{/if}"  title="点击按审核状态排序">审核状态</a></td>
								<td width="100"><a id="sortcolumn6" href="" class="{if sortColumn == 6}{if sortColumnLabel == 1}up_css{else}down_css{/if}{/if}"  title="点击按更新时间排序">更新时间</a></td>
								<td width="100">警告</a></td>
							</tr>
						</thead>
						<tbody>
						{for item in root}
							<tr>	
								<td><input type="checkbox" name="entityIds" value="${item.laData[5]}"/></td>
								<td class="text-center">${item.num}</td>
								<td class="text-center"><a id="${item.laData[0]}" class = "link1" title="点击查看详细信息">${item.laData[1]}</a></td>
								<td class="text-center">${item.laData[2]}</td>
								<td class="text-center">${item.laData[3]}</td>
								<td class="text-center">${item.laData[4]}</td>
								<td class="text-center">${item.laData[5]}</td>
								{if item.laDate[6] == 2}
								<td class="text-center">${item.laData[7]}</td>
								{/if}
								<td>
								<td class="text-center">${item.laData[7]}</td>
								<td class="text-center">${item.laData[7]}</td>
							</tr>
						{forelse}
							<tr>
								<td align="center">暂无符合条件的记录</td>
							</tr>
						{/for}
						</tbody>
					</table>
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_td_padding form-inline">
						<tr class="table_main_tr2">
							<td class = "btn-group">
								<button id = "list_add" class = "btn btn-default btn-sm">添加</button>
								<button id = "list_delete" class = "btn btn-default btn-sm">删除</button>
								<button id = "list_export" class = "btn btn-default btn-sm">导出</button>
								<button id = "btn_audit" class = "btn btn-default btn-sm">审核</button>
							</td>
						</tr>
					</table>
				</textarea>
			
				<!-- 显示列表 -->
				<s:form id="list" theme="simple" action="delete" namespace="/product/application/delete.action">
					<s:hidden id="pagenumber" name="pageNumber"/>
					<s:hidden id="checkedIds" name="checkedIds"/>
					<div id="list_container"  style="display:none;"></div>
				</s:form> 
			</div>
		</div>
		
		<!-- 页脚 -->
		<div class="row">
		<%@ include file="/jsp/footer.jsp"%>
	</body>
	<script>
		seajs.use("js/product/application/list.js", function(list) {
			list.init();
		});
	</script>
</html>