package com.westernacher.internal.travelmanagement.controller;

import com.westernacher.internal.travelmanagement.domain.Project;
import com.westernacher.internal.travelmanagement.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Project> getAll () {
        return repository.findAll();
    }

    @RequestMapping(value = "/customername/{name}", method = RequestMethod.GET)
    public List<Project> getAllByCustomerName (@PathVariable("name") String name) {
        return repository.findAllByCustomerName(name);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Project get (@PathVariable("id") String id) {
        return repository.findById(id).orElse(null);
    }


    @RequestMapping(method = RequestMethod.PUT)
    public Project update (@Valid @RequestBody Project project) {
        return repository.save(project);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void create (@Valid @RequestBody List<Project> projects) {
        repository.saveAll(projects);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete (@PathVariable("id") String id) {
        repository.deleteById(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteAll () {
        repository.deleteAll();
    }

}
