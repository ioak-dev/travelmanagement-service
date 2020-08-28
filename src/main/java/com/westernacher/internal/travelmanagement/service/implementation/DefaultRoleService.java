package com.westernacher.internal.travelmanagement.service.implementation;

import com.westernacher.internal.travelmanagement.domain.Role;
import com.westernacher.internal.travelmanagement.repository.RoleRepository;
import com.westernacher.internal.travelmanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultRoleService implements RoleService {

    @Autowired
    private RoleRepository repository;

    public List<Role> updateAll(List<Role> roles) {

        return repository.saveAll(roles);
    }

}
