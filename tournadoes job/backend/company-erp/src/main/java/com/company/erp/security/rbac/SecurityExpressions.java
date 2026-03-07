package com.company.erp.security.rbac;

import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/** Custom security expression helpers for SpEL in @PreAuthorize. */
@Component("sec")
public class SecurityExpressions {

    public boolean isOwner(String ownerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getName().equals(ownerId);
    }
}
