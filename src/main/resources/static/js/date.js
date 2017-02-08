var now = new Date();                    //当前日期
var nowDayOfWeek = now.getDay();         //今天本周的第几天
var nowDay = now.getDate();              //当前日
var nowMonth = now.getMonth();           //当前月
var nowYear = now.getYear();             //当前年
nowYear += (nowYear < 2000) ? 1900 : 0;  
     
//格式化日期：yyyy-MM-dd
function formatDate(date) {
    var myyear = date.getFullYear();
    var mymonth = date.getMonth()+1;
    var myweekday = date.getDate(); 
    
    if(mymonth < 10){
        mymonth = "0" + mymonth;
    } 
    if(myweekday < 10){
        myweekday = "0" + myweekday;
    }
    return (myyear+"-"+mymonth + "-" + myweekday); 
} 
     
//获得某月的天数
function getMonthDays(myMonth){
    var monthStartDate = new Date(nowYear, myMonth, 1); 
    var monthEndDate = new Date(nowYear, myMonth + 1, 1); 
    var   days   =   (monthEndDate   -   monthStartDate)/(1000   *   60   *   60   *   24); 
    return   days; 
}
     
//获得本季度的开始月份
function getQuarterStartMonth(){
    var quarterStartMonth = 0;
    if(nowMonth<3){
       quarterStartMonth = 0;
    }
    if(2<nowMonth && nowMonth<6){
       quarterStartMonth = 3;
    }
    if(5<nowMonth && nowMonth<9){
       quarterStartMonth = 6;
    }
    if(nowMonth>8){
       quarterStartMonth = 9;
    }
    return quarterStartMonth;
}
     
//获得本周的开始日期
function getWeekStartDate() {
    var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek); 
    return formatDate(weekStartDate);
} 
     
//获得本周的结束日期
function getWeekEndDate() {
    var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek)); 
    return formatDate(weekEndDate);
} 
     
/*
 * interval空为默认获得本月的开始日期
 * 如果设置参数interval则获得与本月间隔指定月的开始日期
 * 例如getMonthStartDate(-1),表示获得上个月的开始日期
 */
function getMonthStartDate(interval){
	if(arguments.length==1){
		var month = nowMonth + interval
	}else{
		var month = nowMonth;
	}
    var monthStartDate = new Date(nowYear, month, 1); 
    return formatDate(monthStartDate);
}
     
/*
 * interval为空默认获得本月的结束日期
 * 如果设置参数interval则获得与本月间隔指定月的结束日期
 * 例如getMonthEndDate(-1),表示获得上个月的结束日期
 */
function getMonthEndDate(interval){
	if(arguments.length==1){
		var month = nowMonth + interval
	}else{
		var month = nowMonth;
	}
    var monthEndDate = new Date(nowYear, month, getMonthDays(month)); 
    return formatDate(monthEndDate);
}
     
//获得本季度的开始日期
function getQuarterStartDate(){
    var quarterStartDate = new Date(nowYear, getQuarterStartMonth(), 1); 
    return formatDate(quarterStartDate);
}
     
//获得本季度的结束日期
function getQuarterEndDate(){
    var quarterEndMonth = getQuarterStartMonth() + 2;
    var quarterStartDate = new Date(nowYear, quarterEndMonth, getMonthDays(quarterEndMonth)); 
    return formatDate(quarterStartDate);
}

/*
 * 比较日期大小
 */  
function compareDate(startDate, endDate) {
    var arys1= new Array();
    var arys2= new Array();
	if(startDate != null && endDate != null) {
	    arys1=startDate.split('-');
		var sdate=new Date(arys1[0],parseInt(arys1[1]-1),arys1[2]);
		arys2=endDate.split('-');
		var edate=new Date(arys2[0],parseInt(arys2[1]-1),arys2[2]);
		if(sdate.getTime() > edate.getTime()) {
		    return -1;
		} else if(sdate.getTime() == edate.getTime()){
		    return 0;
		} else {
			return 1;
		}
    }
}  

/*
 * 判断日期，时间大小
 */
function compareTime(startDate, endDate) {
	if (startDate.length > 0 && endDate.length > 0) {
		var startDateTemp = startDate.split(" ");
		var endDateTemp = endDate.split(" ");

		var arrStartDate = startDateTemp[0].split("-");
		var arrEndDate = endDateTemp[0].split("-");

		var arrStartTime = startDateTemp[1].split(":");
		var arrEndTime = endDateTemp[1].split(":");

		var allStartDate = new Date(arrStartDate[0], arrStartDate[1], arrStartDate[2], arrStartTime[0], arrStartTime[1], arrStartTime[2]);
		var allEndDate = new Date(arrEndDate[0], arrEndDate[1], arrEndDate[2], arrEndTime[0], arrEndTime[1], arrEndTime[2]);

		if (allStartDate.getTime() > allEndDate.getTime()) {
			return -1;
		} else if(allStartDate.getTime() == allEndDate.getTime()){
			return 0;
		} else {
			return 1
		}
	} else {
		return false;
	}
}
/*
 * 判断日期，时间大小
 */
function compareCalendar(startDate, endDate) {
	if (startDate.indexOf(" ") != -1 && endDate.indexOf(" ") != -1) {
		//包含时间，日期  
		return compareTime(startDate, endDate);
	} else {
		//不包含时间，只包含日期  
		return compareDate(startDate, endDate);
	}
}

/*获得当前时间*/
function getCurrentDateTime(){
	var d = new Date();
	var vYear = d.getFullYear();
	var vMon = d.getMonth() + 1;
	var vDay = d.getDate();
	var h = d.getHours();
	var m = d.getMinutes(); 
	var se = d.getSeconds(); 
	var dateTime = vYear+"-"
				   +(vMon<10 ? "0" + vMon : vMon)+"-"
				   +(vDay<10 ? "0"+ vDay : vDay)+" "
				   +(h<10 ? "0"+ h : h)+":"
				   +(m<10 ? "0" + m : m)+":"
				   +(se<10 ? "0" +se : se);
	
	return dateTime;
}
