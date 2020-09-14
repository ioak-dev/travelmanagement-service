package com.westernacher.internal.travelmanagement.service;

import com.westernacher.internal.travelmanagement.controller.representation.Resource;
import com.westernacher.internal.travelmanagement.domain.Wizard;

import java.util.List;

public interface WizardService {

    Wizard createAndUpdate (String userId, Wizard wizard);

    Wizard submit (String wizardId);

    Wizard approveApplicant (String wizardId);

    Wizard rejectApplicant (String wizardId);

    List<Resource.WizardResource> getWizardList (String userId);

    void complete(String wizardId, String userId);

}
