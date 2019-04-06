package com.westernacher.internal.travelmanagement.controller;

import com.westernacher.internal.travelmanagement.domain.Person;
import com.westernacher.internal.travelmanagement.domain.Request;
import com.westernacher.internal.travelmanagement.domain.Role;
import com.westernacher.internal.travelmanagement.domain.Wizard;
import com.westernacher.internal.travelmanagement.repository.PersonRepository;
import com.westernacher.internal.travelmanagement.repository.RequestRepository;
import com.westernacher.internal.travelmanagement.service.PersonService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/wizard")
public class RequestController {

    @Autowired
    private RequestRepository repository;

    @RequestMapping(value ="/draft/{personId}", method = RequestMethod.GET)
    public AvailableResource isAvailable (@PathVariable("personId") String personId) {

        Request request = repository.findById(personId).orElse(null);
        if (request!=null) {
            return new AvailableResource("yes", request.getId());
        }
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Request getRequestWizard (@PathVariable("id") String id) {
        return repository.findById(id).orElse(null);
    }

    @RequestMapping(value = "/create/{personId}", method = RequestMethod.POST)
    public void create (@PathVariable("personId") String personId,
                                @Valid @RequestBody Wizard wizard) {
        repository.save(Request.builder().status("DRAFT").personId(personId).wizard(wizard).build());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Request update (@PathVariable("id") String id,
                          @Valid @RequestBody Wizard wizard) {
        Request request = repository.findById(id).orElse(null);
        if (request!=null) {
            repository.save(Request.builder().id(id).status(request.getStatus()).personId(request.getPersonId()).wizard(wizard).build());
            return request;
        }
        return null;
    }



    @Data
    @AllArgsConstructor
    public static class AvailableResource {

        private String available;
        private String id;

    }
}

