package com.westernacher.internal.travelmanagement.service;

import com.westernacher.internal.travelmanagement.domain.Role;
import com.westernacher.internal.travelmanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoleService {
    Role createAndUpdate (Role role);

    List<Role> updateAll(List<Role> roles);

    void uploadCsvFile(MultipartFile file);

}
