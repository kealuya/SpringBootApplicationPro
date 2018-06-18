package cn.com.szht.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.com.szht.dto.Info;
import cn.com.szht.service.MainService;

@Controller
@RequestMapping("/main")
public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	@Autowired
	private MainService mainService;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public Info test(String name) {
		Info info = new Info();
		info.setId(Long.valueOf(6666));
		info.setName("info name");
		System.out.println(JSON.toJSONString(info));
		logger.info("这是用logger在程序内打出的log");
		return info;
	}

	@RequestMapping(value = "/msgget")
	public String msgget(Map<String, Object> map) {
		String msg = mainService.getServiceMsg();
		String my2 = mainService.getServiceMsgFromDB();
		map.put("msg", "Hello message" + msg);
		map.put("my2","mytest's my2 == " + my2);
		return  "testHello"  ;
	}

	@RequestMapping(value = "/testerror")
	public String testerror() {
		logger.error("testerror", new RuntimeException("testerror"));
		throw new RuntimeException("testerror");
	}

	

	@RequestMapping(value = "/go")
	public String go() {
		return  "testHello"  ;
	}

}
