package com.westernacher.internal.travelmanagement.repository;

import com.westernacher.internal.travelmanagement.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RoleRepository extends MongoRepository<Role, String> {

    List<Role> findByChildUserId(String childUserId);
    List<Role> findByParentUserId(String parentUserId);

}
