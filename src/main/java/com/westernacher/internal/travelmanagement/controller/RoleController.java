
package com.westernacher.internal.travelmanagement.controller;

import com.westernacher.internal.travelmanagement.domain.Role;
import com.westernacher.internal.travelmanagement.domain.RoleType;
import com.westernacher.internal.travelmanagement.repository.RoleRepository;
import com.westernacher.internal.travelmanagement.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private RoleService service;

    @GetMapping
    public List<Role> get () {
        return repository.findAll();
    }

    @PutMapping
    public Role createAndUpdate (@RequestBody Role role) {
        Role existingRole = null;
        if (role.getId() != null) {
            existingRole = repository.findById(role.getId()).orElse(null);
        }

        if (existingRole !=null) {
            existingRole.setParentEmailId(role.getParentEmailId());
            existingRole.setType(role.getType());
            existingRole.setChildEmailId(role.getChildEmailId());
            return repository.save(existingRole);
        }
        return repository.save(role);
    }

    @GetMapping("/{id}")
    public Role get (@PathVariable("id") String id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping("/email/{emailId}")
    public List<RoleType> getRoleTypes (@PathVariable String emailId) {
        List<Role> roles = repository.findByParentEmailId(emailId);
        List<RoleType> roleTypes =  new ArrayList<>();
        roles.stream().forEach(role -> {
            roleTypes.add(role.getType());
        });
        return roleTypes;
    }

    @GetMapping("/email/{emailId}/roles")
    public List<Role> getRoles (@PathVariable String emailId) {
        return repository.findByParentEmailId(emailId);
    }

    @DeleteMapping
    public void deleteAll () {
        repository.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void delete (@PathVariable("id") String id) {
        repository.deleteById(id);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadMultipart(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(service.updateAll(getRoles(getRows(file))));
    }


    private List<String> getRows(MultipartFile file) {
        BufferedReader br;
        List<String> csvline = new ArrayList<>();
        try {

            String line;
            InputStream is = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                csvline.add(line);
            }

            csvline.remove(0);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return csvline;
    }

    private List<Role> getRoles(List<String> rows) {

        List<Role> roles =  new ArrayList<>();

        for(String line : rows) {
            String[] values = line.split(",");
            Role role = new Role();
            role.setParentEmailId(values[0].trim());
            role.setType(RoleType.valueOf(values[1].trim()));
            role.setChildEmailId(values[2].trim());
            roles.add(role);
        }
        return roles;

    }
}
