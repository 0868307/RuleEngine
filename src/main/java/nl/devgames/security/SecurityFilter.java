package nl.devgames.security;

import org.springframework.stereotype.Component;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Wouter on 4/17/2016.
 */
@Component
public class SecurityFilter implements Filter {
    public static final String AUTHTOKEN = "Authorization";
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;

        if(!authorize(response.getHeader(AUTHTOKEN))){
            throw new SecurityException("Invalid token");
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
