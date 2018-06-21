package cn.com.szht.config.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class SzhtExceptionHandler implements HandlerExceptionResolver {
	private static final Logger logger = LoggerFactory.getLogger(SzhtExceptionHandler.class);
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		logger.error("SzhtExceptionHandler", ex);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("ex", ex);
		
		
		if (ex instanceof BusinessException) {
			return new ModelAndView("error/error-business", model);
		} else {
			return new ModelAndView("error/500", model);
		}
	}
}
