package com.ceremonie.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ceremonie.demo.repository.CeremonialYearRepository;
import com.ceremonie.demo.repository.MemberRepository;
import com.ceremonie.demo.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private CeremonialYearRepository ceremonialYearRepository;
    
    @Test
    public void testRepositoriesAreNotNull() {
        assertNotNull(userRepository);
        assertNotNull(memberRepository);
        assertNotNull(ceremonialYearRepository);
    }
    
    @Test
    public void testCountMembers() {
        Long count = memberRepository.countActiveMembers();
        assertNotNull(count);
        assertTrue(count >= 0);
    }
}