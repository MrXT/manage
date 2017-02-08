//$(top.hangge());不开启就不关闭
var firstPage = true;// 解决twbsPagination初始化执行两道
var tableId = "simple-table";// 默认table
var optButtonId = "optButtonId";// 默认操作optButton
var searchForm = "searchForm";// 默认搜索form
var pageId = "page";// 默认分页数据存放id
var opt = {
	visiblePages : 5,
	first : "首页",
	last : "尾页",
	next : "下页",
	prev : "上页",
	onPageClick : function(event, page) {
		if (firstPage) {
			firstPage = false;
			return;
		}
		searchData();
	}
};
/**
 * 根据tableId，optButtonId 获取默认的datagrid
 * 
 * @param idProperty
 * @param loadCheckBox
 */
function getDefalutDatagrid(jsons, idProperty, loadCheckBox) {
	return new Datagrid(jsons, tableId, idProperty, optButtonId, loadCheckBox);
}
/**
 * 设置首页的frame大小
 */
function setFrameHeight() {
	var height = 0;
	if ($("body").height() + 29 < $(window.parent.parent).height()) {
		height = $(window.parent.parent).height() - 45 - 5;
	} else {
		height = $("body").height() + 29;
	}
	$(window.parent.parent.document.getElementById("mainFrame")).height(height);
	$(window.parent.document.getElementById("page")).height(height);
}
$(function() {
	defaultSaveFormId = "saveForm";// 默认保存formId
	defaultQueryUrl = mainUrl + "/query";// 默认查询url
	defaultInsertUrl = mainUrl + "/insert";// 默认新增url
	defaultUpdateUrl = mainUrl + "/update";// 默认修改url
	defaultInvalidUrl = mainUrl + "/invalid";// 默认逻辑删除url
	defaultInvalidBatchUrl = mainUrl + "/invalidBatch";// 默认批量逻辑删除url
	defaultRevalidBatchUrl = mainUrl + "/revalidBatch";// 默认批量恢复url
	defaultRevalidUrl = mainUrl + "/revalid";// 默认恢复url
	defaultSaveFormData = getAreaVal(defaultSaveFormId);
	setFrameHeight();
	window.onresize = function() {
		setFrameHeight();
	}
	// 日期框
	$('.date-picker').datepicker({
		format : "yyyy-mm-dd",
		todayBtn : true,
		clearBtn : true,
		language : "zh-CN",
		autoclose : true,
		todayHighlight : true
	});
	// 下拉框
	if (!ace.vars['touch']) {
		$('.chosen-select').chosen({
			allow_single_deselect : true
		});
		$(".chosen-container").each(function(index, dom) {
			$(dom).width($(dom).prev().css("width"));
		});
	}
});
/**
 * 复选框绑定点击事件
 */
function bindCheck(tableId) {
	// 复选框全选控制
	var active_class = 'active';
	$('#' + tableId + ' > thead > tr > th input[type=checkbox]').eq(0).on(
			'click',
			function() {
				var th_checked = this.checked;// checkbox inside "TH"
				// table header
				$(this).closest('table').find('tbody > tr').each(
						function() {
							var row = this;
							if (th_checked)
								$(row).addClass(active_class).find(
										'input[type=checkbox]').eq(0).prop(
										'checked', true);
							else
								$(row).removeClass(active_class).find(
										'input[type=checkbox]').eq(0).prop(
										'checked', false);
						});
			});
}
/**
 * 默认搜索
 * 
 * @param 参数
 * @param success
 */
function searchDefault(data, success) {
	search(searchForm, pageId, defaultQueryUrl, data, success);
}

/**
 * formId:表单id ，从表单中获取参数. tableId:表格id, pageId:分页数据展示ID url:请求地址, success:成功回调函数
 * data:json参数对象,如果重复，覆盖formId参数
 */
