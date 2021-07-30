//图片上传预览    IE是用了滤镜。
function previewImage(file,preview,previewImg,imghead,pictureId) {
	var MAXWIDTH = 90;
	var MAXHEIGHT = 90;
	var div = document.getElementById(preview);
	if (file.files && file.files[0]) {
		div.innerHTML = '<img id="'+imghead+'" onclick=$("#'+previewImg+'").click()>';
		var img = document.getElementById(imghead);
		img.onload = function() {
			var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
			img.width = rect.width;
			img.height = rect.height;
			img.style.marginTop = '0px';
			img.style.marginLeft = '5px';
			img.style.marginBottom = '3px';
		}
		var reader = new FileReader();
		reader.onload = function(evt) {
			img.src = evt.target.result;
			var _ir=ImageResizer({
                resizeMode:"auto"  
                ,dataSource:img.src
                ,dataSourceType:"base64"  
                ,maxWidth:1200 //允许的最大宽度  
                ,maxHeight:600 //允许的最大高度。  
                ,success:function(resizeImgBase64,canvas){
                    //赋值到隐藏域传给后台
                	$("#"+pictureId).val(resizeImgBase64);
                }  
                ,debug:true  
			});
		}
		reader.readAsDataURL(file.files[0]);
	} else // 兼容IE
	{
		var sFilter = 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
		file.select();
		var src = document.selection.createRange().text;
		div.innerHTML = '<img id='+imghead+'>';
		var img = document.getElementById(imghead);
		img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
		var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
		status = ('rect:' + rect.top + ',' + rect.left + ',' + rect.width + ',' + rect.height);
		div.innerHTML = "<div id=divhead style='width:" + rect.width
				+ "px;height:" + rect.height + "px;margin-top:" + rect.top
				+ "px;" + sFilter + src + "\"'></div>";
	}
}
function clacImgZoomParam(maxWidth, maxHeight, width, height) {
	var param = {
		top : 0,
		left : 0,
		width : width,
		height : height
	};
	if (width > maxWidth || height > maxHeight) {
		rateWidth = width / maxWidth;
		rateHeight = height / maxHeight;

		if (rateWidth > rateHeight) {
			param.width = maxWidth;
			param.height = Math.round(height / rateWidth);
		} else {
			param.width = Math.round(width / rateHeight);
			param.height = maxHeight;
		}
	}
	param.left = Math.round((maxWidth - param.width) / 2);
	param.top = Math.round((maxHeight - param.height) / 2);
	return param;
}