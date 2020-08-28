package com.westernacher.internal.travelmanagement.service;

import org.springframework.stereotype.Service;

@Service
public interface WizardService {

    void sendSubmitMail(String personId);

    void sendL1ApproveMail(String personId);

    void sendAdminApproveMail(String personId);

    void sendRejectMail(String personId);
}
