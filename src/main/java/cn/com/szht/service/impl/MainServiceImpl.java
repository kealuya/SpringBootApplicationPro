package cn.com.szht.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.szht.persistence.dao.MYTESTMapper;
import cn.com.szht.persistence.entity.MYTEST;
import cn.com.szht.service.MainService;
@Service
public class MainServiceImpl implements MainService {

	@Resource
	private MYTESTMapper mytest_mapper;
	@Override
	public String getServiceMsg() {
		return "MainServiceImpl get ServiceMsg ";
	}

	@Override
	public String getServiceMsgFromDB() {
		MYTEST mytest = mytest_mapper.selectByPrimaryKey(112);
		return String.valueOf(mytest.getMy2());
	}

	
	
	
	
}
