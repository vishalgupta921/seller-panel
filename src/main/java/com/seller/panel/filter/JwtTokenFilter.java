package com.seller.panel.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.github.javafaker.App;
import com.seller.panel.controller.ExceptionHandlerController;
import com.seller.panel.handler.ExceptionHandler;
import com.seller.panel.util.AppConstants;
import com.seller.panel.util.EndPointConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenFilter extends BaseFilter implements Filter {

	protected ResourceServerTokenServices resourceServerTokenServices;

	public JwtTokenFilter(ResourceServerTokenServices resourceServerTokenServices,
						  ExceptionHandlerController exceptionHandlerController, ExceptionHandler exceptionHandler) {
		super(exceptionHandlerController, exceptionHandler);
		this.resourceServerTokenServices = resourceServerTokenServices;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		if (!request.getRequestURI().startsWith(request.getContextPath() + "/api") ||
				request.getRequestURI().startsWith(request.getContextPath() + EndPointConstants.Join.JOIN) ||
				request.getRequestURI().startsWith(request.getContextPath() + EndPointConstants.ENDPOINTS_PREFIX
						+"/"+AppConstants.INVITE) ||
				request.getRequestURI().startsWith(request.getContextPath() + EndPointConstants.Registration.REGISTER)) {
			chain.doFilter(req, res);
			return;
		}
		if (request.getRequestURI().startsWith(request.getContextPath() + "/api/v1/login")) {
			chain.doFilter(req, res);
			return;
		}
		String authHeader = request.getHeader("authorization");
		if(StringUtils.isBlank(authHeader)) {
			String headerPayload = null;
			String signature = null;
			Cookie[] cookies = request.getCookies();
			if(cookies != null) {
				for (Cookie ck : cookies) {
					if (ck.getName().equals(AppConstants.HEADER_PAYLOAD))
						headerPayload = ck.getName().trim();
					if (ck.getName().equals(AppConstants.SIGNATURE))
						signature = ck.getName();
				}
			}
			authHeader = "bearer "+headerPayload+"."+signature;
		}
		if (StringUtils.isBlank(authHeader) || authHeader.trim().length() < 7) {
			handleException(res, getException("SP-6"));
			return;
		}
		OAuth2AccessToken accessToken = resourceServerTokenServices.readAccessToken(authHeader.trim().substring(7));
		Map<String, Object> additionalInfo = accessToken.getAdditionalInformation();
		request.setAttribute(AppConstants.ADDITIONAL_INFO, additionalInfo);
		request.setAttribute("token", authHeader.trim().substring(7));
		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
		// Implementing <code>Filter</code> interface
	}

	@Override
	public void init(FilterConfig config) {
		// Implementing <code>Filter</code> interface
	}
}