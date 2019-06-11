<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>图形界面</title>
<base href="<%=path%>">
<script type="text/javascript" src="uiplugs/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="uiplugs/echarts3/echarts.min.js"></script>
</head>
<body>
	<h4>echarts的柱状图案例</h4>
	<hr>
	<div id="main" style="width: 600px;height:400px;"></div>
	<script type="text/javascript">
		//加载echarts柱状图
		function loadEcharts(elemId,xAxisData,yAxisData){
			var myChart = echarts.init(document.getElementById(elemId));
			var option = {
				    title: {
				        text: 'ECharts 入门示例'
				    },
				    animation:false,
				    legend: {
				        data:['销量']
				    },
				    xAxis: {
				        data: xAxisData,
				        axisLabel:{
				        	fontWeight:'bold',
				        	fontFamily:'Microsoft YaHei',
				        	fontSize:'16'
				        }
				    },
				    yAxis: {},
				    series: [{
				        name: '销量',
				        type: 'bar',
				        data: yAxisData
				    }]
			};
			// 使用刚指定的配置项和数据显示图表。
			myChart.setOption(option);
		}
		
        
		$(function(){
		    var xAxisData = ${xAxisData};
		    var yAxisData = ${yAxisData};
		    loadEcharts("main",xAxisData,yAxisData);
		});
	</script>
</body>
</html>