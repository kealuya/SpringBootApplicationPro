package cn.com.szht.config;

import org.apache.catalina.filters.RemoteIpFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import cn.com.szht.filter.SzhtFilter;
import cn.com.szht.interceptor.CommonInterceptor;

/**
 * MyBatis 配置（将路径下的mapper全部视为mapper映射文件） 也可以每个mapper文件上边追加@Mapper注解来实现
 */
@MapperScan("cn.com.szht.persistence.dao")
@Configuration
public class WebAppConfig extends WebMvcConfigurationSupport {

	// 将所有/static/** 访问都映射到classpath:/static/ 目录下
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}

	// 配置追加intercepter
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CommonInterceptor());
	}

	@Bean
	public RemoteIpFilter remoteIpFilter() {
		return new RemoteIpFilter();
	}

	// 配置追加filter，以及过滤静态uri
	@Bean
	public FilterRegistrationBean<SzhtFilter> testFilterRegistration() {
		// 配置无需过滤的路径或者静态资源，如：css，imgage等
		StringBuffer excludedUriStr = new StringBuffer();
		excludedUriStr.append("/SpringBootDemo/login/*");
		excludedUriStr.append(",");
		excludedUriStr.append("/SpringBootDemo/js/*");

		FilterRegistrationBean<SzhtFilter> registration = new FilterRegistrationBean<SzhtFilter>();
		registration.setFilter(new SzhtFilter());
		registration.addUrlPatterns("/*");
		registration.addInitParameter("excludedUri", excludedUriStr.toString());
		registration.setName("SzhtFilter");
		registration.setOrder(1);
		return registration;
	}

	// 通过fastjson自动json转换
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		// 1. 需要定义一个converter转换消息的对象
		FastJsonHttpMessageConverter fasHttpMessageConverter = new FastJsonHttpMessageConverter();

		// 2. 添加fastjson的配置信息，比如:是否需要格式化返回的json的数据
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);

		// 3. 在converter中添加配置信息
		fasHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
		HttpMessageConverter<?> converter = fasHttpMessageConverter;
		return new HttpMessageConverters(converter);
	}

	/**
	 * 配置JSP视图解析器(如果配置文件失效，就要code配置)
	 * 如果没有配置视图解析器。Spring会使用BeanNameViewResolver，通过查找ID与逻辑视图名称匹配且实现了View接口的beans
	 * 由于默认不支持jsp，所以要自己配置resolvers。
	 * 使用了jsp作为视图层后，原来的resources目录下面的static和templates目录就没有用了。
	 * jsp页面就得挪动到webapp/WEB-INF/jsp目录下面。当boot项目，默认是没有WEB-INF目录的，这个没关系，我们自己创建出来就ok
	 */
	@Bean
	public InternalResourceViewResolver setupViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		/** 设置视图路径的前缀 */
		resolver.setPrefix("/WEB-INF/jsp/");
		/** 设置视图路径的后缀 */
		resolver.setSuffix(".jsp");
		return resolver;
	}
}
