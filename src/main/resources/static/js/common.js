/**
 * 封装常用js方法
 * 
 * @author WJK
 * @version:1.1 2014-12
 */
// 屏蔽鼠标右键
document.oncontextmenu = function() {
	return false;
};

/** 处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外 */
function banBackSpace(e) {
	var ev = e || window.event;// 获取event对象
	var obj = ev.target || ev.srcElement;// 获取事件源
	var t = obj.type || obj.getAttribute('type');// 获取事件源类型
	// 获取作为判断条件的事件类型
	var vReadOnly = obj.readOnly || obj.getAttribute('readOnly');
	var vEnabled = obj.enabled || obj.getAttribute('enabled');
	// 处理null值情况
	vReadOnly = (vReadOnly == null) ? false : vReadOnly;
	vEnabled = (vEnabled == null) ? true : vEnabled;
	// 当敲Backspace键时，事件源类型为密码或单行、多行文本的，
	// 并且readonly属性为true或readonly为readonly(IE10)或enabled属性为false的，则退格键失效
	var flag1 = (ev.keyCode == 8
			&& (t == "password" || t == "text" || t == "textarea") && (vReadOnly == true
			|| vReadOnly == 'readonly' || vEnabled != true)) ? true : false;

	// 当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
	var flag2 = (ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea") ? true
			: false;

	// 判断
	if (flag2) {
		return false;
	}
	if (flag1) {
		return false;
	}
}

// 禁止后退键 作用于Firefox、Opera
document.onkeypress = banBackSpace;
// 禁止后退键 作用于IE、Chrome
document.onkeydown = banBackSpace;

/** 打开一个最大化的窗口并关闭当前窗口 */
function openMaxWindow(url) {
	// 注意IE7以后的浏览器需要把站点设置为可信任才能隐藏地址栏
	windowHandle = window
			.open(
					url,
					'',
					'resizable=yes,menubar=no,status=yes,toolbar=no,scrollbars=yes,location=no,directories=0');
	windowHandle.moveTo(0, 0);
	windowHandle.resizeTo(screen.availWidth, screen.availHeight);

	window.opener = null;
	window.open('', '_self');
	self.close();
}

/** json对象复制 */
function cloneJson(jsonObj) {
	var buf;
	if (jsonObj instanceof Array) {
		buf = [];
		var i = jsonObj.length;
		while (i--) {
			buf[i] = clone(jsonObj[i]);
		}
		return buf;
	} else if (jsonObj instanceof Object) {
		buf = {};
		for ( var k in jsonObj) {
			buf[k] = clone(jsonObj[k]);
		}
		return buf;
	} else {
		return jsonObj;
	}
}
/** js对象复制 */
function cloneObject(srcObj, destObj) {
	for ( var property in srcObj) {
		var copy = srcObj[property];
		if (destObj == copy)
			continue;
		if (typeof copy == "object") {
			destObj[property] = cloneObject(destObj[property] || {}, copy);
		} else {
			destObj[property] = copy;
		}
	}
	return destObj;
}
// cookie
/** 创建cookie */
function createCookie(name, value, days) {
	var expires;
	if (days) {
		var date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
		expires = "; expires=" + date.toGMTString();
	} else
		expires = "";
	document.cookie = name + "=" + value + expires + "; path=/";
}
/** 读取cookie */
function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ')
			c = c.substring(1, c.length);
		if (c.indexOf(nameEQ) == 0)
			return c.substring(nameEQ.length, c.length);
	}
	return null;
}
/** 删除cookie */
function eraseCookie(name) {
	createCookie(name, "", -1);
}

/** 判断字符串是否为空 */
function isEmpty(str) {
	if (typeof str === "boolean") {
		return false;
	}
	if (str == "null" || !str) {
		return true;
	}
	if (typeof str === "number") {
		return false;
	}
	if (str.match(/^\s*$/) || str.length == 0) {
		return true;
	}
	return false;
}

function isNotEmpty(str) {
	if (typeof str === "boolean") {
		return true;
	}
	if (!str) {
		return false;
	}
	if (typeof str === "number") {
		return true;
	}
	if (str.match(/^\s*$/) || str.length == 0) {
		return false;
	}
	return true;
}

// 用法:openBlank('/member/succeed.html',{id:'6',describe:'添加控制器,
// 包括前台与后台',money:$('.money:first').text()});
function openBlank(action, data, n) {
	var form = $("<form/>").attr('action', action).attr('method', 'post');
	if (n)
		form.attr('target', '_blank');
	var input = '';
	$.each(data, function(i, n) {
		input += '<input type="hidden" name="' + i + '" value="' + n + '" />';
	});
	form.append(input).appendTo("body").css('display', 'none').submit();
}

/**
 * jquery ajax请求封装
 * 
 * @param opts
 */
function doPost(opts) {
	opts.type = "POST";
	doAjax(opts);
}

function doGet(opts) {
	opts.type = "GET";
	doAjax(opts);
}
/**
 * 
 * @param opts modal:是否加载模态框
 * @returns
 */
