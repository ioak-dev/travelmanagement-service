
package com.westernacher.internal.travelmanagement.controller;

import com.westernacher.internal.travelmanagement.domain.Role;
import com.westernacher.internal.travelmanagement.repository.RoleRepository;
import com.westernacher.internal.travelmanagement.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    public ResponseEntity<List<Role>> getRoles (@RequestParam String userId) {
        return ResponseEntity.ok(repository.findByParentUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> get (@PathVariable("id") String id) {
        return ResponseEntity.ok(repository.findById(id).orElse(null));
    }

    @PutMapping
    public ResponseEntity<Role> createAndUpdate (@RequestBody Role role) {
        return ResponseEntity.ok(service.createAndUpdate(role));
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
    public ResponseEntity<?> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        service.uploadCsvFile(file);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
