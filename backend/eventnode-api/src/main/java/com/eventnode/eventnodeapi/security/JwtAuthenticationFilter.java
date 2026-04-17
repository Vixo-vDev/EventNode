package com.eventnode.eventnodeapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * FLUJO DE DATOS (EventNode)
 * Rol del archivo: filtra cada request del backend y valida JWT antes de llegar a controllers.
 * Por que existe: asegura que Web y Mobile solo accedan a rutas privadas con identidad valida.
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Extraemos el token string puro del request
        String token = getTokenFromRequest(request);

        // LÓGICA DE NEGOCIO: ¿Por qué validamos aquí?
        // Si no validamos la firma y expiración (validateToken), cualquiera podría 
        // inventarse un token y acceder a los endpoints privados o robar datos (falsificación).
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            
            // MAPEO DE DEPENDENCIAS: El token guarda el correo (username) y con eso 
            // buscamos al usuario en la BD (UserDetailsService).
            String username = jwtTokenProvider.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            // Guardamos el contexto. 
            // LÓGICA DE REPERCUSIÓN: Al guardar esto en SecurityContextHolder, el resto 
            // del código backend sabe "quién" está haciendo la petición actual.
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        // El prefijo "Bearer " define el contrato entre clientes (Web/Mobile) y backend.
        // Si cambia esta cadena, el token no se extrae y la peticion queda sin autenticacion.
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer ".length() == 7
        }
        return null;
    }
}
