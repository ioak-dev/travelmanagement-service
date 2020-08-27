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
    public ResponseEntity<Wizard> create (@RequestBody Wizard wizard) {
        wizard.setCreatedDate(new Date());
        wizard.setId(uniqueCurrentTimeMS());
        wizard.setStatus(WizardStatus.DRAFT);
        return ResponseEntity.ok(repository.save(wizard));
    }

    @PostMapping("/submit/{wizardId}")
    public ResponseEntity<Wizard> submit (@PathVariable("wizardId") String wizardId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);
        if (wizard.getStatus().equals(WizardStatus.DRAFT)) {
            wizard.setStatus(WizardStatus.L1);
            wizard.setSubmittedOn(new Date());
            wizard = repository.save(wizard);
            service.sendSubmitMail(wizard.getCreatedBy());
        }
        return ResponseEntity.ok(wizard);
    }

    @PostMapping("/approve/{wizardId}")
    public Wizard approveApplicant (@PathVariable String wizardId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);

        if (wizard.getStatus().equals(WizardStatus.L1)) {
            wizard.setStatus(WizardStatus.L2);
            service.sendL1ApproveMail(wizard.getCreatedBy());
        }else if (wizard.getStatus().equals(WizardStatus.L2)) {
            wizard.setStatus(WizardStatus.ADMIN);
            service.sendAdminApproveMail(wizard.getCreatedBy());
        }else if (wizard.getStatus().equals(WizardStatus.ADMIN)) {
            wizard.setStatus(WizardStatus.ADMIN_APPROVED);
            service.sendAdminApproveMail(wizard.getCreatedBy());
        }
        repository.save(wizard);
        return wizard;
    }
    @PostMapping("/reject/{wizardId}")
    public Wizard rejectApplicant (@PathVariable String wizardId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);
        wizard.setStatus(WizardStatus.DRAFT);
        repository.save(wizard);
        service.sendRejectMail(wizard.getCreatedBy());
        return wizard;
    }


    @PutMapping("/edit")
    public ResponseEntity<Wizard> updateWizard (@RequestParam String id, @RequestBody Wizard wizard) {
        Wizard dbWizard = repository.findById(id).orElse(null);
        dbWizard.setBusDetails(wizard.getBusDetails());
        dbWizard.setCabDetails(wizard.getCabDetails());
        dbWizard.setFlightDetails(wizard.getFlightDetails());
        dbWizard.setHotelDetails(wizard.getHotelDetails());
        return ResponseEntity.ok(repository.save(dbWizard));
    }

    @GetMapping("/{type}/{userId}")
    public List<Resource.WizardResource> getWizard (@PathVariable("type") String type,
                                                    @PathVariable("userId") String userId) {
        List<Person> personList = personRepository.findAll();
        Person person = personRepository.findById(userId).orElse(null);
        List<Wizard> wizards = repository.findAll();

        if (type.equalsIgnoreCase("REVIEWER")) {
            List<Wizard> wizardList = new ArrayList<>();

            List<Wizard> l1WizardList = new ArrayList<>();
            List<Wizard> l2WizardList = new ArrayList<>();
            List<Wizard> adminWizardList = new ArrayList<>();


            for (Wizard wizard:wizards) {
                if (!wizard.getCreatedBy().equals(userId)) {
                    if (wizard.getStatus() == WizardStatus.L1) {
                        l1WizardList.add(wizard);
                    } else if (wizard.getStatus() == WizardStatus.L2) {
                        l2WizardList.add(wizard);
                    } else if (wizard.getStatus() == WizardStatus.ADMIN) {
                        adminWizardList.add(wizard);
                    } else if (wizard.getStatus() == WizardStatus.ADMIN_APPROVED) {
                        adminWizardList.add(wizard);
                    } else if (wizard.getStatus() == WizardStatus.COMPLETE) {
                        adminWizardList.add(wizard);
                    }
                }
            }


            List<Role> roles = roleRepository.findByParentEmailId(person.getEmail());

            if (roles.contains(RoleType.L1)) {
                wizardList.addAll(l1WizardList);
            }

            if (roles.contains(RoleType.L2)) {
                wizardList.addAll(l2WizardList);
            }

            if (roles.contains(RoleType.Admin)) {
                wizardList.addAll(adminWizardList);
            }

            return Resource.wizardConverter(wizardList, personList);
        } else if(type.equalsIgnoreCase("APPLICANT")) {
            return Resource.wizardConverter(repository.findAllByCreatedBy(userId), personList);
        }
        return new ArrayList<>();
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
