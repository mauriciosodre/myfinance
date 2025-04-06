package br.com.msodrej.myfinance.domain.utils;

import br.com.msodrej.myfinance.adapter.config.security.UserPrincipal;
import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {
        // Construtor privado para evitar instância
    }

    /**
     * Obtém o usuário autenticado atual a partir do SecurityContextHolder.
     *
     * @return UserPrincipal do usuário autenticado.
     * @throws SystemErrorException se o usuário não estiver autenticado.
     */
    public static UserPrincipal getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            return userPrincipal;
        }
        throw new SystemErrorException("User not authenticated");
    }
}