package cn.com.szht.persistence.dao;

import java.util.List;

import cn.com.szht.persistence.entity.CITY;

public interface CITYMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(CITY record);

    int insertSelective(CITY record);

    List<CITY> selectAll();

    int updateByPrimaryKeySelective(CITY record);

    int updateByPrimaryKey(CITY record);
}