package com.westernacher.internal.travelmanagement.service;

import com.westernacher.internal.travelmanagement.domain.Person;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

public interface PersonService {
    Person createAndUpdate (Person person);
    void uploadPersonFile(MultipartFile file);
}
