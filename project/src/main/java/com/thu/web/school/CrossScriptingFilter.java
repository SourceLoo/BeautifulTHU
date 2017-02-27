package com.thu.web.school;

import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class WebConfiguration {
	@Bean
	public RemoteIpFilter remoteIpFilter() {
		return new RemoteIpFilter();
	}

	@Bean
	public FilterRegistrationBean testFilterRegistration() {

		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new CrossScriptingFilter());
		registration.addUrlPatterns("/*");
		registration.addInitParameter("paramName", "paramValue");
		registration.setName("MyFilter");
		registration.setOrder(1);
		return registration;
	}

	public class CrossScriptingFilter implements Filter {

		public void destroy() {
			// TODO Auto-generated method stub

		}

		public void doFilter(ServletRequest req, ServletResponse res,
							 FilterChain chain) throws IOException, ServletException {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;

			//设置编码
				request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");

			//设置iframe策略
			response.setHeader("x-frame-options", "SAMEORIGIN");

			//XSS过滤，SQL注入过滤
			chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);

		}

		public void init(FilterConfig arg0) throws ServletException {
		}

	}
}
