/*
 * @author:Liujia
 * @description: 角色常看
 * @date:2015/09/01
 */
define(function(require, exports, module){
	require("form");
	var nameSpace = "product";
	var showDetail = function(){
	        $("#view_container").hide();
	        $("#view_container").html(TrimPath.processDOMTemplate("view_template",{}))
	        $("#view_container").show();
	}
	exports.init = function(){
		showDetail();
		$("body").on("click", ".list-group-item", function(){
			$(this).parent().find(".first-review-selected").removeClass("first-review-selected");
			$(this).addClass("first-review-selected");
			$("input", this)[0].checked = true;
		})
	}
});