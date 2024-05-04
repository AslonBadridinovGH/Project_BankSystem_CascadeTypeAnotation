package uz.pdp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.service.AuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilters extends OncePerRequestFilter {

    @Autowired
    JwtProvider   jwtProvider;

    @Autowired
    AuthService myAuthService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                             FilterChain filterChain) throws ServletException, IOException {

        // take token from HttpServletRequest:
        String token = request.getHeader("Authorization");

        if (token!=null && token.startsWith("Bearer")){

        token=token.substring(7);

    /*
        boolean validateToken = jwtProvider.validateToken(token);
        if (validateToken){
        String username = jwtProvider.getUsernameFromToken(token);
        UserDetails userDetails = myAuthService.loadUserByUsername(username);
    */

            String  emailFromToken = jwtProvider.getEmailFromToken(token);
            if (emailFromToken!=null){

           UserDetails userDetails = myAuthService.loadUserByUsername(emailFromToken);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

       }
    }

         filterChain.doFilter(request,response);

    }

}
