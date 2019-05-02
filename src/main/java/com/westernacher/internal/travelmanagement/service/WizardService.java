package com.westernacher.internal.travelmanagement.service;

import com.westernacher.internal.travelmanagement.domain.Person;
import com.westernacher.internal.travelmanagement.domain.Role;
import com.westernacher.internal.travelmanagement.domain.RoleType;
import com.westernacher.internal.travelmanagement.repository.PersonRepository;
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

    public void sendSubmitMail(String personId) {
        Person person = personRepository.findById(personId).orElse(null);
        List<Person> personList = personRepository.findAll();

        List<String> emailList = new ArrayList<>();
        for (Person person1:personList) {
            if (person1.getRoles().contains(RoleType.L1)) {
                emailList.add(person1.getEmail());
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

    public void sendL1ApproveMail(String personId) {
        Person person = personRepository.findById(personId).orElse(null);
        List<Person> personList = personRepository.findAll();

        List<String> emailList = new ArrayList<>();
        for (Person person1:personList) {
            if (person1.getRoles().contains(RoleType.Admin)) {
                emailList.add(person1.getEmail());
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
