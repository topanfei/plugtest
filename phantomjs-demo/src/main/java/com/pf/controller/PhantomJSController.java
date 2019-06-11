package com.pf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/phantomjs")
public class PhantomJSController {

	@RequestMapping(value="index")
	public String index(Model model) {
		//组装echarts柱形图数据
		Object[] xAxisDataArray = {"衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"};
		Object[] yAxisDataArray = {5, 20, 36, 10, 10, 20};
		model.addAttribute("xAxisData", JSON.toJSONString(xAxisDataArray));
		model.addAttribute("yAxisData", JSON.toJSONString(yAxisDataArray));
		return "phantomjs/index";
	}
}
