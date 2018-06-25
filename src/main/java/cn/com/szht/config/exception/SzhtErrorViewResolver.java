package cn.com.szht.config.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

@Component
public class SzhtErrorViewResolver implements ErrorViewResolver {

	@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
		String pageError = "";
		switch (status.value()) {
		case 404:
			pageError = "404";
			break;
		case 500:
			pageError = "500";
			break;
		default:
			pageError = "SzhtError";
			break;
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("errorMsg", JSON.toJSONString(model));
		mav.setViewName("error/" + pageError);
		return mav;
	}

}
