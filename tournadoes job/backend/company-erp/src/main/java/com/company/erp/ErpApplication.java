package com.company.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Company ERP — Main Application Entry Point
 * Architecture : Hexagonal + DDD + CQRS
 * Java 21 | Spring Boot 3.2+
 *
 * Config beans (@EnableJpaAuditing, @EnableCaching, @EnableAsync)
 * are declared in their respective @Configuration classes:
 * - JpaConfig       → @EnableJpaAuditing + AuditorAware
 * - CacheConfig     → @EnableCaching
 * - AsyncConfig     → @EnableAsync + ThreadPoolTaskExecutor
 */
@SpringBootApplication
public class ErpApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErpApplication.class, args);
    }
}
