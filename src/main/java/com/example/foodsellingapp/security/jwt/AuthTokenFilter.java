package com.example.foodsellingapp.security.jwt;

import com.example.foodsellingapp.model.dto.JWTPayloadDto;
import com.example.foodsellingapp.model.entity.Role;
import com.example.foodsellingapp.security.UserDetailsServiceImpl;
import com.example.foodsellingapp.security.constant.URLConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    private AuthService loginService;
//
//    @SneakyThrows
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain chain) throws ServletException, IOException {
//
//        // skip check auth with some specific uri
//        if (!isSkipCheckAuth(request)) {
//            String jwtToken = getJwtFromRequest(request);
//            // validate jwt
//            String errorCode = jwtTokenUtil.verifyJWTAccessToken(jwtToken);
//            if (org.apache.commons.lang3.StringUtils.isNotEmpty(errorCode)) {
//                log.error("INVALID TOKEN : {}", jwtToken);
//                throw new AuthenticationException();
//            }
//
//            // add data to header to down stream service
//            JWTPayloadDto payload = jwtTokenUtil.getPayloadFromJWT(jwtToken);
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                    loginService.buildLoginInfo(payload), null,
//                    loginService.getGrantedAuthories(getRoleNames(payload.getRoleNames())));
//            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        chain.doFilter(request, response);
//    }
//
//    private List<String> getRoleNames(List<Role> roles) {
//        List<String> roleNames = new ArrayList<>();
//        roles.forEach(role -> {
//            roleNames.add(role.getName().name());
//        });
//        return roleNames;
//    }
//
//    private boolean isSkipCheckAuth(HttpServletRequest request) {
//        String urlPath = request.getRequestURI();
//        if (URLConstant.LOGIN_URI.equals(urlPath) || URLConstant.REFRESH_TOKEN.equals(urlPath)) {
//            return true;
//        }
//        if (urlPath.startsWith(URLConstant.PREFIX_PUBLIC_URL)) {
//            return true;
//        }
//        return false;
//    }
//
//    private String getJwtFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader(URLConstant.HEADER_AUTHORIZATION);
//
//        if (org.apache.commons.lang3.StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return StringUtils.EMPTY;
//    }

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }
}
