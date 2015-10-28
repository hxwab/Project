/**
 * @author liujia
 * @date 2015/10/20
 * @descrioption 初评列表模块
 */
define(function(require, exports, module){
	var list = require("list");
	var nameSpace = "firstReview";
	require("form");
	//初始化函数，初始化列表
	exports.init = function(){
		list.pageShow({
			"nameSpace":nameSpace,
			"sortColumnId":["sortcolumn0","sortcolumn1","sortcolumn2"],
			"sortColumnValue":{"sortcolumn0":0,"sortcolumn1":1,"sortcolumn2":2}
		});
	};

	$("body").on("click", ".first-review", function(){
		var selected = $("#list_container input[name='entityId']:checked");
		if(!selected.length){
			alert("请选择！");
			return ;
		}
		var entityId = selected.val();
		var type = selected.attr("data");
		window.location.href = "jsp/product/firstReview/review.jsp?entityId" + entityId + "&type=" + type;
		return ;
	})
});

