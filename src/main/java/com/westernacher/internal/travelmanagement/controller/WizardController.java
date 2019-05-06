package com.westernacher.internal.travelmanagement.controller;

import com.westernacher.internal.travelmanagement.controller.representation.Resource;
import com.westernacher.internal.travelmanagement.domain.*;
import com.westernacher.internal.travelmanagement.repository.PersonRepository;
import com.westernacher.internal.travelmanagement.repository.WizardRepository;
import com.westernacher.internal.travelmanagement.service.WizardService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
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

    private static final AtomicLong LAST_TIME_MS = new AtomicLong();

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Wizard getWizard (@PathVariable("id") String id) {
        return repository.findById(id).orElse(null);
    }


    @RequestMapping(value = "/{type}/{userId}", method = RequestMethod.GET)
    public List<Resource.WizardResource> getWizard (@PathVariable("type") String type,
                                                    @PathVariable("userId") String userId) {
        List<Person> personList = personRepository.findAll();
        Person person = personRepository.findById(userId).orElse(null);
        List<Wizard> wizards = repository.findAll();

        if (type.equals("REVIEWER")) {
            List<Wizard> wizardList = new ArrayList<>();

            List<Wizard> l1WizardList = new ArrayList<>();
            List<Wizard> l2WizardList = new ArrayList<>();
            List<Wizard> adminWizardList = new ArrayList<>();


            for (Wizard wizard:wizards) {
                if (wizard.getStatus()== WizardStatus.L1) {
                    l1WizardList.add(wizard);
                } else if (wizard.getStatus()== WizardStatus.L2) {
                    l2WizardList.add(wizard);
                } else if (wizard.getStatus()== WizardStatus.ADMIN) {
                    adminWizardList.add(wizard);
                } else if (wizard.getStatus()== WizardStatus.ADMIN_APPROVED) {
                    adminWizardList.add(wizard);
                }
            }

            List<String> roles = new ArrayList<>();

            person.getRoles().stream().forEach(role -> {
                roles.add(role.getType().name());
                roles.addAll(role.getOptions());
            });

            if (roles.contains(RoleType.L1.name())) {
                wizardList.addAll(l1WizardList);
            }

            if (roles.contains(RoleType.L2.name())) {
                wizardList.addAll(l2WizardList);
            }

            if (roles.contains(RoleType.Admin.name())) {
                wizardList.addAll(adminWizardList);
            }

            return Resource.wizardConverter(wizardList, personList);
        } else if(type.equals("APPLICANT")) {
            return Resource.wizardConverter(repository.findAllByCreatedBy(userId), personList);
        }
        return new ArrayList<>();
    }


    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public Wizard update (@Valid @RequestBody Wizard wizard, @PathVariable("userId") String userId) {
        if (StringUtils.isEmpty(wizard.getId())) {
            wizard.setCreatedBy(userId);
            wizard.setId(uniqueCurrentTimeMS());
            wizard.setStatus(WizardStatus.DRAFT);
        }
        return repository.save(wizard);
    }

    @RequestMapping(value = "/{wizardId}/submit/{userId}", method = RequestMethod.POST)
    public Wizard applicantSubmit (@PathVariable("wizardId") String wizardId, @PathVariable("userId") String userId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);
        wizard.setStatus(WizardStatus.L1);
        wizard.setSubmittedOn(new Date());
        repository.save(wizard);
        service.sendSubmitMail(wizard.getCreatedBy());
        return wizard;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteApplicant (@PathVariable("id") String id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/{wizardId}/approve/{userId}", method = RequestMethod.POST)
    public Wizard approveApplicant (@PathVariable("wizardId") String wizardId, @PathVariable("userId") String userId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);

        if (wizard.getStatus() == WizardStatus.L1) {
            wizard.setStatus(WizardStatus.ADMIN);
            service.sendL1ApproveMail(wizard.getCreatedBy());

        }else if (wizard.getStatus() == WizardStatus.ADMIN) {
            wizard.setStatus(WizardStatus.ADMIN_APPROVED);
            service.sendAdminApproveMail(wizard.getCreatedBy());
        }
        repository.save(wizard);
        return wizard;
    }
    @RequestMapping(value = "/{wizardId}/reject/{userId}", method = RequestMethod.POST)
    public Wizard rejectApplicant (@PathVariable("wizardId") String wizardId, @PathVariable("userId") String userId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);
        wizard.setStatus(WizardStatus.DRAFT);
        repository.save(wizard);
        service.sendRejectMail(wizard.getCreatedBy());
        return wizard;
    }

    @RequestMapping(value = "/{wizardId}/complete/{userId}", method = RequestMethod.POST)
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
