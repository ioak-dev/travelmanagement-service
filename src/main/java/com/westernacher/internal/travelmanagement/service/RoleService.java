package com.westernacher.internal.travelmanagement.service;

import com.westernacher.internal.travelmanagement.domain.Role;
import com.westernacher.internal.travelmanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    List<Role> updateAll(List<Role> roles);

}
