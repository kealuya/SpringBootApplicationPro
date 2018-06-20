package cn.com.szht.service;

import java.util.List;

import cn.com.szht.persistence.entity.EMPLOYEE_INFO;
import cn.com.szht.persistence.entity.ROLE_POWER;
import cn.com.szht.persistence.entity.USER_ROLE;


public interface UserManagerService {
    
    /**
     * 获得用户中文名称
     */
    public String getUserChineseName(String userid);
    /**
     * 获得用户
     */
    public EMPLOYEE_INFO getUser(String userid);
     /**
      * 查询角色信息
      */
     public List<USER_ROLE> findRoleByUserid(String userid);
     /**
      * 查询权限信息
      */
     public List<ROLE_POWER> findPowerByRoleid(String roleid);

     /**
      * 查询用户信息
      */
     public EMPLOYEE_INFO findUserInfoById(String userid); 
     
}
