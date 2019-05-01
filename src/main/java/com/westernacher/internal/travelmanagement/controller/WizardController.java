package com.westernacher.internal.travelmanagement.controller;

import com.westernacher.internal.travelmanagement.domain.*;
import com.westernacher.internal.travelmanagement.repository.WizardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/wizard")
@Slf4j
public class WizardController {

    @Autowired
    private WizardRepository repository;
    private static final AtomicLong LAST_TIME_MS = new AtomicLong();


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Wizard getWizard (@PathVariable("id") String id) {
        return repository.findById(id).orElse(null);
    }


    @RequestMapping(value = "/{type}/{userId}", method = RequestMethod.GET)
    public List<Wizard> getWizard (@PathVariable("type") String type,
                                   @PathVariable("userId") String userId) {

        return repository.findAllByCreatedBy(userId);
    }


    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public Wizard update (@Valid @RequestBody Wizard wizard, @PathVariable("userId") String userId) {
        if (StringUtils.isEmpty(wizard.getId())) {
            wizard.setCreatedBy(userId);
            wizard.setId(uniqueCurrentTimeMS());
            wizard.setStatus(WizardStatus.DRAFT);
        }
        return repository.save(wizard);
    }


    @RequestMapping(value = "/{wizardId}/submit/{userId}", method = RequestMethod.POST)
    public Wizard applicantSubmit (@PathVariable("wizardId") String wizardId, @PathVariable("userId") String userId) {
        Wizard wizard = repository.findById(wizardId).orElse(null);
        wizard.setStatus(WizardStatus.L1);
        repository.save(wizard);
        return wizard;
    }

    private static String uniqueCurrentTimeMS() {
        long now = System.currentTimeMillis();
        while(true) {
            long lastTime = LAST_TIME_MS.get();
            if (lastTime >= now)
                now = lastTime+1;
            if (LAST_TIME_MS.compareAndSet(lastTime, now))
                return Long.toString(now);
        }
    }
}

