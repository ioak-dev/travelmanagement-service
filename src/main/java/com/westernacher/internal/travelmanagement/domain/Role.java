package com.westernacher.internal.travelmanagement.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Role {
    @Id
    private String id;
    private String parentUserId;
    private RoleType type;
    private String childUserId;
}
