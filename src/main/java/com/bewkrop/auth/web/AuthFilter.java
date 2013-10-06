package com.bewkrop.auth.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bewkrop.auth.user.User;
import com.bewkrop.auth.user.UserRequest;
import com.bewkrop.auth.user.UserService;
import com.bewkrop.auth.user.UserServiceFactory;


// TODO: fazer o release no maven (com source) e colocar como dependencia no outro projeto
// TODO: framework generica o suficiente para poder usar de outras formas (jaas?)
public class AuthFilter extends HttpFilter {

	private static final String LOGIN_PAGE = "/auth/login.html?next=%s";
	private static final String DEFAULT_ENCODING = "UTF-8";
	
	public static final String USER_PROPERTY = "hTby3zf8K7F46LF4aT8tzfyx8uChhvVJ";
	
	private UserServiceFactory factory = null;
	
	@Override
	public void init(FilterConfig filterConfig) {
		// TODO: colocar url de login, (falha de login? ou configurar a url de falha de login no servlet?)
		super.init(filterConfig);
		this.factory = UserServiceFactory.instance();
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		UserService service = this.factory.build();
		
		String key = request.getParameter("key");
		String password = request.getParameter("password");
		
		if (isEmpty(key, password)) {
			response.sendRedirect(loginURL(request));
			return;
		}
		
		User user = service.get(key);
		
		// TODO: password digester
		if (user == null || !user.hash().equals(password.trim())) {
			// TODO: login / password invalid
			// o que acontece quando um post (ajax) eh redirecionado?
			response.sendRedirect(loginURL(request));
			return;
		}
		
		// AUTHENTICATED
		boolean isSecure = request.isSecure(); // should I allow not secure connection?
		UserRequest userRequest = new UserRequest(user, isSecure);
		request.setAttribute(USER_PROPERTY, userRequest);		
		chain.doFilter(request, response);
		
	}
	
	private boolean isEmpty(String key, String password) {
		if (key == null || password == null) return true;
		if (key.trim().isEmpty() || password.trim().isEmpty()) return true;
		return false;
	}
	
	private static String loginURL(HttpServletRequest request) throws ServletException {
		try {
			String urlDestino = request.getRequestURL().toString();
			String encoded = URLEncoder.encode(urlDestino, DEFAULT_ENCODING);
			return String.format(LOGIN_PAGE, encoded);
		} catch (UnsupportedEncodingException e) {
			throw new ServletException(e);
		}
	}

}
