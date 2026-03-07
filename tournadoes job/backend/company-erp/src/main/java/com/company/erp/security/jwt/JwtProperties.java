package com.company.erp.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Binds JWT configuration from application.yml:
 *   app.security.jwt.*
 */
@Component
@ConfigurationProperties(prefix = "app.security.jwt")
@Getter
@Setter
public class JwtProperties {

    /** HMAC secret key (Base64 or hex, min 256 bits) */
    private String secretKey;

    /** Access token TTL in milliseconds (default: 15 min) */
    private long accessTokenExpiration = 900_000L;

    /** Refresh token TTL in milliseconds (default: 7 days) */
    private long refreshTokenExpiration = 604_800_000L;
}
