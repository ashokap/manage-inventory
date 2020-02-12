package com.example.manageinventory.authentication;

import com.example.manageinventory.authentication.AppUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthRequestFilter extends OncePerRequestFilter {

    @Autowired
    private AppUserDetailService appUserDetailService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        //  Strategy:
        //  This runs once per request
        //  Bearer token is part of the http headers in the request
        //  Therefore, the procedure is:
        //  1.  Get the token from header
        //  2.  Use the token to extract the username stored in it
        //  3.  If the username is not null and the authorization is not yet done,
        //      validate the token
        //  4.  Once the token is validated, put the token in the request object

        final String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtils.extractUsername(jwt);
        }

        //  If already authenticated, we do not wish to authenticate again
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.appUserDetailService.loadUserByUsername(username);


            if (jwtUtils.validateToken(jwt, userDetails)) {

                //  Intercepting the normal course
                //  These are all defaults, but we are plugging only after validation
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        //  Let the middleware processing go on
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
