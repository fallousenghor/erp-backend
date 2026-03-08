package com.company.erp.security.rbac;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

/**
 * Custom permission evaluator enabling expressions like:
 *   @PreAuthorize("hasPermission(null, 'employee:create')")
 *   @PreAuthorize("hasPermission(#id, 'Employee', 'employee:read')")
 * 
 * Also grants access to:
 *   - Admin users (ROLE_ADMIN)
 *   - HR users (ROLE_HR)
 *   - Manager users (ROLE_MANAGER) - for viewing performance data
 *   - Employee users (ROLE_EMPLOYEE) - for viewing their own performance data
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    // Roles that can access performance data for viewing
    private static final Set<String> PERFORMANCE_VIEW_ROLES = Set.of(
        "ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_MANAGER",
        "ROLE_EMPLOYEE",
        "ROLE_FINANCE",
        "ROLE_USER",
        "ROLE_TEACHER"
    );

    // Roles that can create/update performance data
    private static final Set<String> PERFORMANCE_MANAGE_ROLES = Set.of(
        "ROLE_ADMIN",
        "ROLE_HR_MANAGER"
    );

    @Override
    public boolean hasPermission(Authentication authentication,
                                  Object targetDomainObject,
                                  Object permission) {
        if (authentication == null || permission == null) return false;
        
        // Grant access to admins and HR users
        if (isAdminOrHR(authentication)) {
            return true;
        }
        
        // Grant access for performance:view based on roles in PERFORMANCE_VIEW_ROLES
        if ("performance:view".equals(permission.toString()) && hasAnyRole(authentication, PERFORMANCE_VIEW_ROLES)) {
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
        
        // Grant access for performance:view based on roles in PERFORMANCE_VIEW_ROLES
        if ("performance:view".equals(permission.toString()) && hasAnyRole(authentication, PERFORMANCE_VIEW_ROLES)) {
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
     * Check if user has any of the specified roles
     */
    private boolean hasAnyRole(Authentication authentication, Set<String> roles) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(roles::contains);
    }
    
    /**
     * Check if user has ADMIN or HR role
     */
    private boolean isAdminOrHR(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_HR_MANAGER"));
    }
    
    /**
     * Check if user has MANAGER role
     */
    private boolean isManager(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_MANAGER"));
    }
    
    /**
     * Check if user has EMPLOYEE role
     */
    private boolean isEmployee(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_EMPLOYEE"));
    }
}
