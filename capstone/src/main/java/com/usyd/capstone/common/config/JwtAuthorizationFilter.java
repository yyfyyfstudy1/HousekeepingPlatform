package com.usyd.capstone.common.config;

import com.usyd.capstone.common.util.TokenUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        // 获取当前请求的 URL
        String requestUrl = request.getRequestURI();

        // 放行接口
        if (requestUrl.endsWith("/login") || requestUrl.startsWith("/public") || requestUrl.startsWith("/imserver")) {
            chain.doFilter(request, response);
            return;
        }

        String token = extractTokenFromRequest(request);


        if (token != null) {

            // token前面的"Bearer "需要截取
            String[] parts = token.split(" ");
            if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
                token = parts[1];
            }

            Claims claims = TokenUtils.getClaims(token, 60L);
            String roles = claims.get("roles").toString();

            // 从token的声明中获取角色信息
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);

            Authentication authentication = new UsernamePasswordAuthenticationToken(null, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }



    private String extractTokenFromRequest(HttpServletRequest request) {
        // 从请求头或其他位置提取 JWT

        String token = request.getHeader("Authorization"); // 从请求头获取 Token


        return token;
    }
}