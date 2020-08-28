
package com.westernacher.internal.travelmanagement.controller;

import com.westernacher.internal.travelmanagement.domain.Person;
import com.westernacher.internal.travelmanagement.repository.PersonRepository;
import com.westernacher.internal.travelmanagement.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@RestController
@RequestMapping("/person")
@Slf4j
public class PersonController {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonService service;

    @GetMapping
    public List<Person> get () {
        return repository.findAll();
    }

    @PutMapping
    public Person createAndUpdate (@RequestBody Person person) {
        return service.createAndUpdate(person);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> get (@PathVariable("id") String id) {
        return ResponseEntity.ok(repository.findById(id).orElse(null));
    }

    @GetMapping("/unit/{unit}")
    public ResponseEntity<List<Person>> getByUnit (@PathVariable("unit") String unit) {
        return ResponseEntity.ok(repository.findAllByUnit(unit));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Person> getPersonByEmail (@PathVariable("email") String email) {
        return ResponseEntity.ok(repository.findPersonByEmail(email.toLowerCase()));
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
    public void uploadPersonFile(@ModelAttribute("file") MultipartFile file) {
        service.uploadPersonFile(file);
    }
}
