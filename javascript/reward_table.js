
$(document).ready(function(){
	//aflterLoginCheck();
	getList();
	var now = new Date();
	var month = now.getMonth()+1
	var day = now.getDate()
	var hours = now.getHours(); 
	var minutes = now.getMinutes(); 
	var seconds = now.getSeconds(); 
	if (hours > 12){
		hours -= 12; ampm = "오후 "; 
	}
	else{ ampm = "오전 "; }
	if (hours < 10){ hours = "0" + hours; }
	if (minutes < 10){ minutes = "0" + minutes; } 
	if (seconds < 10){ seconds = "0" + seconds; } 
	$("#loading_date").text("전일 1차 대사 건수는 금일 14시경 반영 예정 ("+ month +"월"+day +"일 "+ampm + hours + ":" + minutes + ":" + seconds+")");

	
	
});

//이벤트 리스트 불러오기
function getList(){
	/*var toDate = $('#toDate').val();
	var serverNo = $("#serverNo option:selected").val();
	var type = "1";
*/
	$.ajax({
		  type:"get"
		, url:"/mobile/emstower/jsp/reward_table.jsp"
		, data:{
			 /*  "toDate": toDate
			   ,"serverNo": serverNo
			   ,"type": type*/
			   }
		, dataType:"JSON"
		, success: function(data){
			console.log(data)
			var makeHtml = "";
			var today = new Date();
			$.each(data, function(i,v){
				makeHtml += "<tr>";
				makeHtml += "	<td>"+v.shop_name+"</td>";
				
				/*makeHtml += "	<td>"+v.day1_order_cnt+"</td>";
				makeHtml += "	<td>"+v.day1_order_chk_cnt+"</td>";
				makeHtml += "	<td>"+v.day1_order_chk_cnt+"</td>";
				*/
				
				
				makeHtml += "	<td>"+commaNum(v.day1_order_cnt)+"</td>";
				makeHtml += "	<td>"+commaNum(v.day1_order_chk_cnt)+"</td>";
				
				if (v.day1_rate_warnning)
					makeHtml += "	<td style='background-color:red;'>"+v.day1_rate+"</td>";
				else 
					makeHtml += "	<td>"+v.day1_rate+"</td>";
				
				
				makeHtml += "	<td>"+commaNum(v.day10_order_cnt)+"</td>";
				makeHtml += "	<td>"+commaNum(v.day10_point_cnt)+"</td>";
				
				if (v.day10_rate_warnning)
					makeHtml += "	<td style='background-color:red;'>"+v.day10_rate+"</td>";
				else 
					makeHtml += "	<td>"+v.day10_rate+"</td>";
				
				makeHtml += "	<td>"+commaNum(v.day_1)+"</td>";
				
				
				if (v.day_1_day_7_warnning)
					makeHtml += "	<td style='background-color:red;'>"+v.day_1_day_7+"</td>";
				else 
					makeHtml += "	<td>"+v.day_1_day_7+"</td>";
				
				
				if (v.day_1_day_30_warnning)
					makeHtml += "	<td style='background-color:red;'>"+v.day_1_day_30+"</td>";
				else 
					makeHtml += "	<td>"+v.day_1_day_30+"</td>";
				
				
				
				makeHtml += "	<td>"+commaNum(v.day_7)+"</td>";
				makeHtml += "	<td>"+commaNum(v.day_30)+"</td>";
				/*
				if (v.recent_rewardcode_warnning){
					makeHtml += "	<td style='background-color:red;'>"+v.recent_rewardcode + " ["+ v.recent_rewardcode.length+"]</td>";
					makeHtml += "	<td style='background-color:red;'>"+v.recent_ok_rewardcode+ " ["+v.recent_ok_rewardcode.length+"]</td>";
				}else{
					makeHtml += "	<td>"+v.recent_rewardcode + " ["+ v.recent_rewardcode.length+"]</td>";
					makeHtml += "	<td>"+v.recent_ok_rewardcode+ " ["+v.recent_ok_rewardcode.length+"]</td>";
				}*/
				
				makeHtml += "</tr>";
			});
			$('#log_list tbody').html(makeHtml);
		}
	  , complete: function(){

	  }
	});
}
function numberWithCommas(x) {
    return x.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}



//콤마
function commaNum(amount) {
    var delimiter = ",";
    var i = parseInt(amount);

    if(isNaN(i)) { return ''; }

    var minus = '';

    if (i < 0) { minus = '-'; }
    i = Math.abs(i);

    var n = new String(i);
    var a = [];

    while(n.length > 3)
    {
        var nn = n.substr(n.length-3);
        a.unshift(nn);
        n = n.substr(0,n.length-3);
    }

    if (n.length > 0) { a.unshift(n); }
    n = a.join(delimiter);
    amount = minus + (n+ "");
    return amount;
}
