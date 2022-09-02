package com.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticationFilter implements Filter{
	private ServletContext context;

    public void init(FilterConfig fConfig) throws ServletException {
        this.context = fConfig.getServletContext();
        this.context.log("AuthenticationFilter initialized");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userid") == null) {   //checking whether the session exists
            //this.context.log("Unauthorized access request");
            res.sendRedirect(req.getContextPath() + "/login.jsp");
        } else {
            // pass the request along the filter chain
        	res.setHeader("Cache-Control",
                    "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        	res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        	res.setDateHeader("Expires", 0);
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
        //close any resources here
    }
}
