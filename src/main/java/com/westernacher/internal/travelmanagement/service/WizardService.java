package com.westernacher.internal.travelmanagement.service;

import com.westernacher.internal.travelmanagement.domain.Person;
import com.westernacher.internal.travelmanagement.domain.Role;
import com.westernacher.internal.travelmanagement.domain.RoleType;
import com.westernacher.internal.travelmanagement.repository.PersonRepository;
import com.westernacher.internal.travelmanagement.repository.RoleRepository;
import com.westernacher.internal.travelmanagement.repository.WizardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WizardService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private WizardRepository repository;

    @Autowired
    private EmailUtility utility;

    private RoleRepository roleRepository;

    public void sendSubmitMail(String personId) {
        Person person = personRepository.findById(personId).orElse(null);
        List<Role> roles = roleRepository.findByChildEmailId(person.getEmail());

        List<String> emailList = new ArrayList<>();
        for (Role role:roles) {
            if (role.getType().equals(RoleType.L1)) {
                emailList.add(role.getParentEmailId());
            }
        }

        String[] subjectParameter = {};
        String[] bodyParameter = {};
        try {
            utility.send(person.getEmail(), "submit_Confirmation_Mail_Subject", "submit_Confirmation_Mail_Body",subjectParameter, bodyParameter );
            utility.send(emailList, "submit_notification_subject", "submit_notification_body",subjectParameter, bodyParameter );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendL1ApproveMail(String personId) {
        Person person = personRepository.findById(personId).orElse(null);
        List<Role> roles = roleRepository.findByChildEmailId(person.getEmail());

        List<String> emailList = new ArrayList<>();
        for (Role role:roles) {
            if (role.getType().equals(RoleType.Admin)) {
                emailList.add(role.getParentEmailId());
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
}
