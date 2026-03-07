package com.company.erp.shared.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * AOP Aspect that intercepts @Auditable methods and persists
 * an AuditLog entry for each invocation.
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        String username = resolveUsername();
        String ipAddress = resolveIpAddress();
        String userAgent = resolveUserAgent();
        boolean success = true;
        String errorMessage = null;
        Object result = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            success = false;
            errorMessage = ex.getMessage();
            throw ex;
        } finally {
            persistAuditLog(auditable, username, ipAddress, userAgent,
                    joinPoint.getArgs(), result, success, errorMessage);
        }
    }

    private void persistAuditLog(Auditable auditable, String username,
                                  String ipAddress, String userAgent,
                                  Object[] args, Object result,
                                  boolean success, String errorMessage) {
        try {
            AuditLog.AuditLogBuilder builder = AuditLog.builder()
                    .username(username)
                    .action(auditable.action())
                    .entityType(auditable.entity())
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .success(success)
                    .errorMessage(errorMessage);

            if (auditable.captureValues() && result != null) {
                builder.newValue(objectMapper.writeValueAsString(result));
            }

            auditLogRepository.save(builder.build());
        } catch (Exception ex) {
            // Never fail the main operation because of audit logging
            log.error("Failed to persist audit log for action [{}]: {}",
                    auditable.action(), ex.getMessage());
        }
    }

    private String resolveUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName();
        }
        return "anonymous";
    }

    private String resolveIpAddress() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String xForwardedFor = request.getHeader("X-Forwarded-For");
                if (xForwardedFor != null && !xForwardedFor.isBlank()) {
                    return xForwardedFor.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }
        } catch (Exception ignored) {
        }
        return "unknown";
    }

    private String resolveUserAgent() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                return attrs.getRequest().getHeader("User-Agent");
            }
        } catch (Exception ignored) {
        }
        return "unknown";
    }
}
