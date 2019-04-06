package com.westernacher.internal.travelmanagement.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Role {
    @Id
    private RoleType type;
    private List<String> options;
}
