package cn.com.szht.config.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class SzhtFilter implements Filter {
	/**
	 * 不过滤的地址
	 */
	private String[] excludedUris;

	@Override
	public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) srequest;
		String uri = request.getRequestURI();
		
		if (isExcludedUri(uri)) {
			filterChain.doFilter(srequest, sresponse);
		} else {
			// 打印请求Url
			System.out.println("SzhtFilter,url :" + request.getRequestURI());
			filterChain.doFilter(srequest, sresponse);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		/** 加载初始化参数 */
		excludedUris = filterConfig.getInitParameter("excludedUri").split(",");
	}

	@Override
	public void destroy() {
	}

	/**
	 * 判断uri是否过滤
	 * 
	 * @param uri
	 * @return
	 */
	private boolean isExcludedUri(String uri) {
		if (excludedUris == null || excludedUris.length <= 0) {
			return false;
		}
		for (String ex : excludedUris) {
			uri = uri.trim();
			ex = ex.trim();
			if (uri.toLowerCase().matches(ex.toLowerCase().replace("*", ".*")))
				return true;
		}
		return false;
	}

}
