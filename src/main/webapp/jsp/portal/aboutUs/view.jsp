<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page isELIgnored ="true"%><%--添加对EL表达式的支持--%>
<%@ taglib prefix="s" uri="/struts-tags"%> <%--添加对struts标签的支持 --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>湖北省社会科学优秀成果奖申报评审系统</title>
        <%@ include file="/jsp/base.jsp"%>
    </head>
    <body>
    	<%@ include file="/jsp/innerNav.jsp"%>
    	<sec:authorize ifAnyGranted="ROLE_USER">
  
    	</sec:authorize>
			
		<div class="row">
			<ol class="breadcrumb mybreadcrumb">当前位置：
				<li class="active">关于我们</li>
			</ol>
		</div>
		<div class="row mySlide">
			<div class="col-xs-3 sidebar1 view-nav-bar no-padding-right">
				<div><a href="portal/news/toList.action?type=news&update=1"><i class="fa fa-newspaper-o fa-2"></i>&nbsp;社科动态&nbsp;>></a></div>
				<div><a href="portal/news/toList.action?type=notice&update=1"><i class="fa fa-book fa-2"></i>&nbsp;通知公告&nbsp;>></a></div>
				<div><a href="portal/news/toList.action?type=status&update=1"><i class="fa fa-file-text fa-2"></i>&nbsp;政策文件&nbsp;>></a></div>
				<div><a href="portal/news/toList.action?type=rules&update=1"><i class="fa fa-question-circle fa-2"></i>&nbsp;注意事项&nbsp;>></a></div>
			</div>
			
			<div class="col-xs-9 sidebar2 no-padding-left aboutUs">
				<img alt="photo" style="height:200px;width:780px;" src="assets/images/photo.png">
				<p>湖北省社会科学界联合会（以下简称省社科联）是中共湖北省委、湖北省人民政府领导下的学术性群众团体，是党和政府联系和团结广大哲学社会科学工作者的桥梁和纽带。</p>
				<h3 class="title-h3">机构名称</h3>
				<p>湖北省社会科学界联合会</p>
				<h3 class="title-h3">基本信息</h3>
				<p>湖北省社会科学界联合会（以下简称省社科联）是中共湖北省委、湖北省人民政府领导下的学术性群众团体，是党和政府联系和团结广大哲学社会科学工作者的桥梁和纽带。1958年1月经湖北省人民政府批准筹建，1959年9月举行了湖北省社会科学界第一次代表大会暨湖北省哲学社会科学联合会成立大会，中共“一大”代表、著名马克思主义理论家、教育家李达当选为第一届省社科联主席。
				从那时至今，湖北省社科联已经走过了51年光辉历程。其间几经坎坷，特别是“十年动乱”，社会科学事业遭到空前的劫难，湖北省哲学社会科学联合会停止了活动。
				党的十一届三中全会春风化雨，祖国迎来了春天。1979年经省委批准，恢复省社科联机构，2002年更名为湖北省社会科学界联合会，湖北省的社会科学事业焕发勃勃生机，得到长足发展。
				截止目前为止，湖北省省级哲学社会科学学术社团已经发展到160余家，民办社科研究机构9个，覆盖了文史哲政经等各学科领域，学科门类齐全，结构合理，整体实力进一步增强。其中辛亥革命史研究、荆楚文化研究、武当文化研究和三峡文化研究是我省的特色和优长学科，其学术研究在国内乃至世界上都处于领先水平。湖北省14个市州成立了社科联，全省社科类社团会员逾30万人。</p>
			</div>
		</div>
		<div class="row">
		<%@ include file="/jsp/footer.jsp"%>
		<!-- <script src="js/main.js" type="text/javascript"></script> -->
	</body>
</html>