package cn.com.szht.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import cn.com.szht.persistence.entity.EMPLOYEE_INFO;
import cn.com.szht.persistence.entity.ROLE_POWER;
import cn.com.szht.persistence.entity.USER_ROLE;
import cn.com.szht.service.UserManagerService;

@Configuration
public class ShiroConfig {
	/**
	 * ShiroFilterFactoryBean 处理拦截资源文件问题。 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
	 * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager Filter Chain定义说明
	 * 1、一个URL可以配置多个Filter，使用逗号分隔 
	 * 2、当设置多个过滤器时，全部验证通过，才视为通过 
	 * 3、部分过滤器可指定参数，如perms，roles
	 */
	@Bean
	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {

		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 拦截器.
		// rest：比如/admins/user/**=rest[user],根据请求的方法，相当于/admins/user/**=perms[user：method]
		// ,其中method为post，get，delete等。
		// port：比如/admins/user/**=port[8081],当请求的url的端口不是8081是跳转到schemal：//serverName：8081?queryString,其中schmal是协议http或https等，serverName是你访问的host,8081是url配置里port的端口，queryString是你访问的url里的？后面的参数。
		// perms：比如/admins/user/**=perms[user：add：*],perms参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，比如/admins/user/**=perms["user：add：*,user：modify：*"]，当有多个参数时必须每个参数都通过才通过，想当于isPermitedAll()方法。
		// roles：比如/admins/user/**=roles[admin],参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，当有多个参数时，比如/admins/user/**=roles["admin,guest"],每个参数通过才算通过，相当于hasAllRoles()方法。//要实现or的效果看http://zgzty.blog.163.com/blog/static/83831226201302983358670/
		// anon：比如/admins/**=anon 没有参数，表示可以匿名使用。
		// authc：比如/admins/user/**=authc表示需要认证才能使用，没有参数
		// authcBasic：比如/admins/user/**=authcBasic没有参数表示httpBasic认证
		// ssl：比如/admins/user/**=ssl没有参数，表示安全的url请求，协议为https
		// user：比如/admins/user/**=user没有参数表示必须存在用户，当登入操作时不做检查

		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/main/logout", "logout");

		filterChainDefinitionMap.put("/main/static/**", "anon");
		filterChainDefinitionMap.put("/main/login", "anon");
		filterChainDefinitionMap.put("/main/index", "anon");

		filterChainDefinitionMap.put("/main/**", "authc");

		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/main/login");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/index");
		// 未授权界面
		shiroFilterFactoryBean.setUnauthorizedUrl("/errorView/403_error.html");// 不生效(详情原因看MyExceptionResolver)
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(myShiroRealm());
		// 注入缓存管理器;
		// 注意:开发时请先关闭，如不关闭热启动会报错
		// securityManager.setCacheManager(ehCacheManager());//这个如果执行多次，也是同样的一个对象;
		// 注入记住我管理器;
		//securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}

	/**
	 * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
	 */
	@Bean
	public SelfAuthorizingRealm myShiroRealm() {
		SelfAuthorizingRealm myShiroRealm = new SelfAuthorizingRealm();
		//是否需要密码由shiro负责加密
		//myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return myShiroRealm;
	}

	/**
	 * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
	 * 所以我们需要修改下doGetAuthenticationInfo中的代码; @return
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashIterations(2);// 散列的次数，比如散列两次，相当于md5(md5(""));
		hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);// 表示是否存储散列后的密码为16进制，需要和生成密码时的一样，默认是base64；
		return hashedCredentialsMatcher;
	}

	/**
	 * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * shiro缓存管理器; 需要注入对应的其它的实体类中： 1、安全管理器：securityManager
	 * 可见securityManager是整个shiro的核心；
	 *
	 * @return
	 */
	@Bean
	public EhCacheManager ehCacheManager() {
		EhCacheManager cacheManager = new EhCacheManager();
		//cacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
		return cacheManager;
	}

	/**
	 * cookie对象;
	 * 
	 * @return
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
		// System.out.println("ShiroConfiguration.rememberMeCookie()");
		// 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		// <!-- 记住我cookie生效时间30天 ,单位秒;-->
		simpleCookie.setMaxAge(259200);
		return simpleCookie;
	}

	/**
	 * cookie管理对象;
	 * 
	 * @return
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		// System.out.println("ShiroConfiguration.rememberMeManager()");
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		return cookieRememberMeManager;
	}

	@Component
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

}
