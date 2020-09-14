package com.westernacher.internal.travelmanagement.service.implementation;

import com.westernacher.internal.travelmanagement.domain.Person;
import com.westernacher.internal.travelmanagement.domain.Role;
import com.westernacher.internal.travelmanagement.domain.RoleType;
import com.westernacher.internal.travelmanagement.repository.PersonRepository;
import com.westernacher.internal.travelmanagement.repository.RoleRepository;
import com.westernacher.internal.travelmanagement.service.RoleService;
import com.westernacher.internal.travelmanagement.util.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultRoleService implements RoleService {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CSVService csvService;

    public List<Role> updateAll(List<Role> roles) {
        return repository.saveAll(roles);
    }

    public Role createAndUpdate (Role role) {
        Role existingRole = null;
        if (role.getId() != null) {
            existingRole = repository.findById(role.getId()).orElse(null);
        }

        if (existingRole !=null) {
            existingRole.setParentUserId(role.getParentUserId());
            existingRole.setType(role.getType());
            existingRole.setChildUserId(role.getChildUserId());
            return repository.save(existingRole);
        }
        return repository.save(role);
    }

    public void uploadCsvFile(MultipartFile file) {
        updateAll(getRoles(csvService.readCSVRows(file)));
    }

    private List<Role> getRoles(List<String[]> rows) {

        Map<String, String> personMap = new HashMap<>();

        List<Person> personList = personRepository.findAll();

        personList.stream().forEach(person -> {
            personMap.put(person.getEmail(), person.getId());
        });

        List<Role> roles =  new ArrayList<>();

        for(String[] line : rows) {
            Role role = new Role();
            role.setParentUserId(personMap.get(line[0].trim()));
            role.setType(RoleType.valueOf(line[1].trim()));
            role.setChildUserId(personMap.get(line[2].trim()));
            roles.add(role);
        }
        return roles;

    }

}
