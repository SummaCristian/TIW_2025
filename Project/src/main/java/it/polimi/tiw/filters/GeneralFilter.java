package it.polimi.tiw.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class GeneralFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // (Optional) Nothing needed here
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String contextPath = req.getContextPath(); // ex: /Project_TIW
        String path = req.getRequestURI().substring(contextPath.length()); // ex: /css/mystyle.css

        boolean loggedIn = (req.getSession(false) != null) && (req.getSession(false).getAttribute("user") != null);

        boolean isPublicPath = path.equals("/login") || path.equals("/signup") ||
                                path.equals("/LoginServlet") || path.equals("/SignUpServlet") || 
                                path.equals("/app") || path.equals("/logout");

        boolean isStaticResource = path.startsWith("/css/") || path.startsWith("/images/") || path.startsWith("/js/");

        // 💥 Special case: ROOT path
        if (path.equals("/")) {
            res.sendRedirect(contextPath + "/app"); // Redirect root to /app
            return;
        }

        if (loggedIn || isPublicPath || isStaticResource) {
            // ✅ User is allowed to proceed
            chain.doFilter(request, response);
        } else {
            // ❌ Not logged in and trying to access something protected
            res.sendRedirect(contextPath + "/app");
        }
    }


    @Override
    public void destroy() {
        // (Optional) Nothing needed here
    }
}