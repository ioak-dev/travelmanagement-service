package com.westernacher.internal.travelmanagement.service;

import com.westernacher.internal.travelmanagement.domain.Person;
import org.springframework.web.multipart.MultipartFile;

public interface PersonService {
    Person createAndUpdate (Person person);

    void uploadPersonFile(MultipartFile file);
}
