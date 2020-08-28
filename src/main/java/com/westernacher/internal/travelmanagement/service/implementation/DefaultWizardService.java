package com.westernacher.internal.travelmanagement.service.implementation;

import com.westernacher.internal.travelmanagement.controller.representation.Resource;
import com.westernacher.internal.travelmanagement.domain.*;
import com.westernacher.internal.travelmanagement.repository.PersonRepository;
import com.westernacher.internal.travelmanagement.repository.RoleRepository;
import com.westernacher.internal.travelmanagement.repository.WizardRepository;
import com.westernacher.internal.travelmanagement.service.EmailUtility;
import com.westernacher.internal.travelmanagement.service.WizardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DefaultWizardService implements WizardService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private WizardRepository repository;

    @Autowired
    private EmailUtility utility;

    @Autowired
    private RoleRepository roleRepository;

    private static final AtomicLong LAST_TIME_MS = new AtomicLong();

    @Override
    public Wizard createAndUpdate(String userId, Wizard wizard) {
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
                return repository.save(dbWizard);
            }
        } else {
            wizard.setCreatedDate(new Date());
            wizard.setId(uniqueCurrentTimeMS());
            wizard.setStatus(WizardStatus.DRAFT);
            return repository.save(wizard);
        }
        return null;
    }

    @Override
    public Wizard submit (String wizardId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);
        if (wizard.getStatus().equals(WizardStatus.DRAFT)) {
            wizard.setStatus(WizardStatus.L1);
            wizard.setSubmittedOn(new Date());
            wizard = repository.save(wizard);
            sendSubmitMail(wizard.getCreatedBy());
        }
        return wizard;
    }

    @Override
    public Wizard approveApplicant (String wizardId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);

        if (wizard.getStatus().equals(WizardStatus.DRAFT)) {
            wizard.setStatus(WizardStatus.L1);
            sendL1ApproveMail(wizard.getCreatedBy());
        }else if (wizard.getStatus().equals(WizardStatus.L1)) {
            wizard.setStatus(WizardStatus.L2);
            sendL1ApproveMail(wizard.getCreatedBy());
        }else if (wizard.getStatus().equals(WizardStatus.L2)) {
            wizard.setStatus(WizardStatus.ADMIN);
            sendAdminApproveMail(wizard.getCreatedBy());
        }else if (wizard.getStatus().equals(WizardStatus.ADMIN)) {
            wizard.setStatus(WizardStatus.ADMIN_APPROVED);
            sendAdminApproveMail(wizard.getCreatedBy());
        }
        repository.save(wizard);
        return wizard;
    }

    @Override
    public Wizard rejectApplicant (String wizardId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);
        wizard.setStatus(WizardStatus.DRAFT);
        sendRejectMail(wizard.getCreatedBy());
        return repository.save(wizard);
    }

    @Override
    public List<Resource.WizardResource> getWizardList (String userId) {

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
        
    public void complete(String wizardId, String userId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);
        wizard.setStatus(WizardStatus.COMPLETE);
        repository.save(wizard);
    }

    public void sendSubmitMail(String personId) {
        Person person = personRepository.findById(personId).orElse(null);
        List<Role> roles = roleRepository.findByChildUserId(personId);

        List<String> emailList = new ArrayList<>();
        for (Role role:roles) {
            if (role.getType().equals(RoleType.L1)) {
                emailList.add(role.getParentUserId());
            }
        }

        String[] subjectParameter = {"subject"};
        String[] bodyParameter = {"body1", "body2"};
        try {
            utility.send(person.getEmail(), "submitSubject", "submitBody",subjectParameter, bodyParameter );
            utility.send(emailList, "submitSubject", "submitBody",subjectParameter, bodyParameter );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendL1ApproveMail(String personId) {
        Person person = personRepository.findById(personId).orElse(null);
        List<Role> roles = roleRepository.findByChildUserId(personId);

        List<String> emailList = new ArrayList<>();
        for (Role role:roles) {
            if (role.getType().equals(RoleType.Admin)) {
                emailList.add(role.getParentUserId());
            }
        }

        String[] subjectParameter = {};
        String[] bodyParameter = {};
        try {
            utility.send(person.getEmail(), "submitSubject", "submitBody",subjectParameter, bodyParameter );
            utility.send(emailList, "submitSubject", "submitBody",subjectParameter, bodyParameter );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAdminApproveMail(String personId) {
        Person person = personRepository.findById(personId).orElse(null);
        String[] subjectParameter = {};
        String[] bodyParameter = {};
        try {
            utility.send(person.getEmail(), "submitSubject", "submitBody",subjectParameter, bodyParameter );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRejectMail(String personId) {
        Person person = personRepository.findById(personId).orElse(null);
        String[] subjectParameter = {};
        String[] bodyParameter = {};
        try {
            utility.send(person.getEmail(), "rejectSubject", "rejectBody",subjectParameter, bodyParameter );
        } catch (Exception e) {
            e.printStackTrace();
        }
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
