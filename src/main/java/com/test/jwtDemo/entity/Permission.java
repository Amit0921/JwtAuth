package com.test.jwtDemo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

	ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    MEMBER_READ("member:read"),
    MEMBER_CREATE("member:create"),
    ;

    @Getter
    private final String permission;
}
