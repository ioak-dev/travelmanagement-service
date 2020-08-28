
package com.westernacher.internal.travelmanagement.controller;

import com.westernacher.internal.travelmanagement.domain.Person;
import com.westernacher.internal.travelmanagement.domain.PersonStatus;
import com.westernacher.internal.travelmanagement.repository.PersonRepository;
import com.westernacher.internal.travelmanagement.service.implementation.DefaultPersonService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/person")
@Slf4j
public class PersonController {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private DefaultPersonService service;

    @GetMapping
    public List<Person> get () {
        return repository.findAll();
    }

    @PutMapping
    public Person createAndUpdate (@RequestBody Person person) {
        Person existingPerson = repository.findPersonByEmail(person.getEmail());
        if(existingPerson != null) {
            existingPerson.setEmpId(person.getEmpId());
            existingPerson.setName(person.getName());
            existingPerson.setJobName(person.getJobName() != null ? person.getJobName():null);
            existingPerson.setUnit(person.getUnit() != null?person.getUnit():null);
            existingPerson.setJoiningDate(person.getJoiningDate());
            existingPerson.setLevel(person.getLevel() != null ? person.getLevel():null);
            existingPerson.setSpecialization(person.getSpecialization() != null ? person.getSpecialization():null);
            existingPerson.setLastAppraisalDate(person.getLastAppraisalDate());
            existingPerson.setDuration(person.getDuration());
            existingPerson.setStatus(person.getStatus());
            return repository.save(existingPerson);
        }
        return repository.save(person);
    }

    @GetMapping("/{id}")
    public Person get (@PathVariable("id") String id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping("/unit/{unit}")
    public List<Person> getByUnit (@PathVariable("unit") String unit) {
        return repository.findAllByUnit(unit);
    }

    @GetMapping("/email/{email}")
    public Person getPersonByEmail (@PathVariable("email") String email) {
        return repository.findPersonByEmail(email.toLowerCase());
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
    public ResponseEntity<?> uploadPersonFile(@ModelAttribute("file") MultipartFile file) {

        repository.deleteAll();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.endsWith(".csv")) {
            BufferedReader br;
            List<String> csvline = new ArrayList<>();
            try {
                String line;
                InputStream is = file.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    csvline.add(line);
                }
                csvline.remove(0);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            csvline.stream().forEach(line -> {
                String[] values = line.split(",");
                Person person = new Person();
                person.setEmpId(values[0].trim());
                person.setName(values[1].trim());
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                try{
                    person.setJoiningDate(format.parse(values[2].trim()));
                }catch (ParseException e) {

                }

                person.setJobName(values[3].trim());
                person.setUnit(values[4].trim());
                person.setLevel(values[5].trim());
                person.setSpecialization(values[6].trim());
                person.setStatus(PersonStatus.valueOf(values[7].trim()));
                person.setEmail(values[8].trim().toLowerCase());
                try{
                    person.setLastAppraisalDate(format.parse(values[9].trim()));
                }catch(ParseException e){}

                person.setDuration(Integer.parseInt(values[10].trim()));

                repository.save(person);
            });
        }
    return null;
    }

    @GetMapping("/download/employeestatus/{cycleId}")
    public void downloadCSV(HttpServletResponse response, @PathVariable String cycleId) throws IOException{

        /*response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=PersonStatus.csv");

        List<Person> personList = repository.findAll();
        Map<String, Person> personMap = new HashMap<>();
        personList.stream().forEach(person -> {
            personMap.put(person.getId(), person);
        });

        List<Appraisal> appraisalList = appraisalRepository.findAllByCycleId(cycleId);
        List<PersonResource> personResources = new ArrayList<>();
        appraisalList.stream().forEach(appraisal -> {
            PersonResource resource = new PersonResource();
            resource.setEmployeeId(personMap.get(appraisal.getUserId()).getEmpId());
            resource.setEmployeeName(personMap.get(appraisal.getUserId()).getName());
            resource.setEmployeeEmail(personMap.get(appraisal.getUserId()).getEmail());
            resource.setEmployeeStatus(appraisal.getStatus().name());
            personResources.add(resource);
        } );

        backupService.writeDataToCsvUsingStringArray(response.getWriter(), personResources);*/
    }

    @Data
    public static class PersonResource {

        private String employeeId;
        private String employeeName;
        private String employeeEmail;
        private String employeeStatus;
    }

}
