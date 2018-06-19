package cn.com.szht.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.com.szht.persistence.dao.CITYMapper;
import cn.com.szht.persistence.dao.MYTESTMapper;
import cn.com.szht.persistence.entity.CITY;
import cn.com.szht.persistence.entity.MYTEST;
import cn.com.szht.service.MainService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class MainServiceImpl implements MainService {

	@Resource
	private MYTESTMapper mytest_mapper;
	@Resource
	private CITYMapper city_mapper;

	@Override
	public String getServiceMsg() {
		return "MainServiceImpl get ServiceMsg ";
	}

	@Override
	public String getServiceMsgFromDB() {

		PageHelper.startPage(2, 10, "ID DESC");
		List<CITY> cityList = city_mapper.selectAll();
		PageInfo<CITY> page = new PageInfo<CITY>(cityList);
		page.getList();

		//MYTEST mytest = mytest_mapper.selectByPrimaryKey(112);

		return "test";
	}

}
