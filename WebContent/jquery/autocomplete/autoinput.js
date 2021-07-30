document.write('<script type="text/javascript" src="jquery/autocomplete/autocomplete.js"></script>');
//keyword: 文本框显示值， valText：放id值，url：返回数据url， id：唯一id键， name： 值
function autoinput(keyword,valText,url) {
	$.post(url, function(data){
		$('#'+keyword).autocomplete(data, {
			max : 12, //列表里的条目数
			minChars : 0, //自动完成激活之前填入的最小字符
			width : 400, //提示的宽度，溢出隐藏
			scrollHeight : 300, //提示的高度，溢出显示滚动条
			matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			autoFill : false, //自动填充
			formatItem : function(row, i, max) {
				return row.name;
			},
			formatResult : function(row) {
				return row.name;
			}
		}).result(function(event, row, formatted) {
			$("#"+valText).val(row.id);
		});
	},"json");
};

//根据id 匹配name
function autoinput2(keyword,valText,url) {
	$.post(url, function(data){
		$('#'+keyword).autocomplete(data, {
			max : 12, //列表里的条目数
			minChars : 0, //自动完成激活之前填入的最小字符
			width : 400, //提示的宽度，溢出隐藏
			scrollHeight : 300, //提示的高度，溢出显示滚动条
			matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			autoFill : false, //自动填充
			formatItem : function(row, i, max) {
				return ""+row.id;
			},
			formatResult : function(row) {
				return ""+row.id;
			}
		}).result(function(event, row, formatted) {
			$("#"+valText).val(row.name);
		});
	},"json");
};

function autoinput3(text_val,url){
	$.post(url, function(data){
		$("#"+text_val).autocomplete(data, {
			max : 12, //列表里的条目数
			minChars : 0, //自动完成激活之前填入的最小字符
			width : 150, //提示的宽度，溢出隐藏
			scrollHeight : 300, //提示的高度，溢出显示滚动条
			matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			autoFill : false, //自动填充
			formatItem : function(row, i, max) {
				return ""+row;
			}
		});
	},"json");
}

function autoinput4(text_val,data){
		$("#"+text_val).autocomplete(data, {
			max : data.length, //列表里的条目数
			minChars : 0, //自动完成激活之前填入的最小字符
			width : 250, //提示的宽度，溢出隐藏
			scrollHeight : 300, //提示的高度，溢出显示滚动条
			matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			autoFill : false, //自动填充
			formatItem : function(row, i, max) {
				return row.toString().trim();
			}
		}).result(function(event, row, formatted) {
		});
	}
function autoinput5(text_val,data){
	$("#"+text_val).autocomplete(data, {
		max : data.length, //列表里的条目数
		minChars : 0, //自动完成激活之前填入的最小字符
		width : 200, //提示的宽度，溢出隐藏
		scrollHeight : 300, //提示的高度，溢出显示滚动条
		matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
		autoFill : false, //自动填充
		formatItem : function(row, i, max) {
			return row.toString().trim();
		}
	}).result(function(event, row, formatted) {
	});
}

//编辑，根据id，获取name的值
function getNameById(url,id,idVal,text,name){
	$.post(url, { id: idVal},function(data){
		$("#"+text).val(data.name);
	});
}



	
