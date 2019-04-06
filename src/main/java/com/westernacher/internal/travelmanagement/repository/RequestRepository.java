package com.westernacher.internal.travelmanagement.repository;

import com.westernacher.internal.travelmanagement.domain.Person;
import com.westernacher.internal.travelmanagement.domain.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RequestRepository extends MongoRepository<Request, String> {
    /*List<Person> findAllByUnit(String unit);
    Person findPersonByEmail(String email);*/
}
