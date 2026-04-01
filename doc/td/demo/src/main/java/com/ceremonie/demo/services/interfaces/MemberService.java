package com.ceremonie.demo.services.interfaces;


import java.util.List;

import com.ceremonie.demo.dto.request.CreateMemberRequest;
import com.ceremonie.demo.dto.request.UpdateMemberRequest;
import com.ceremonie.demo.dto.response.MemberResponse;

public interface MemberService {
    MemberResponse createMember(CreateMemberRequest request);
    MemberResponse updateMember(Long id, UpdateMemberRequest request);
    MemberResponse getMemberById(Long id);
    MemberResponse getMemberByMemberNumber(String memberNumber);
    List<MemberResponse> getAllMembers();
    List<MemberResponse> getActiveMembers();
    List<MemberResponse> searchMembers(String search);
    void deleteMember(Long id);
    Long countActiveMembers();
    String generateMemberNumber();
}
