package com.westernacher.internal.travelmanagement.service.implementation;

import com.westernacher.internal.travelmanagement.domain.Person;
import com.westernacher.internal.travelmanagement.domain.PersonStatus;
import com.westernacher.internal.travelmanagement.repository.PersonRepository;
import com.westernacher.internal.travelmanagement.service.PersonService;
import com.westernacher.internal.travelmanagement.util.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@Service
public class DefaultPersonService implements PersonService {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private CSVService csvService;

    @Override
    public Person createAndUpdate (Person person) {
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

    @Override
    public void uploadPersonFile(MultipartFile file) {
        repository.deleteAll();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.endsWith(".csv")) {
            List<String[]> csvline = csvService.readCSVRows(file);
            csvline.stream().forEach(line -> {
                Person person = new Person();
                person.setEmpId(line[0].trim());
                person.setName(line[1].trim());
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                try{
                    person.setJoiningDate(format.parse(line[2].trim()));
                }catch (ParseException e) {

                }

                person.setJobName(line[3].trim());
                person.setUnit(line[4].trim());
                person.setLevel(line[5].trim());
                person.setSpecialization(line[6].trim());
                person.setStatus(PersonStatus.valueOf(line[7].trim()));
                person.setEmail(line[8].trim().toLowerCase());
                try{
                    person.setLastAppraisalDate(format.parse(line[9].trim()));
                }catch(ParseException e){}

                person.setDuration(Integer.parseInt(line[10].trim()));

                repository.save(person);
            });
        }
    }
}
