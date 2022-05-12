package com.hcmute.backendtechnologicalapplianceswebsite.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.hcmute.backendtechnologicalapplianceswebsite.filter.CustomAuthenticationFilter.JWT_SECRET;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String method = request.getMethod();
        String apiPath = "/api/technological_appliances";

        if (method.equals("GET")) {
            List<String> publicPaths = new ArrayList<>();
            publicPaths.add(apiPath + "/products");
            publicPaths.add(apiPath + "/products/**");
            publicPaths.add(apiPath + "/brands");
            publicPaths.add(apiPath + "/brands/**");
            publicPaths.add(apiPath + "/reviews");
            publicPaths.add(apiPath + "/reviews/**");
            publicPaths.add(apiPath + "/categories");
            publicPaths.add(apiPath + "/categories/**");

            for (String path : publicPaths) {
                if (path.contains("**") && servletPath.startsWith(path.replace("**", ""))) {
                    return true;
                } else if (servletPath.equals(path)) {
                    return true;
                }
            }
        } else if (method.equals("POST")) {
            List<String> publicPaths = new ArrayList<>();
            publicPaths.add(apiPath + "/login");
            publicPaths.add(apiPath + "/register");

            for (String path : publicPaths) {
                if (servletPath.equals(path)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/technological_appliances/login")) {
            filterChain.doFilter(request, response);
        } else {
            String token = request.getHeader(AUTHORIZATION);
            if (token != null && token.startsWith("Bearer ")) {
                try {
                    token = token.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET.getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT jwt = verifier.verify(token);
                    String username = jwt.getSubject();
                    String[] roles = jwt.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }
}
