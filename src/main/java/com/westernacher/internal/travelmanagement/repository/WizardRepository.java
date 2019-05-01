package com.westernacher.internal.travelmanagement.repository;

import com.westernacher.internal.travelmanagement.domain.Wizard;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WizardRepository extends MongoRepository<Wizard, String> {

    List<Wizard> findAllByCreatedBy(String createdBy);

}
