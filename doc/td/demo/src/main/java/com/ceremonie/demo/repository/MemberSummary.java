package com.ceremonie.demo.repository;

public interface MemberSummary {
    Long getId();
    String getMemberNumber();
    String getFirstName();
    String getLastName();
    String getPhoneNumber();
    Boolean getActive();
}