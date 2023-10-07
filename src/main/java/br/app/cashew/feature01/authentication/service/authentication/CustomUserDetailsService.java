package br.app.cashew.feature01.authentication.service.authentication;

import br.app.cashew.feature01.authentication.exception.email.EmailNotFoundException;
import br.app.cashew.feature01.authentication.exception.user.UserDetailsLoaderException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private Map<String, UserDetailsLoader> userDetailsLoaders;

    @Autowired
    public CustomUserDetailsService(List<UserDetailsLoader> userDetailsLoaders) {

        this.userDetailsLoaders = userDetailsLoaders.stream()
                .collect(Collectors.toMap(
                        userDetailsLoader -> userDetailsLoader.getClass().getSimpleName(),
                        Function.identity()
                ));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws EmailNotFoundException {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String loaderName = determineLoaderBasedOnEndpoint(request); // Implement this method to decide which loader to use
        UserDetailsLoader loader = userDetailsLoaders.get(loaderName);
        if (loader == null) {
            throw new UserDetailsLoaderException("Loader not found for endpoint: " + loaderName);
        }

        return loader.loadUserByUsername(email);
    }

    private String determineLoaderBasedOnEndpoint(HttpServletRequest request) {
        // Extract the endpoint path from the request
        String requestURI = request.getRequestURI();

        // You can define your mapping logic here based on requestURI
        if (requestURI.startsWith("/api/v1/auth/partner")) {
            return "UserDetailsLoaderForPartner";
        }
        else {
            return "UserDetailsLoaderForUser";
        }
    }

}