function doAjax(opts) {
	// 处理参数，如果参数为null,就剔除
	for ( var key in opts.data) {
		if (opts.data[key] == null) {
			delete opts.data[key];
		}
	}
	if(typeof(opts.modal) == 'undefined'){//默认打开模态框
		opts.modal = true;
	}
	if(opts.modal){
		top.jzts();// ajax请求开启模态框
	}
	opts.successCP = opts.success;
	var success = function(result) {
		if (result.status == 200) {// 成功
			var suc = opts.successCP;
			suc(result.data);
		} else if (result.status == 201 || result.status == 202) {// session失效,没有权限
			showErrorMsg(result.msg);
			window.location.href = "login/login"
		} else {
			console.log(result.msg);
			showErrorMsg(result.msg);
		}
		if(opts.modal){
			top.hangge();// 请求成功关闭模态框
		}
	};
	opts.success = success;
	$.ajax(opts);
}
/**
 * 显示错误信息
 * 
 * @param msg
 */
function showErrorMsg(msg) {
	alert(msg);
}
/**
 * 显示成功信息
 * 
 * @param msg
 */
function showSuccessMsg(msg) {
	alert(msg);
}
/**
 * 显示提示信息
 * 
 * @param msg
 */
function showWarningMsg(msg) {
	alert(msg);
}
function confirmAndPost(opts) {
	var msg = (opts.msg ? opts.msg : "确认提交?");
	opts.method = "POST";
	bootbox.confirm(msg, function(result) {
		if (result) {
			doPost(opts);
		}
	});
}

/** *************确认并发送get请求************ */
function confirmAndGet(opts) {
	var msg = (opts.msg ? opts.msg : "确认提交?");
	opts.method = "GET";
	bootbox.confirm(msg, function(result) {
		if (result) {
			doPost(opts);
		}
	});
}

/** 表单清空 */
function clearForm(id) {
	$(':input', '#' + id).not(':button, :submit, :reset, :hidden').val('')
			.removeAttr('checked').removeAttr('selected');
	/**
	 * 去除chosen框的值
	 */
	$('#' + id + ' .chosen-select').chosen("destroy");
	$('#' + id + ' .chosen-select').val("");
	$('#' + id + ' .chosen-select').chosen({
		allow_single_deselect : true
	});
}

/** 含隐藏域清空 */
function clearFullForm(id) {
	/**
	 * 还原到最初的select
	 */
	$('#' + id + ' .chosen-select').chosen("destroy");
	$(':input', '#' + id).not(':button, :submit, :reset').val('').removeAttr(
			'checked').removeAttr('selected');
	$('#' + id + ' .chosen-select').chosen({
		allow_single_deselect : true
	});
}
/**
 * 字段消息提示
 * 
 * @param $dom
 *            jquery对象
 * @param msg
 *            提示消息
 */
function tip($dom, msg, data) {
	if (!data) {
		$dom.tips({
			side : 1,
			msg : msg,
			bg : '#AE81FF',
			time : 2
		});
	} else {
		$dom.tips(data);
	}
}
/**
 * 检查表单下的不能为空的元素 areaId :表单id
 */
function checkAreaNull(areaId) {
	var inputDoms = $(':input', '#' + areaId);
	for (var i = 0; i < inputDoms.length; i++) {
		var $dom = $(inputDoms[i]);
		if ($dom.attr("required")) {
			if ($dom.attr("required") == "required" && isEmpty($dom.val())) {
				tip($dom, $dom.attr("check-msg"))
				$dom.focus();
				return false;
			}
		}
	}
	var noInputDoms = $("#" + areaId).find("*").not(':input');
	for (var i = 0; i < noInputDoms.length; i++) {
		var $dom = $(noInputDoms[i]);
		if ($dom.attr("required")) {
			if ($dom.attr("required") == "required" && isEmpty($dom.val())) {
				tip($dom, $dom.attr("check-msg"))
				$dom.focus();
				return false;
			}
		}
	}
	return true;
}
/**
 * 区域取值。区域内所有包含name属性的取值。 flag:是否取隐藏值,ture:取，false：不取。默认为true
 */
function getAreaVal(areaId, flag) {
	if (isEmpty(areaId)) {
		showErrorMsg("getAreaVal需要dom id！");
		return;
	}
	var doms;
	if (flag == false) {
		doms = $("#" + areaId).find("*").not(':hidden');
	} else {
		doms = $("#" + areaId).find("*");
	}
	var result = {};
	doms.each(function(index, dom) {
		if (dom.name) {
			if (isNotEmpty(dom.name) && dom.value != null) {
				if (dom.nodeName == "SELECT" || dom.nodeName == "INPUT") {
					result[dom.name] = dom.value;
				} else {
					result[dom.name] = dom.innerHTML;
				}
			}
		}
	});
	return result;
}

/**
 * 区域设值 flag:是否根据name设置，true：是，false：否。默认为true
 */
