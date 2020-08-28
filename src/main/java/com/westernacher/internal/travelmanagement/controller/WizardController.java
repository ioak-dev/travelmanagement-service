package com.westernacher.internal.travelmanagement.controller;

import com.westernacher.internal.travelmanagement.controller.representation.Resource;
import com.westernacher.internal.travelmanagement.domain.*;
import com.westernacher.internal.travelmanagement.repository.PersonRepository;
import com.westernacher.internal.travelmanagement.repository.RoleRepository;
import com.westernacher.internal.travelmanagement.repository.WizardRepository;
import com.westernacher.internal.travelmanagement.service.WizardService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/wizard")
@Slf4j
public class WizardController {

    @Autowired
    private WizardRepository repository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private WizardService service;

    @Autowired
    private RoleRepository roleRepository;

    private static final AtomicLong LAST_TIME_MS = new AtomicLong();

    @GetMapping("/{id}")
    public Wizard getWizard (@PathVariable("id") String id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/create")
    public ResponseEntity<Wizard> create (@RequestParam String userId, @RequestBody Wizard wizard) {
        Wizard dbWizard = null;

        if (wizard.getId() != null) {
            dbWizard = repository.findById(wizard.getId()).orElse(null);
        }

        if (dbWizard != null) {
            if (dbWizard.getStatus().equals(WizardStatus.DRAFT) && dbWizard.getCreatedBy().equals(userId)) {
                dbWizard.setProjectDetails(wizard.getProjectDetails());
                dbWizard.setCabDetails(wizard.getCabDetails());
                dbWizard.setTravelType(wizard.getTravelType());
                dbWizard.setBusDetails(wizard.getBusDetails());
                dbWizard.setTrainDetails(wizard.getTrainDetails());
                dbWizard.setFlightDetails(wizard.getFlightDetails());
                dbWizard.setHotelDetails(wizard.getHotelDetails());
                dbWizard.setInsuranceDetails(wizard.getInsuranceDetails());
                dbWizard.setVisaDetails(wizard.getVisaDetails());
                dbWizard.setPmEmail(wizard.getPmEmail());
                dbWizard.setUpdatedDate(new Date());
                dbWizard.setUpdatedBy(userId);
                return ResponseEntity.ok(repository.save(dbWizard));
            }
        } else {
            wizard.setCreatedDate(new Date());
            wizard.setId(uniqueCurrentTimeMS());
            wizard.setStatus(WizardStatus.DRAFT);
            return ResponseEntity.ok(repository.save(wizard));
        }
        return null;
    }

    @PostMapping("/submit/{wizardId}")
    public ResponseEntity<Wizard> submit (@PathVariable("wizardId") String wizardId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);
        if (wizard.getStatus().equals(WizardStatus.DRAFT)) {
            wizard.setStatus(WizardStatus.L1);
            wizard.setSubmittedOn(new Date());
            wizard = repository.save(wizard);
            //service.sendSubmitMail(wizard.getCreatedBy());
        }
        return ResponseEntity.ok(wizard);
    }

    @PostMapping("/approve/{wizardId}")
    public Wizard approveApplicant (@PathVariable String wizardId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);

        if (wizard.getStatus().equals(WizardStatus.DRAFT)) {
            wizard.setStatus(WizardStatus.L1);
            //service.sendL1ApproveMail(wizard.getCreatedBy());
        }else if (wizard.getStatus().equals(WizardStatus.L1)) {
            wizard.setStatus(WizardStatus.L2);
            //service.sendL1ApproveMail(wizard.getCreatedBy());
        }else if (wizard.getStatus().equals(WizardStatus.L2)) {
            wizard.setStatus(WizardStatus.ADMIN);
            //service.sendAdminApproveMail(wizard.getCreatedBy());
        }else if (wizard.getStatus().equals(WizardStatus.ADMIN)) {
            wizard.setStatus(WizardStatus.ADMIN_APPROVED);
            //service.sendAdminApproveMail(wizard.getCreatedBy());
        }
        repository.save(wizard);
        return wizard;
    }
    @PostMapping("/reject/{wizardId}")
    public Wizard rejectApplicant (@PathVariable String wizardId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);
        wizard.setStatus(WizardStatus.DRAFT);
        //service.sendRejectMail(wizard.getCreatedBy());
        return repository.save(wizard);
    }


    /*@PutMapping("/edit")
    public ResponseEntity<Wizard> updateWizard (@RequestParam String id, @RequestParam String loginId,
                                                @RequestBody Wizard wizard) {
        Wizard dbWizard = repository.findById(id).orElse(null);
        if (dbWizard.getStatus().equals(WizardStatus.DRAFT) && dbWizard.getCreatedBy().equals(loginId)) {
            dbWizard.setProjectDetails(wizard.getProjectDetails());
            dbWizard.setCabDetails(wizard.getCabDetails());
            dbWizard.setTravelType(wizard.getTravelType());
            dbWizard.setBusDetails(wizard.getBusDetails());
            dbWizard.setTrainDetails(wizard.getTrainDetails());
            dbWizard.setFlightDetails(wizard.getFlightDetails());
            dbWizard.setHotelDetails(wizard.getHotelDetails());
            dbWizard.setInsuranceDetails(wizard.getInsuranceDetails());
            dbWizard.setVisaDetails(wizard.getVisaDetails());
            dbWizard.setPmEmail(wizard.getPmEmail());
            dbWizard.setUpdatedDate(new Date());
            dbWizard.setUpdatedBy(loginId);
            return ResponseEntity.ok(repository.save(dbWizard));
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }*/

    @GetMapping("/user/{userId}")
    public List<Resource.WizardResource> getWizardList (@PathVariable("userId") String userId) {

        List<Wizard> response = new ArrayList<>();

        response.addAll(repository.findAllByCreatedBy(userId));

        List<Role> roles = roleRepository.findByParentUserId(userId);

        List<RoleType> roleTypes = new ArrayList<>();

        List<String> childIds = new ArrayList<>();

        roles.stream().forEach(role -> {
            childIds.add(role.getChildUserId());
            roleTypes.add(role.getType());
        });

        childIds.stream().forEach(childId-> {
            response.addAll(repository.findAllByCreatedBy(childId));
        });

        if (roleTypes.contains(RoleType.Admin)) {
            response.addAll(repository.findAllByStatus(WizardStatus.ADMIN));
        }

        return Resource.wizardConverter(response, personRepository.findAll());
    }

    @DeleteMapping("/{id}")
    public void deleteApplicant (@PathVariable("id") String id) {
        repository.deleteById(id);
    }



    @PostMapping("/{wizardId}/complete/{userId}")
    public void complete(@PathVariable("wizardId") String wizardId, @PathVariable("userId") String userId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);
        wizard.setStatus(WizardStatus.COMPLETE);
        repository.save(wizard);
    }

    private static String uniqueCurrentTimeMS() {
        long now = System.currentTimeMillis();
        while(true) {
            long lastTime = LAST_TIME_MS.get();
            if (lastTime >= now)
                now = lastTime+1;
            if (LAST_TIME_MS.compareAndSet(lastTime, now))
                return Long.toString(now);
        }
    }
}
