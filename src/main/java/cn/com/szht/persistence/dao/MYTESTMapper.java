package cn.com.szht.persistence.dao;

import cn.com.szht.persistence.entity.MYTEST;

public interface MYTESTMapper {
	int deleteByPrimaryKey(Integer my1);

	int insert(MYTEST record);

	int insertSelective(MYTEST record);

	MYTEST selectByPrimaryKey(Integer my1);

	int updateByPrimaryKeySelective(MYTEST record);

	int updateByPrimaryKey(MYTEST record);
}