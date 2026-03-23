package com.company.erp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String hash = encoder.encode("Admin@123");
        System.out.println("Hash: " + hash);
        
        // Verify the hash from migrations
        String migrationHash = "$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzvIGKq.Hm";
        System.out.println("Migration hash valid: " + encoder.matches("Admin@123", migrationHash));
    }
}
