//获取系统当前时间
function getTime(key_id) {
	//获取系统时间。  
	var d=new Date(); 
	var YY=d.getFullYear();
	var MM=d.getMonth()+1;
	var DD=d.getDate();
	var hh=d.getHours();  
	var mm=d.getMinutes();  
	var ss=d.getSeconds();  
	//将时间显示，时间格式形如：2017-03-15 15:16:10  
	$("#"+key_id).val(YY+"-"+(MM<10?'0':'')+MM+"-"+(DD<10?'0':'')+DD+" "+(hh<10?'0':'')+hh+":"+(mm<10?'0':'')+mm+":"+ (ss<10?'0':'')+ss);
	
};


//获取系统年月日
function getTimeYMD(key_id) {
	//获取系统时间。  
	var d=new Date(); 
	var YY=d.getFullYear();
	var MM=d.getMonth()+1;
	var DD=d.getDate();
	var hh=d.getHours();  
	var mm=d.getMinutes();  
	var ss=d.getSeconds();  
	//将时间显示，时间格式形如：2017-03-15 15:16:10  
	$("#"+key_id).val(YY+(MM<10?'0':'')+MM+(DD<10?'0':'')+DD);
	
};