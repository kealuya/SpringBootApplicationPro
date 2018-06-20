package cn.com.szht.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.com.szht.persistence.entity.EMPLOYEE_INFO;
import cn.com.szht.persistence.entity.ROLE_POWER;
import cn.com.szht.persistence.entity.USER_ROLE;
import cn.com.szht.service.UserManagerService;
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserManagerServiceImpl implements UserManagerService {

	@Override
	public String getUserChineseName(String userid) {
		return null;
	}

	@Override
	public EMPLOYEE_INFO getUser(String userid) {
		return null;
	}

	@Override
	public List<USER_ROLE> findRoleByUserid(String userid) {
		List<USER_ROLE> list = new ArrayList<>();
		USER_ROLE ur = new USER_ROLE();
		ur.setRoleid("role1");
		list.add(ur);
		return list;
	}

	@Override
	public List<ROLE_POWER> findPowerByRoleid(String roleid) {

		List<ROLE_POWER> list = new ArrayList<>();
		ROLE_POWER ur = new ROLE_POWER();
		ur.setRoleid("role1");
		ur.setPowerid("power1");
		list.add(ur);
		return list;
	}

	@Override
	public EMPLOYEE_INFO findUserInfoById(String userid) {
		EMPLOYEE_INFO ei = new EMPLOYEE_INFO();
		ei.setUserid("admin");
		ei.setId("004");
		ei.setPassword("admin");
		if (!"admin".equals(userid)) {
			return null;
		}
		return ei;
	}

}
