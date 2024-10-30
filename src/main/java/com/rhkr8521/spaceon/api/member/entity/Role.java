package com.rhkr8521.spaceon.api.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER"), HOST("ROLE_HOST"), ADMIN("ROLE_ADMIN");

    private final String key;
}
