package com.bd.serwis.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAuthenticationFilter implements Filter {

	private FilterConfig config;
	private final static String AUTH_KEY_USER = "user.logged";

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		if (((HttpServletRequest) req).getSession().getAttribute(AUTH_KEY_USER) == null) {
			((HttpServletResponse) resp).sendRedirect(request.getContextPath()
					+ "/error/access_denied.xhtml");
		} else {
			chain.doFilter(req, resp);
		}
	}

	/**
	 *
	 * @param config
	 * @throws ServletException
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	/**
	     *
	     */
	@Override
	public void destroy() {
		config = null;
	}
}