function setAreaVal(areaId, data, flag) {
	if (isEmpty(areaId) || !data || data == null) {
		showErrorMsg("setAreaVal需要dom id及设值数据！");
		return;
	}
	$(':input', '#' + areaId).each(function(index, dom) {
		for ( var attr in data) {
			if (dom.name == attr) {
				dom.value = data[attr];
			} else if (flag == false) {
				// do nothing
			}
		}
	});
	$("#" + areaId).find("*").not(':input').each(function(index, dom) {
		for ( var attr in data) {
			if (dom.name == attr) {
				dom.innerHTML = data[attr];
			} else if (flag == false) {
				// do nothing
			}
		}
	});
}

function linkHover() {
	$(".linkFont").hover(function() {
		$(this).css("color", "#FF0000");
		$(this).css("font-weight", "bold");
		$(this).css("cursor", "pointer");
	}, function() {
		$(this).css("color", "");
		$(this).css("font-weight", "");
		$(this).css("cursor", "");
	});
}
/**
 * 开始日期: id:结束日期框id isShowClear:清空按钮
 */
function startDateFocus(id, isShowClear) {
	WdatePicker({
		isShowClear : isShowClear == true ? isShowClear : false,
		readOnly : true,
		maxDate : "#F{$dp.$D('" + id + "')}"
	});
}

/**
 * 结束日期: id:开始日期框id isShowClear:清空按钮
 */
function endDateFocus(id, isShowClear) {
	WdatePicker({
		isShowClear : isShowClear == true ? isShowClear : false,
		readOnly : true,
		minDate : "#F{$dp.$D('" + id + "')}"
	});
}

/**
 * 设置开始时间与结束时间的间隔不超过指定的天数 id:结束日期框id interval:间隔天数 isShowClear:清空按钮
 */
function startDateIntervalFocus(id, interval, isShowClear) {
	WdatePicker({
		isShowClear : isShowClear == true ? isShowClear : false,
		readOnly : true,
		maxDate : "#F{$dp.$D('" + id + "')}",
		minDate : "#F{$dp.$D('" + id + "',{d:" + interval + "})}"
	});
}

/**
 * 显示诚信度对应的图片 honestyStatus 诚信度
 */
function showHonesty(honestyStatus) {
	var content = "";
	var top = 5;// 最高诚信度
	for (var i = 0; i < honestyStatus; i++) {
		content += "<img style='width:12px;heiht:12px;' src='resources/images/button/star.png'/>";
	}
	for (var j = 0; j < top - honestyStatus; j++) {
		content += "<img style='width:12px;heiht:12px;' src='resources/images/button/star_blank.png'/>";
	}
	return content;
}

/**
 * 上传文件校验
 * 
 * @param id
 *            上传组件的id
 * @param fileType
 *            上传类型包括两种：'pic'、'video'分别表示上传图片和视频
 * @returns 通过校验返回true，反之false
 */
function validateFile(id, fileType) {
	var pattern;// 上传限制
	var msg;// 上传限制信息
	var fileSize;// 文件大小
	var fileName;// 文件名称
	var limitSize;// 文件上传大小限制,默认图片限制为5M，视频限制为100M

	if (fileType == "pic") {
		pattern = /(\.*.jpg$)|(\.*.png$)|(\.*.jpeg$)|(\.*.gif$)|(\.*.bmp$)/;
		msg = "系统仅支持上传<br/>jpg/jpeg/png/gif/bmp格式的图片！";
		limitSize = 5;
	} else if (fileType == "video") {
		pattern = /(\.*.rm$)|(\.*.rmvb$)|(\.*.mpg$)|(\.*.mp4$)|(\.*.flv$)/;
		msg = "系统仅支持上传<br/>rm/rmvb/mpg/mp4/flv格式的视频！";
		limitSize = 100;
	}

	if (!!window.ActiveXObject || 'ActiveXObject' in window) {// IE浏览器
		var fileobject = new ActiveXObject("Scripting.FileSystemObject");
		var file = fileobject.GetFile($("#" + id + "").val());
		fileName = file.ShortName.toLowerCase();
		fileSize = file.Size;
	} else if (isFirefox = navigator.userAgent.indexOf("Firefox") > 0) {// Firefox
		var file = $("#" + id)[0];
		fileName = file.value.toLowerCase();
		fileSize = file.files[0].size;
	} else {
		showErrorMsg("请使用IE或Firefox浏览器进行上传");
		return false;
	}

	// 判断是否为可上传的文件类型
	if (!pattern.test(fileName)) {
		showErrorMsg(msg);
		return false;
	}

	// 判断上传文件的大小
	if (fileSize > limitSize * 1024 * 1024) {
		showErrorMsg("上传文件不能大于" + limitSize + "M");
		return false;
	}

	return true;
}
/**
 * 判断是否为json对象
 * 
 * @param obj
 * @returns {Boolean}
 */
function isJson(obj) {
	var isjson = (typeof obj == "object");
	return isjson;
}
String.prototype.endWith = function(str) {
	var reg = new RegExp(str + "$");
	return reg.test(this);
}
/**
 * 以特殊符号分割array
 * 
 */
Array.prototype.split = function(seperator) {
	var str = "";
	for (var i = 0; i < this.length; i++) {
		str += this[i] + ",";
	}
	return str.substr(0, str.length - 1);
}