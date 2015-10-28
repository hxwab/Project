/*
 * @author fengzheqi
 * @date 2015/08/26
 * @description 奖励管理
 * */

define(function(require, exports, module){
	require('form');
	var list = require('list');
	var nameSpace = "system/dataDic/reward";
	
	exports.init = function() {
		list.pageShow({
			"nameSpace":nameSpace,
			"sortColumnId":["sortcolumn0","sortcolumn1","sortcolumn2","sortcolumn3"],
			"sortColumnValue":{"sortcolumn0":0,"sortcolumn1":1,"sortcolumn2":2,"sortcolumn3":3}
		})
	}
})