function search(formId, pageId, url, data, success) {
	var param = getAreaVal(formId);
	if (data != null) {
		for ( var key in data) {
			param[key] = data[key];
		}
	}
	param.pageNo = $("#" + pageId + " .pagination").twbsPagination(
			"getCurrentPage");
	param.pageSize = $("#" + pageId + " .pageSize").val();
	doGet({
		url : url,
		data : param,
		success : function(result) {
			var page = param.page;
			if(typeof page == "undefined"){
				page = true;
			}
			$("#" + pageId + " .pageTotal").html(result.pageTotal);
			$("#" + pageId + " .totalCount").html(result.totalCount);
			var totalPages = result.pageTotal == 0 ? 1 : result.pageTotal;
			var currentPage = result.pageNo > totalPages ? totalPages
					: result.pageNo;
			if(page){
				// 动态分页
				$("#" + pageId + " .pagination").remove();
				$("#" + pageId + " .pageBox")
						.html(
								'<ul class="pagination-sm pagination" style="margin:0px;"></ul>');
				firstPage = true;
				$("#" + pageId + " .pagination").twbsPagination($.extend({}, opt, {
					startPage : currentPage,
					totalPages : totalPages
				}));
			}
			success(result);
		}
	});
}
// 新增
function add() {
	if (!checkAreaNull(defaultSaveFormId)) {
		return false;
	}// 检测不能为空的字段
	var data = getAreaVal(defaultSaveFormId);
	if (!beforeAddUpdateParam(data)) {
		return false;
	}// 检测数据
	doPost({
		url : defaultInsertUrl,
		data : data,
		success : function(result) {
			$("#myModal").modal("hide");
			searchData();
		}
	});
}
// 逻辑删除
function invalid(data) {
	confirmAndPost({
		msg : "确定要删除该记录？",
		url : defaultInvalidUrl,
		data : {
			id : data[idProperity]
		},
		success : function(result) {
			searchData();
		}
	});
}
// 恢复
function revalid(data) {
	confirmAndPost({
		msg : "确定要恢复该记录？",
		url : defaultRevalidUrl,
		data : {
			id : data[idProperity]
		},
		success : function(result) {
			searchData();
		}
	});
}
/**
 * 打开保存页面
 * data isJson 表示为修改
 * data boolean false 表示不要清空表单
 */
function savePage(data) {
	clearFullForm(defaultSaveFormId);// 清空表单
	var isLoadDefult = false;
	if(typeof data == "undefined"){
		isLoadDefult = true;
	}else if(typeof data == "boolean"){
		isLoadDefult = data;
	}
	if (isJson(data)) {
		doGet({
			url : defaultQueryUrl,
			// modal : false,//是否显示加载框
			data : {
				id : data[idProperity]
			},
			success : function(result) {
				if (result.list.length > 0) {
					setAreaVal(defaultSaveFormId, result.list[0]);
				} else {
					showWarningMsg("未找到该记录！");
				}
				$("#myModalLabel").html("编辑");
				document.getElementById("saveButton").onclick = function() {
					edit();
				};
				$("#myModal").modal("show");
			}
		});
	} else {
		if(isLoadDefult){
			setAreaVal(defaultSaveFormId,defaultSaveFormData);
		}
		$("#myModalLabel").html("新增");
		document.getElementById("saveButton").onclick = function() {
			add();
		};
		$("#myModal").modal("show");
	}

}
// 修改
function edit() {
	if (!checkAreaNull(defaultSaveFormId)) {
		return false;
	}// 检测表单不能为空的字段
	var data = getAreaVal(defaultSaveFormId);
	if (!beforeEditUpdateParam(data)) {
		return false;
	}// 检测数据
	doPost({
		url : defaultUpdateUrl,
		data : data,
		success : function(result) {
			$("#myModal").modal("hide");
			searchData();
		}
	});
}
// 批量操作
/**
 * paramData: 0:逻辑删除，1：逻辑回复 success:成功回调函数,默认执行searchData();
 */
function makeAll(paramData, success) {
	var msg;
	if (paramData == 0) {
		msg = '确定要删除选中的数据吗?';
	} else {
		msg = '确定要恢复选中的数据吗?';
	}
	bootbox
			.confirm(
					msg,
					function(result) {
						if (result) {
							var str = '';
							for (var i = 0; i < document
									.getElementsByName('ids').length; i++) {
								if (document.getElementsByName('ids')[i].checked) {
									if (str == '')
										str += document
												.getElementsByName('ids')[i].value;
									else
										str += ','
												+ document
														.getElementsByName('ids')[i].value;
								}
							}
							if (str == '') {
								bootbox
										.dialog({
											message : "<span class='bigger-110'>您没有选择任何内容!</span>",
											buttons : {
												"button" : {
													"label" : "确定",
													"className" : "btn-sm btn-success"
												}
											}
										});
								$("#zcheckbox").tips({
									side : 1,
									msg : '点这里全选',
									bg : '#AE81FF',
									time : 8
								});
								return;
							} else {
								doPost({
									url : paramData == 0 ? defaultInvalidBatchUrl
											: defaultRevalidBatchUrl,
									data : {
										ids : str
									},
									success : function(result) {
										if (success) {
											success(result);
										} else {
											searchData();
										}
									}
								});
							}
						}
					});
};
