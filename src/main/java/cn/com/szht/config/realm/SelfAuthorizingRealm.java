package cn.com.szht.config.realm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import cn.com.szht.persistence.entity.EMPLOYEE_INFO;
import cn.com.szht.persistence.entity.ROLE_POWER;
import cn.com.szht.persistence.entity.USER_ROLE;
import cn.com.szht.service.UserManagerService;

public class SelfAuthorizingRealm extends AuthorizingRealm {

	@Resource
	private UserManagerService userManagerService;

	/**
	 * <property name="authenticationQuery" value= "SELECT PASSWORD FROM
	 * NEIKONG_CL_EMPLOYEE_INFO WHERE USERID = ?" > </property>
	 * <property name="userRolesQuery" value= "SELECT ROLEID from
	 * NEIKONG_CL_USER_ROLE left join NEIKONG_CL_ROLE using(ROLEID) left join
	 * NEIKONG_CL_EMPLOYEE_INFO using(USERID) WHERE USERID = ?" ></property>
	 * <property name="permissionsQuery" value= "SELECT POWERID FROM
	 * NEIKONG_CL_ROLE_POWER left join NEIKONG_CL_ROLE using(ROLEID) left join
	 * NEIKONG_CL_POWER using(POWERID) WHERE ROLEID = ?" ></property>
	 */

	/**
	 * 添加角色
	 * 
	 * @param userid
	 * @param info
	 */

	private void addRole(String userid, SimpleAuthorizationInfo info) {
		List<USER_ROLE> roles = (List<USER_ROLE>) userManagerService.findRoleByUserid(userid);
		if (roles != null && roles.size() > 0) {
			for (USER_ROLE role : roles) {
				info.addRole(role.getRoleid());
			}
		}
	}

	/**
	 * 添加权限
	 * 
	 * @param powerid
	 * @param info
	 * @return
	 */
	private SimpleAuthorizationInfo addPermission(String roleid, SimpleAuthorizationInfo info) {

		List<ROLE_POWER> role_Power = (List<ROLE_POWER>) userManagerService.findPowerByRoleid(roleid);
		for (ROLE_POWER rolepower : role_Power) {
			info.addStringPermission(rolepower.getPowerid());// 添加权限
		}
		return info;
	}

	/**
	 * 获取授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 用户名
		String username = (String) principals.fromRealm(getName()).iterator().next();
		// 根据用户名来添加相应的权限和角色
		if (!StringUtils.isEmpty(username)) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			addRole(username, info);
			for (String roleid : info.getRoles()) {
				addPermission(roleid, info);
			}
			return info;
		}
		return null;
	}

	/**
	 * 登录验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		// 令牌——基于用户名和密码的令牌
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		// 令牌中可以取出用户名
		String userid = token.getUsername();
		// 让shiro框架去验证账号密码
		if (!StringUtils.isEmpty(userid)) {
			EMPLOYEE_INFO user = userManagerService.findUserInfoById(userid);
			if (user != null) {
				// return new SimpleAuthenticationInfo( user.getUserid(), user.getPassword(),
				// getName());
				List<Object> principals = new ArrayList<Object>();
				principals.add(user.getUserid());
				principals.add(user);
				SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principals, user.getPassword(),
						this.getName());
				return info;
			}
		}
		return null;

	}
}