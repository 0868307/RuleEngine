package nl.tma.security;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Wouter on 4/17/2016.
 */
@Component
public class SecurityFilter implements Filter {
    public static final String AUTHORIZATION = "Authorization";
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        String authToken = ((HttpServletRequest) req).getHeader(AUTHORIZATION);
        String path = ((HttpServletRequest) req).getRequestURI();
        System.out.println("token = "+ authToken);
        if (path.startsWith("/login") || path.startsWith("/sonar")) {
            chain.doFilter(req, res);
        }
        else if (!authorize(authToken)) {

            throw new SecurityException("Not a valid token");
        }
        chain.doFilter(req, res);



    }
    public boolean authorize(String token){
        return AuthToken.checkToken(token);
    }
    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig arg0) throws ServletException {}
}
