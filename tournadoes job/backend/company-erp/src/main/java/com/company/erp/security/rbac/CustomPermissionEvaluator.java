package com.company.erp.security.rbac;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Custom permission evaluator enabling expressions like:
 *   @PreAuthorize("hasPermission(null, 'employee:create')")
 *   @PreAuthorize("hasPermission(#id, 'Employee', 'employee:read')")
 * 
 * Also grants access to:
 *   - Admin users (ROLE_ADMIN)
 *   - HR users (ROLE_HR)
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication,
                                  Object targetDomainObject,
                                  Object permission) {
        if (authentication == null || permission == null) return false;
        
        // Grant access to admins and HR users
        if (isAdminOrHR(authentication)) {
            return true;
        }
        
        return hasAuthority(authentication, permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                  Serializable targetId,
                                  String targetType,
                                  Object permission) {
        if (authentication == null || permission == null) return false;
        
        // Grant access to admins and HR users
        if (isAdminOrHR(authentication)) {
            return true;
        }
        
        return hasAuthority(authentication, permission.toString());
    }

    private boolean hasAuthority(Authentication authentication, String permission) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(permission));
    }
    
    /**
     * Check if user has ADMIN or HR role
     */
    private boolean isAdminOrHR(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_HR"));
    }
}
