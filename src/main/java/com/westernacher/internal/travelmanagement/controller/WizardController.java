package com.westernacher.internal.travelmanagement.controller;

import com.westernacher.internal.travelmanagement.controller.representation.Resource;
import com.westernacher.internal.travelmanagement.domain.*;
import com.westernacher.internal.travelmanagement.repository.WizardRepository;
import com.westernacher.internal.travelmanagement.service.WizardService;
import com.westernacher.internal.travelmanagement.service.implementation.DefaultWizardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/wizard")
@Slf4j
public class WizardController {

    @Autowired
    private WizardRepository repository;

    @Autowired
    private WizardService service;

    @GetMapping("/{id}")
    public ResponseEntity<Wizard> getWizard (@PathVariable String id) {
        return ResponseEntity.ok(repository.findById(id).orElse(null));
    }

    @PutMapping("/create")
    public ResponseEntity<Wizard> createAndUpdate (@RequestParam String userId,
                                          @RequestBody Wizard wizard) {
        return ResponseEntity.ok(service.createAndUpdate(userId, wizard));
    }

    @PostMapping("/submit/{wizardId}")
    public ResponseEntity<Wizard> submit (@PathVariable String wizardId) {
        return ResponseEntity.ok(service.submit(wizardId));
    }

    @PostMapping("/approve/{wizardId}")
    public ResponseEntity<Wizard> approveApplicant (@PathVariable String wizardId) {
        return ResponseEntity.ok(service.approveApplicant(wizardId));
    }
    @PostMapping("/reject/{wizardId}")
    public ResponseEntity<Wizard> rejectApplicant (@PathVariable String wizardId) {
        return ResponseEntity.ok(service.rejectApplicant(wizardId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Resource.WizardResource>> getWizardList (@PathVariable String userId) {
        return ResponseEntity.ok(service.getWizardList(userId));
    }

    @DeleteMapping("/{id}")
    public void deleteApplicant (@PathVariable String id) {
        repository.deleteById(id);
    }



    @PostMapping("/{wizardId}/complete/{userId}")
    public void complete(@PathVariable String wizardId, @PathVariable String userId) {
        service.complete(wizardId, userId);
    }


}
