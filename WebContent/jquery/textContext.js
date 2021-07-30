//跳到列表
function openText(id){
	var iMask = $("<div></div>").addClass("iMask");
	iMask.css("width",$(window).width() );
	iMask.css("height",$(window).height() );
	iMask.appendTo("body");
	var iBox = $("<div></div>").addClass("iBox");
	iBox.appendTo("body");
	var iTextarea = $('<textarea rows="3" cols="20"></textarea>').addClass("iText");
	iTextarea.appendTo(iBox);
	var iBtn = $("<button type='button'>确定插入</button>").addClass("iBtn");
	iBtn.appendTo(iBox);
	iTextarea.val($("#"+id).val());
	var iSource = $("<button type='button'>原数据</button>").addClass("iSource");
	iSource.appendTo(iBox);
	
	var iClose = $("<button type='button'>关闭</button>").addClass("close1");
	iClose.appendTo(iBox);
	
	iSource.click(function(){
		iTextarea.val($("#"+id).val());
	});
	iBtn.click(function(){
		$("#"+id).val( iTextarea.val() );
		iBox.remove();
		iMask.remove();
	});
	
	iClose.click(function(){
		iBox.remove();
		iMask.remove();
	});
}

function openTextRead(id){
	//alert($("#"+id).text());
	var iMask = $("<div></div>").addClass("iMask");
	iMask.css("width",$(window).width() );
	iMask.css("height",$(window).height() );
	iMask.appendTo("#alertBox");
	var iBox = $("<div></div>").addClass("iBox1");
	iBox.appendTo("#alertBox");
	var iTextarea = $('<textarea rows="3" cols="20" style="background-color: #fff;" readonly="readonly"></textarea>').addClass("iText1");
	iTextarea.appendTo(iBox);
	var iBtn = $("<button type='button'>关闭</button>").addClass("iBtn");
	iBtn.appendTo(iBox);
	iTextarea.val($("#"+id).text());
	iBtn.click(function(){
		iBox.remove();
		iMask.remove();
	});
}

